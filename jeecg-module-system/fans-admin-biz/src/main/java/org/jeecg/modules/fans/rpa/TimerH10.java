package org.jeecg.modules.fans.rpa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.fans.Tools;
import org.jeecg.modules.fans.base.entity.FansName;
import org.jeecg.modules.fans.base.mapper.FansNameMapper;
import org.jeecg.modules.fans.content.entity.FansPostPub;
import org.jeecg.modules.fans.content.mapper.FansPostPubMapper;
import org.jeecg.modules.fans.interact.entity.FansCtrlAccount;
import org.jeecg.modules.fans.interact.entity.FansCtrlNode;
import org.jeecg.modules.fans.interact.entity.FansList;
import org.jeecg.modules.fans.interact.mapper.FansCtrlAccountMapper;
import org.jeecg.modules.fans.interact.mapper.FansCtrlNodeMapper;
import org.jeecg.modules.fans.interact.mapper.FansListMapper;
import org.jeecg.modules.fans.market.entity.FansLib;
import org.jeecg.modules.fans.market.mapper.FansLibMapper;
import org.jeecg.modules.fans.rpa.vo.CrawVo;
import org.jeecg.modules.fans.rpa.vo.FansVo;
import org.jeecg.modules.fans.rpa.vo.PostDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//每日处理非实时事务
@Service
@Slf4j
@Component
public class TimerH10 {
    @Autowired
    private SheetOper sheetOper;
    @Autowired
    private Fb2Sheet fb2Sheet;
    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;
    @Value(value = "${jeecg.path.uploadAccess}")
    private String uploadAccess;
    @Value(value = "${ai.sdPath}")
    private String sdPath;
    @Autowired
    private FansListMapper fansListMapper;
    @Autowired
    private FansCtrlAccountMapper fansCtrlAccountMapper;
    @Autowired
    private FansPostPubMapper fansPostPubMapper;
    @Autowired
    private FansLibMapper fansLibMapper;
    @Autowired
    private FansNameMapper fansNameMapper;
    @Autowired
    private FansCtrlNodeMapper fansCtrlNodeMapper;
    //好友名单数据处理
    private void syncFansFData(GoogleCredentials credentials){
        //所有正常的账号
        QueryWrapper<FansCtrlAccount> qw=new QueryWrapper<FansCtrlAccount>();
     //   qw.eq("fans_domain","facebook");
        qw.eq("online_status","ok");
        List<FansCtrlAccount> list=this.fansCtrlAccountMapper.selectList(qw);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                FansCtrlAccount fansCtrlAccount=list.get(i);
                List<FansVo> fansVos=this.fb2Sheet.crawFansData(credentials,fansCtrlAccount.getUsername());
                if(fansVos!=null && fansVos.size()>0 ){
                    for (int j = 0; j < fansVos.size(); j++) {
                        FansVo vo=fansVos.get(j);
                        //在粉库中存在是主动好友，不在粉库中是被动好友
                        String joinWay="passivity";
                        QueryWrapper<FansLib> qwFansLib=new QueryWrapper<FansLib>();
                        qwFansLib.eq("link",vo.getFansLink());
                        long countLib=this.fansLibMapper.selectCount(qwFansLib);
                        if(countLib>0){
                            joinWay="initiative";
                            //更新粉库状态到f1
                            UpdateWrapper<FansLib> uw=new UpdateWrapper<FansLib>();
                            uw.eq("account",fansCtrlAccount.getUsername());
                            uw.eq("link",vo.getFansLink());
                            uw.set("search_state","f1");
                            uw.set("friend_time",new Date());
                            this.fansLibMapper.update(null,uw);
                        }else{
                            //补充到粉库
                            FansLib fansLib=new FansLib();
                            fansLib.setLink(vo.getFansLink());
                            fansLib.setFriendTime(new Date());
                            fansLib.setName(vo.getFansNickname());
                            fansLib.setProfile(vo.getFansProfile());
                            fansLib.setSearchState("f1");
                            fansLib.setAccount(fansCtrlAccount.getUsername());
                            fansLib.setCg(fansCtrlAccount.getCg());
                            this.fansLibMapper.insert(fansLib);
                        }

                        QueryWrapper<FansList> qwFans=new QueryWrapper<FansList>();
                        qwFans.eq("username",fansCtrlAccount.getUsername());
                        qwFans.eq("fans_link",vo.getFansLink());
                        long fansCount=this.fansListMapper.selectCount(qwFans);
                        if(fansCount==0){
                            FansList fans =new FansList();
                            fans.setFansLink(vo.getFansLink());
                            fans.setFansNickname(vo.getFansNickname());
                            fans.setFansProfile(vo.getFansProfile());
                            fans.setFansStatus("friend");
                            fans.setJoinTime(vo.getSyncFansDataDateTime());
                            fans.setJoinWay(joinWay);
                            fans.setUsername(fansCtrlAccount.getUsername());
                            this.fansListMapper.insert(fans);
                        }

                    }

                    //更新账户的好友数
                    QueryWrapper<FansList> qwFans1=new QueryWrapper<FansList>();
                    qwFans1.eq("username",fansCtrlAccount.getUsername());
                    long fansCount1=this.fansListMapper.selectCount(qwFans1);
                    UpdateWrapper<FansCtrlAccount> uw1=new UpdateWrapper<FansCtrlAccount>();
                    uw1.eq("username",fansCtrlAccount.getUsername());
                    //uw1.set("activetime",new Date());
                    uw1.set("friend_num",fansCount1);
                    this.fansCtrlAccountMapper.update(null,uw1);
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){

                    }
                }


            }
        }
    }
    //养号处理
    private void syncActive(GoogleCredentials credentials){
        //所有需要养号的
        QueryWrapper<FansCtrlAccount> qw=new QueryWrapper<FansCtrlAccount>();
    //    qw.eq("fans_domain","facebook");

        qw.apply("(sync_active_date_time is null or sync_active_date_time < CURRENT_DATE()) or online_status='error'");
        List<FansCtrlAccount> list=this.fansCtrlAccountMapper.selectList(qw);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                FansCtrlAccount account=list.get(i);
                Date newSyncDate=this.fb2Sheet.checkActive(credentials,account.getUsername(),account.getSyncActiveDateTime());
                if(newSyncDate==null){
                    account.setOnlineStatus("error");
                }else{
                    account.setSyncActiveDateTime(newSyncDate);
                    account.setActivetime(newSyncDate);
                    account.setOnlineStatus("ok");
                }

                this.fansCtrlAccountMapper.updateById(account);
                try{
                    Thread.sleep(1000);
                }catch (Exception e){

                }

            }
        }
    }
    //帖子运营数据处理
    private void syncPostData(GoogleCredentials credentials){
        //重建工作表
        //获取所有小号
        QueryWrapper<FansCtrlAccount> qw1=new QueryWrapper<FansCtrlAccount>();
        qw1.eq("online_status","ok");
        qw1.eq("account_type","small");
        List<FansCtrlAccount> accountSmallList=this.fansCtrlAccountMapper.selectList(qw1);
        if(accountSmallList!=null){
            for (int i = 0; i < accountSmallList.size(); i++) {
                FansCtrlAccount accountSmall=accountSmallList.get(i);
                //获取每个小号挂的号
                QueryWrapper<FansCtrlAccount> qw2=new QueryWrapper<FansCtrlAccount>();
                qw2.eq("account_small",accountSmall.getUsername());
                qw2.eq("online_status","ok");
             //   qw2.eq("account_type","main");
                List<FansCtrlAccount> accountMainList=this.fansCtrlAccountMapper.selectList(qw2);
                if(accountMainList!=null && accountMainList.size()>0){
                    //删除并新建小号的工作表
                    if(accountSmall.getSheetIdFbCraw()!=null && accountSmall.getSheetIdFbCraw()>0){
                        //有些小号可能是新建的没有sheet
                        fb2Sheet.deleteSheetIdFbCraw(credentials,accountSmall.getUsername(),accountSmall.getSheetIdFbCraw());
                    }
                    //形成小号内容
                    List<CrawVo> list=new ArrayList<CrawVo>();
                    for (int j = 0; j < accountMainList.size(); j++) {
                        CrawVo vo =new CrawVo();
                        vo.setUsername(accountMainList.get(j).getUsername());
                        vo.setLink(accountMainList.get(j).getLink());
                        list.add(vo);
                    }
                    int sheetIdFbCraw=fb2Sheet.createSheetIdFbCraw(credentials,accountSmall.getUsername(),list);
                        if(sheetIdFbCraw>0){
                            //更新小号的爬虫sheetid
                            UpdateWrapper<FansCtrlAccount> uw=new UpdateWrapper<FansCtrlAccount>();
                            uw.eq("username",accountSmall.getUsername());
                            uw.set("sheet_id_fb_craw",sheetIdFbCraw);
                            this.fansCtrlAccountMapper.update(null,uw);
                        }
                }
            }
        }

        //所有已发布的帖子的账号
        List<String> list=this.fansPostPubMapper.selectHasFansPostPub("facebook");
        if(list!=null){
            for(int i=0;i<list.size();i++){
                String username=list.get(i);
                List<PostDataVo> postDataVos=this.fb2Sheet.crawPostData(credentials,username);
                if(postDataVos!=null && postDataVos.size()>0 ){
                    for (int j = 0; j < postDataVos.size(); j++) {
                        PostDataVo vo=postDataVos.get(j);
                        UpdateWrapper<FansPostPub> uw=new UpdateWrapper<FansPostPub>();
                        uw.eq("post_url",vo.getPostUrl());
                        uw.set("sync_post_data_date_time",vo.getSyncPostDataDateTime());
                        //为零可能有错误，即使没错没必要更新
                        if(vo.getGoodNum()>0){
                            uw.set("good_num",vo.getGoodNum());
                        }
                        if(vo.getCommentNum()>0){
                            uw.set("comment_num",vo.getCommentNum());
                        }
                        if(vo.getShareNum()>0){
                            uw.set("share_num",vo.getShareNum());
                        }
                        if(vo.getGoodNum()==0 && vo.getCommentNum()==0 && vo.getShareNum()==0){
                            continue;
                        }
                        this.fansPostPubMapper.update(null,uw);
                    }
                }

                try{
                    Thread.sleep(1000);
                }catch (Exception e){

                }

            }
        }
    }
    //根据号源特点，到时间就自动生成名字和头像名（如果所需）
    private void genarateNameProfile(){
        String faceurl=sdPath+"/human/headPhoto";

        //头像
        QueryWrapper<FansCtrlAccount> qw1=new QueryWrapper<FansCtrlAccount>();
        qw1.eq("auto_profile","1");
        qw1.isNull("profile");
        List<FansCtrlAccount> accountList=this.fansCtrlAccountMapper.selectList(qw1);
        if(accountList!=null){
            for (int i = 0; i < accountList.size(); i++) {
                FansCtrlAccount account = accountList.get(i);
                //根据号源是否是本国，判断时间
                Long days= Tools.subDay(new Date(),account.getCreateTime());
                if(account.getLocalSrc()==1){
                    //本国6天后
                    if(days<6){
                        continue;
                    }
                }else{
                    //异国20天后
                    if(days<20){
                        continue;
                    }
                }
                //调用生头像接口
                String profile=null;
                JSONObject postData=new JSONObject();
                postData.put("nationality",account.getCountry());
                postData.put("age",30);
                postData.put("hairColor","brown");
                postData.put("nums",1);
                postData.put("gender","woman");
                postData.put("prompt","");
                try {
                    JSONObject retdata = Tools.post(faceurl, postData);
                    if(retdata!=null && retdata.getJSONObject("data")!=null  && retdata.getJSONObject("data").getJSONObject("outputImages")!=null) {
                        JSONObject img = retdata.getJSONObject("data").getJSONObject("outputImages");
                        profile =  "profile/" + account.getUsername()+"_profile.png";
                        String savepathWhole =  uploadpath+"/"+profile;
                        Tools.FileDownloader(img.getString("img"), savepathWhole);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                if(profile!=null){
                    account.setProfile(profile);
                    //更新状态为wait
                    account.setProfileInfoState("wait");
                    this.fansCtrlAccountMapper.updateById(account);
                }
            }
        }

        ////////名字
        QueryWrapper<FansCtrlAccount> qw2=new QueryWrapper<FansCtrlAccount>();
        qw2.eq("auto_name","1");
        qw2.isNull("name");
        List<FansCtrlAccount> accountList2=this.fansCtrlAccountMapper.selectList(qw2);
        if(accountList2!=null){
            for (int i = 0; i < accountList2.size(); i++) {
                FansCtrlAccount account = accountList2.get(i);
                //根据号源是否是本国，判断时间
                Long days= Tools.subDay(new Date(),account.getCreateTime());
                if(account.getLocalSrc()==1){
                    //本国3天后
                    if(days<3){
                        continue;
                    }
                }else{
                    //异国10天后
                    if(days<10){
                        continue;
                    }
                }
                //从名字库找一个名字
                QueryWrapper<FansName> qwName=new QueryWrapper<FansName>();
                qwName.eq("country",account.getCountry());
                if(account.getFansDomain().equals("facebook")){
                    qwName.eq("fb_use",0);
                }
                qwName.last("limit 1");
                FansName fansName=fansNameMapper.selectOne(qwName);
                if(fansName!=null){
                    //生成名字
                    account.setName(fansName.getFname()+" "+fansName.getName());
                    if(account.getFansDomain().equals("facebook")){
                        fansName.setFbUse(1);
                        this.fansNameMapper.updateById(fansName);
                    }
                    //更新状态为wait
                    account.setNameInfoState("wait");
                    this.fansCtrlAccountMapper.updateById(account);
                }
            }
        }
    }
    //更新节点状态停止
    private void updateCtrlNodeStatusStop(){
        QueryWrapper<FansCtrlNode> qw=new QueryWrapper<FansCtrlNode>();
        qw.apply("activetime< CURRENT_DATE");
        List<FansCtrlNode> list=this.fansCtrlNodeMapper.selectList(qw);
        if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                FansCtrlNode obj=list.get(i);
                obj.setCtrlStatus("stop");
                this.fansCtrlNodeMapper.updateById(obj);
            }
        }
    }
    @Scheduled(cron = "10 1 0/12 * * ?")
   // @Scheduled(cron = "0 0/1 * * * ?")
    public void run() {
        System.out.println("TimerH10 !  时间:" + DateUtils.getTimestamp());
        updateCtrlNodeStatusStop();
        genarateNameProfile();
        GoogleCredentials credentials=null;
        try{
            credentials=sheetOper.genarateGoogleCredentials();
       //     syncPostData(credentials);
            syncFansFData(credentials);
         //   syncActive(credentials);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            credentials=null;
        }
    }
}
