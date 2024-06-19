package org.jeecg.modules.fans.rpa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.fans.Tools;
import org.jeecg.modules.fans.content.entity.FansPostPub;
import org.jeecg.modules.fans.content.mapper.FansPostPubMapper;
import org.jeecg.modules.fans.content.service.IFansPostService;
import org.jeecg.modules.fans.interact.entity.FansCtrlAccount;
import org.jeecg.modules.fans.interact.entity.FansList;
import org.jeecg.modules.fans.interact.mapper.FansCtrlAccountMapper;
import org.jeecg.modules.fans.interact.mapper.FansListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//每隔1分钟运行
@Service
@Slf4j
@Component
public class TimerM1 {

    @Autowired
    private FansCtrlAccountMapper fansCtrlAccountMapper;
    @Autowired
    private FansPostPubMapper fansPostPubMapper;
    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;
    @Value(value = "${jeecg.path.uploadAccess}")
    private String uploadAccess;
    @Value(value = "${ai.sdPath}")
    private String sdPath;
    @Autowired
    private IFansPostService fansPostService;


    //定时后台换脸
    private void changeFace(){
        String url=sdPath+"/human/faceSwap";


   //     uploadAccess="http://ec2-52-76-246-31.ap-southeast-1.compute.amazonaws.com/fansAdmin/sys/common/static/";
        QueryWrapper<FansPostPub> qw = new QueryWrapper<FansPostPub>();
        qw.eq("is_publish","changeFace");
        List<FansPostPub> list=this.fansPostPubMapper.selectList(qw);
        if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                FansPostPub pub=list.get(i);
                //获取头像
                QueryWrapper<FansCtrlAccount> qw1 = new QueryWrapper<FansCtrlAccount>();
                qw1.eq("username",pub.getDomainAccount());
                qw1.eq("fans_domain",pub.getDomain());
                FansCtrlAccount fansCtrlAccount=this.fansCtrlAccountMapper.selectOne(qw1);
                String profile=fansCtrlAccount.getProfile();
                String imgPaths=pub.getImgPath();

                if(profile!=null && imgPaths!=null){
                    String[] imgPathArray= StringUtils.split(imgPaths,",");
                    if(imgPathArray!=null && imgPathArray.length>0){
                        //改成绝对地址
                        String profilePath=uploadAccess+profile;
                        boolean isChangeOk=true;
                        for (int j = 0; j < imgPathArray.length; j++) {
                            String imgPath=uploadAccess+imgPathArray[j];
                            //调用换脸接口
                            JSONObject postData=new JSONObject();
                            postData.put("inputImage",profilePath);
                            postData.put("inputImage2",imgPath);
                       //     System.out.println("inputImage:"+profilePath);
                       //     System.out.println("inputImage2:"+imgPath);
                            try{
                                JSONObject retdata=Tools.post(url,postData);
                           //     System.out.println(retdata.toJSONString());
                                if(retdata!=null && retdata.getJSONObject("data")!=null  && retdata.getJSONObject("data").getJSONArray("outputImages")!=null){
                                    JSONArray imgs=retdata.getJSONObject("data").getJSONArray("outputImages");
                                    if(imgs.size()>0){
                                        String dealimgurl= imgs.getString(0);
                                        imgPathArray[j]=dealimgurl;//写入回来
                              //          System.out.println("dealimgurl:"+dealimgurl);
                                    }
                                }else{
                                    isChangeOk=false;//有一个没换成就是失败
                                    Thread.sleep(1000*60);
                                }
                           /*
                            {
                                "code": 200,
                                    "message": "操作成功",
                                    "data": {
                                "outputImages": [
                                "http://52.76.246.31:39003/files/uploadsBz/20240323/62fc6388f0e646d1bc0590be82bb6271.png"
        ],
                                "id": 665,
                                        "status": "success"
                            }
                            */
                            }catch (Exception e){
                                e.printStackTrace();

                            }
                        }
                        //将数组拼接回来
                        String newImgPath=this.fansPostService.dealImgPathLocal(String.join(",", imgPathArray));
                        UpdateWrapper<FansPostPub> uw=new  UpdateWrapper<FansPostPub>();
                       // System.out.println("newImgPath:"+newImgPath);
                        uw.eq("id",pub.getId());
                        uw.set("img_Path",newImgPath);

                        if(isChangeOk){
                            uw.set("is_publish","none");//恢复成待发布状态
                        }
                        this.fansPostPubMapper.update(null,uw);
                    }
                }
            }
        }
    }
    @Scheduled(cron = "0 0/1 * * * ?")
    public void run() {
        GoogleCredentials credentials=null;
        try{
            System.out.println("TimerM5 !  时间:" + DateUtils.getTimestamp());
            changeFace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            credentials=null;
        }
    }
}
