package org.jeecg.modules.fans.rpa;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.fans.Tools;
import org.jeecg.modules.fans.base.entity.FansProxy;
import org.jeecg.modules.fans.base.mapper.FansProxyMapper;
import org.jeecg.modules.fans.content.entity.FansPostPub;
import org.jeecg.modules.fans.content.mapper.FansPostPubMapper;
import org.jeecg.modules.fans.interact.entity.FansCtrlAccount;
import org.jeecg.modules.fans.interact.entity.FansCtrlNode;
import org.jeecg.modules.fans.interact.entity.FansList;
import org.jeecg.modules.fans.interact.mapper.FansCtrlAccountMapper;
import org.jeecg.modules.fans.interact.mapper.FansCtrlNodeMapper;
import org.jeecg.modules.fans.interact.mapper.FansListMapper;
import org.jeecg.modules.fans.market.entity.FansLib;
import org.jeecg.modules.fans.market.entity.FansSearch;
import org.jeecg.modules.fans.market.mapper.FansLibMapper;
import org.jeecg.modules.fans.market.mapper.FansSearchMapper;
import org.jeecg.modules.fans.rpa.vo.SheetId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

//每隔10分钟运行
@Service
@Slf4j
@Component
public class TimerM10 {
    @Autowired
    private SheetOper sheetOper;
    @Autowired
    private Fb2Sheet fb2Sheet;

    @Autowired
    private FansCtrlAccountMapper fansCtrlAccountMapper;
    @Autowired
    private FansCtrlNodeMapper fansCtrlNodeMapper;
    @Autowired
    private FansProxyMapper fansProxyMapper;
    @Autowired
    private FansPostPubMapper fansPostPubMapper;
    @Autowired
    private FansListMapper fansListMapper;
    @Autowired
    private FansSearchMapper fansSearchMapper;
    @Autowired
    private FansLibMapper fansLibMapper;

    @Value(value = "${jeecg.path.uploadAccess}")
    private String uploadAccess;//图片访问地址

    //自动加好友
    private void autoAddFriend(GoogleCredentials credentials){
        //查询已备选的名单
        QueryWrapper<FansLib> qwFansLib=new QueryWrapper<FansLib>();
        qwFansLib.eq("search_state","ready");
        List<FansLib> list=this.fansLibMapper.selectList(qwFansLib);
        if(list!=null && list.size()>0){
            //查询大号+客群名单，放在map里，以客群为key，list为value
            Map<String,List<FansCtrlAccount>> map=new HashMap<String,List<FansCtrlAccount>>();
            QueryWrapper<FansCtrlAccount> qwAccount=new QueryWrapper<FansCtrlAccount>();
            qwAccount.eq("online_status","ok");
            qwAccount.eq("account_type","main");
            List<FansCtrlAccount> listAccount=this.fansCtrlAccountMapper.selectList(qwAccount);
            if(listAccount!=null && listAccount.size()>0){
                for (int i = 0; i < listAccount.size(); i++) {
                    FansCtrlAccount account=listAccount.get(i);
                    List<FansCtrlAccount> listAccountCg=map.get(account.getCg());
                    if(listAccountCg==null){
                        listAccountCg=new ArrayList<FansCtrlAccount>();
                    }
                    listAccountCg.add(account);
                    map.put(account.getCg(),listAccountCg);
                }
                //遍历待加名单，随机匹配 下发指令
                for (int j = 0; j < list.size(); j++) {
                    FansLib fansLib=list.get(j);
                    List<FansCtrlAccount> listAccountCg=map.get(fansLib.getCg());
                    if(listAccountCg!=null && listAccountCg.size()>0){
                        int pos=Tools.randomN(listAccountCg.size()-1);
                        String username=listAccountCg.get(pos).getUsername();
                        String link=fansLib.getLink();
                        fansLib.setAccount(username);
                        fansLib.setSearchState("f0");
                        fansLib.setFriendTime(new Date());
                        //下发指令
                        boolean ret=this.fb2Sheet.addFriend(credentials,username,link);
                        if(ret){
                            this.fansLibMapper.updateById(fansLib);
                        }
                    }
                }
            }
        }


    }
    //搜粉结果采集
    private void searchFansRet(GoogleCredentials credentials){
        //查询已有小号
        QueryWrapper<FansSearch> qw=new QueryWrapper<FansSearch>();
        qw.isNotNull("username");
        List<FansSearch> fansSearchList=this.fansSearchMapper.selectList(qw);
        if(fansSearchList!=null && fansSearchList.size()>0){
            Set<String> usernameSet=new HashSet<String>();
            Map<String,String> cgMap=new HashMap<String,String>();
            for(int i=0;i<fansSearchList.size();i++){
                String usernameSmall=fansSearchList.get(i).getUsername();
                if(!usernameSet.contains(usernameSmall)){
                    usernameSet.add(usernameSmall);
                }
                cgMap.put(fansSearchList.get(i).getUrl(),fansSearchList.get(i).getCg());
            }
            Iterator<String> iterator=usernameSet.iterator();
            while(iterator.hasNext()){
                String usernameSmall=iterator.next();
                //System.out.println("username:"+username);
                List<FansLib> list=this.fb2Sheet.crawSearchFansRetData(credentials,usernameSmall);

                    if(list!=null && list.size()>0){
                        Iterator<FansLib> iteratorFansLib=list.iterator();
                        while(iteratorFansLib.hasNext()){
                            FansLib fansLib=iteratorFansLib.next();
                            //确定客群
                            fansLib.setCg(cgMap.get(fansLib.getSearchUrl()));
                            //筛选不合格的
                            if(fansLib.getProfile().indexOf("143086968_2856368904622192_1959732218791162458_n.png")>0 ){
                                //无头像
                                fansLib.setSearchState("out");
                            }else if(fansLib.getInfo()==null  || fansLib.getInfo().equals("无")){
                                //无个人信息
                                fansLib.setSearchState("out");
                            }

                            if(fansLib.getFansNum()==null ||fansLib.getFansNum().equals("无")|| fansLib.getFansNum().equals("好友")){
                                fansLib.setFansNum("");
                            }
                            //if()
                            //去重写入db
                         //   QueryWrapper<FansLib> qwFansLib=new QueryWrapper<FansLib>();
                        //    qwFansLib.eq("link",fansLib.getLink());
                            //   long count=this.fansLibMapper.selectCount(qwFansLib);
                            //  if(count==0){
                            try{
                                this.fansLibMapper.insert(fansLib);
                            }catch (java.lang.Throwable e){}
                            //  }
                        }


                    }
            }
            //更新计划表的实际数量
            for(int i=0;i<fansSearchList.size();i++){
                FansSearch fansSearch=fansSearchList.get(i);
                String searchUrl=fansSearch.getUrl();
                QueryWrapper<FansLib> qw1=new QueryWrapper<FansLib>();
                qw1.eq("search_url",searchUrl);
                long count=this.fansLibMapper.selectCount(qw1);
                fansSearch.setUserNum((int)count);
                this.fansSearchMapper.updateById(fansSearch);
            }
        }



    }
    //发帖反馈
    private void syncSendPost(GoogleCredentials credentials){
        //遍历所有发送中的帖子
        QueryWrapper<FansPostPub> qw=new QueryWrapper<FansPostPub>();
        List<String> str=new ArrayList<String>();
        str.add("start");
        str.add("error");
        qw.in("is_publish",str);
        //qw.eq("domain","facebook");
        List<FansPostPub> list=this.fansPostPubMapper.selectList(qw);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                FansPostPub fansPostPub=list.get(i);
                String  postUrl=this.fb2Sheet.checkSendPostStatus(credentials,fansPostPub.getPostId(),fansPostPub.getDomainAccount());
                if(postUrl==null){
                    fansPostPub.setIsPublish("error");
                }else if(postUrl.equals("")){//不知道成功还是失败

                }else{
                    fansPostPub.setIsPublish("ok");
                    fansPostPub.setPublishTime(new Date());
                    //更新帖子url
                    fansPostPub.setPostUrl(postUrl);

                }
                this.fansPostPubMapper.updateById(fansPostPub);
                if(fansPostPub.getIsPublish().equals("ok")){
                    //发帖成功，更新本账户的发帖量,活跃时间
                    QueryWrapper<FansPostPub> qw1=new QueryWrapper<FansPostPub>();
                    qw1.eq("domain_account",fansPostPub.getDomainAccount());
                    qw1.eq("is_publish","ok");
                    long num=this.fansPostPubMapper.selectCount(qw1);
                    UpdateWrapper<FansCtrlAccount> uw=new UpdateWrapper<FansCtrlAccount>();
                    uw.eq("username",fansPostPub.getDomainAccount());
                    uw.set("activetime",new Date());
                    uw.set("post_num",num);
                    this.fansCtrlAccountMapper.update(null,uw);
                }
            }
        }
    }
    //同步个人信息
    private void syncInfo(GoogleCredentials credentials){
        //遍历所有等待挂ads的
        QueryWrapper<FansCtrlAccount> qw=new QueryWrapper<FansCtrlAccount>();
        List<String> str=new ArrayList<String>();
        qw.eq("mount_state","ok");
        qw.apply("profile_info_state ='wait' or name_info_state ='wait' or cover_info_state ='wait'");

        List<FansCtrlAccount> list=this.fansCtrlAccountMapper.selectList(qw);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                FansCtrlAccount account=list.get(i);
                if(account.getProfileInfoState()!=null && account.getProfileInfoState().equals("wait")){
                    boolean ret=this.fb2Sheet.syncInfo( credentials,"profile",uploadAccess+account.getProfile(),account.getUsername());
                    if(ret){
                        account.setProfileInfoState("doing");
                    }
                }
                if(account.getNameInfoState()!=null && account.getNameInfoState().equals("wait")){
                    boolean ret=this.fb2Sheet.syncInfo( credentials,"name",account.getName(),account.getUsername());
                    if(ret){
                        account.setNameInfoState("doing");
                    }
                }
                if(account.getCoverInfoState()!=null && account.getCoverInfoState().equals("wait")){
                    boolean ret=this.fb2Sheet.syncInfo( credentials,"cover",uploadAccess+account.getCover(),account.getUsername());
                    if(ret){
                        account.setCoverInfoState("doing");
                    }
                }
                if(account.getIntroInfoState()!=null && account.getIntroInfoState().equals("wait")){
                    boolean ret=this.fb2Sheet.syncInfo( credentials,"intro",account.getIntro(),account.getUsername());
                    if(ret){
                        account.setIntroInfoState("doing");
                    }
                }
                this.fansCtrlAccountMapper.updateById(account);

            }
        }
    }
    //同步个人信息反馈
    private void syncInfoResp(GoogleCredentials credentials){
        //遍历所有未获取到反馈的
        QueryWrapper<FansCtrlAccount> qw=new QueryWrapper<FansCtrlAccount>();
        qw.eq("mount_state","ok");
        qw.apply("profile_info_state ='doing' or name_info_state ='doing' or cover_info_state ='doing' or profile_info_state ='doing'");

        Date now =new Date();
        List<FansCtrlAccount> list=this.fansCtrlAccountMapper.selectList(qw);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                FansCtrlAccount account=list.get(i);
                if(account.getProfileInfoState()!=null && account.getProfileInfoState().equals("doing")){
                    boolean ret=this.fb2Sheet.checkSyncInfo( credentials,"profile",account.getUsername());
                    if(ret){
                        account.setProfileInfoState("done");
                        account.setLastProfileDate(new Date());
                    }
                }
                if(account.getNameInfoState()!=null && account.getNameInfoState().equals("doing")){
                    boolean ret=this.fb2Sheet.checkSyncInfo( credentials,"name",account.getUsername());
                    if(ret){
                        account.setNameInfoState("done");
                        account.setLastNameDate(new Date());
                    }
                }
                if(account.getCoverInfoState()!=null && account.getCoverInfoState().equals("doing")){
                    boolean ret=this.fb2Sheet.checkSyncInfo( credentials,"cover",account.getUsername());
                    if(ret){
                        account.setCoverInfoState("done");
                        account.setLastCoverDate(new Date());
                    }
                }
                if(account.getIntroInfoState()!=null && account.getIntroInfoState().equals("doing")){
                    boolean ret=this.fb2Sheet.checkSyncInfo( credentials,"intro",account.getUsername());
                    if(ret){
                        account.setIntroInfoState("done");
                        account.setLastIntroDate(new Date());
                    }
                }

                this.fansCtrlAccountMapper.updateById(account);
            }
        }
    }
    //加好友反馈
    private void syncAddFriend(GoogleCredentials credentials){
        //遍历所有发出请求的好友
        QueryWrapper<FansList> qw=new QueryWrapper<FansList>();
        qw.eq("fans_status","toFriend");
        List<FansList> list=this.fansListMapper.selectList(qw);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                FansList fans=list.get(i);
                String  status=this.fb2Sheet.checkAddFriendStatus(credentials,fans.getUsername(),fans.getFansNickname());
                if(status==null){
                    fans.setFansStatus("toFriendErr");
                }else if(status.equals("ok")){
                    fans.setJoinWay("initiative");
                    fans.setFansStatus("friend");
                }else if(status.equals("error")){
                    fans.setFansStatus("toFriendErr");
                }else if(status.equals("noneed")){
                    fans.setFansStatus("friend");
                }
                this.fansListMapper.updateById(fans);
            }
        }
    }

    //ads上号
    private void mountAccountToSheet(GoogleCredentials credentials){
        //遍历未上号的
        QueryWrapper<FansCtrlAccount> qw=new QueryWrapper<FansCtrlAccount>();
        qw.eq("mount_state","un_ads");
        List<FansCtrlAccount> list=this.fansCtrlAccountMapper.selectList(qw);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                FansCtrlAccount account=list.get(i);


                //分配ads代理id 当前活跃最少的
                QueryWrapper<FansCtrlNode> qw1=new QueryWrapper<FansCtrlNode>();
                qw1.orderByAsc("online_num");
                qw1.last("limit 1");
                FansCtrlNode node=this.fansCtrlNodeMapper.selectOne(qw1);
                if(node!=null){
                    account.setAgentId(node.getId());
                }else{
                    account.setAgentId("0");
                }
                //关联代理具体信息
                FansProxy fansProxy=this.fansProxyMapper.selectById(account.getProxyId());
                //增加账号表新记录
                boolean ret=this.fb2Sheet.mountAccountToSheet(credentials,account,fansProxy);
                if(ret){
                    account.setMountState("un_login0");
                    this.fansCtrlAccountMapper.updateById(account);
                }

                //创建一般信息表
                /*
                SheetId sheetId=this.fb2Sheet.createAccount(credentials,account.getBrowserNo());
                if(sheetId==null){
                    try{
                        Thread.sleep(2000);
                    }catch(Exception e){}
                    continue;
                }
                */
            }
        }
    }
    //ads上号反馈
    private void mountAccountResp(GoogleCredentials credentials){
        //遍历已经上号
        QueryWrapper<FansCtrlAccount> qw=new QueryWrapper<FansCtrlAccount>();
        List<String> str=new ArrayList<String>();
        str.add("un_login0");
        str.add("un_2fa");
        qw.in("mount_state",str);
        List<FansCtrlAccount> list=this.fansCtrlAccountMapper.selectList(qw);

        if(list!=null){
            for(int i=0;i<list.size();i++){
                FansCtrlAccount account=list.get(i);

                //查询google账号表更新情况,更新 account
                this.fb2Sheet.mountAccountResp(credentials,account);
                this.fansCtrlAccountMapper.updateById(account);

                //全部成功后，创建一般信息表
               if(account.getMountState().equals("ok")){
                   SheetId sheetId=this.fb2Sheet.createAccount(credentials,account.getUsername());
                   if(sheetId!=null){
                       account.setSheetIdFbMsgData(sheetId.getSheetIdFbMsgData());
                       account.setSheetIdFbPostData(sheetId.getSheetIdFbPostData());
                       account.setSheetIdFbFriendData(sheetId.getSheetIdFbFriendData());
                       this.fansCtrlAccountMapper.updateById(account);
                   }
               }

            }
        }
    }
    @Scheduled(cron = "0 0/10 * * * ?")
   // @Scheduled(cron = "0 0/1 * * * ?")
    public void run() {
        System.out.println("TimerM10 !  时间:" + DateUtils.getTimestamp());

        GoogleCredentials credentials=null;
        try{

            credentials=sheetOper.genarateGoogleCredentials();
            mountAccountToSheet(credentials);
            mountAccountResp(credentials);
            syncInfo(credentials);
            syncInfoResp(credentials);
            syncSendPost(credentials);
            //syncAddFriend(credentials);

            //searchFansRet(credentials);
            //autoAddFriend(credentials);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            credentials=null;
        }
    }
}
