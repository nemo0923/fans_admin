package org.jeecg.modules.fans.rpa;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.oauth2.GoogleCredentials;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.fans.Tools;
import org.jeecg.modules.fans.base.entity.FansProxy;
import org.jeecg.modules.fans.content.entity.FansPostPub;
import org.jeecg.modules.fans.interact.entity.FansCtrlAccount;
import org.jeecg.modules.fans.market.entity.FansLib;
import org.jeecg.modules.fans.rpa.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

//facebook与googe sheet交互操作
@Component
public class Fb2Sheet {
    @Value(value = "${google.sheet.fbPost}")
    private String fbPost;//fb发帖
    @Value(value = "${google.sheet.fbPostData}")
    private String fbPostData;//fb发帖数据
    @Value(value = "${google.sheet.fbActive}")
    private String fbActive;//fb养号
    @Value(value = "${google.sheet.fbAddFriend}")
    private String fbAddFriend;//fb加好友
    @Value(value = "${google.sheet.fbFriendData}")
    private String fbFriendData;//fb好友名单数据
    @Value(value = "${google.sheet.fbSendMsg}")
    private String fbSendMsg;//fb发私信
    @Value(value = "${google.sheet.fbMsgData}")
    private String fbMsgData;//fb采集私信数据
    @Value(value = "${google.sheet.fbMyInfo}")
    private String fbMyInfo;//fb更新个人信息
    @Value(value = "${google.sheet.fbCrawList}")
    private String fbCrawList;//小号采集帖子数据名单

    @Value(value = "${google.sheet.fbSearchFans}")
    private String fbSearchFans;//小号搜粉指令
    @Value(value = "${google.sheet.fbSearchFansRet}")
    private String fbSearchFansRet;//小号搜粉结果
    @Value(value = "${google.sheet.fbSearchFansTmp}")
    private String fbSearchFansTmp;//小号搜粉临时表

    @Value(value = "${google.sheet.accountSheet}")
    private String accountSheet;//账号信息表

    @Value(value = "${jeecg.path.uploadAccess}")
    private String uploadAccess;//图片访问地址

    @Autowired
    private SheetOper sheetOper;

    //增加账号表新记录
    public boolean mountAccountToSheet(GoogleCredentials credentials, FansCtrlAccount account, FansProxy fansProxy){
        try{
            List<Object> values=new ArrayList<Object>();
            values.add(account.getAgentId());
            values.add(account.getFansDomain());
            values.add(account.getUsername());
            values.add(account.getPassword());
            values.add(account.getFakey());
            values.add(account.getCg());
            values.add(account.getAutoFriend());

            values.add(fansProxy.getProxySoft());
            values.add(fansProxy.getProxyName());
            values.add(fansProxy.getProxyType());
            values.add(fansProxy.getProxyHost());
            values.add(fansProxy.getProxyPort());
            values.add(fansProxy.getProxyUser());
            values.add(fansProxy.getProxyPassword());
            values.add("");
            values.add("");
            values.add("");
            values.add("");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            values.add(sdf.format(new Date()));
            sheetOper.appendValues(credentials,"fbdata",accountSheet,"account",values);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //检查账号表更新记录
    public void mountAccountResp(GoogleCredentials credentials, FansCtrlAccount account){
        try{
            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",accountSheet,"account");
            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();
                for(int i=1;i<list2.size();i++){
                    List<Object> list1=list2.get(i);
            //        System.out.println("list1.size():"+list1.size());
                    if(list1.size()>=17){
                        String username=(String)(list1.get(2));
            //            System.out.println("username:"+username);
                        if(username!=null && !username.equals(account.getUsername())){
                            continue;
                        }

                        //如果有浏览器账号信息，说明ads上号完成。如果有主页链接，说明更新完成。
                        String browserNo=(String)(list1.get(14));
                        String link=(String)(list1.get(15));
          //              System.out.println("browserNo:"+browserNo);
                        if(browserNo!=null && !browserNo.equals("") && account.getMountState().equals("un_login0")){
                            //之前状态：已同步到sheet，未挂到ads上
                            //当前状态，已挂到ads上
                            account.setMountState("un_ads1");
                            account.setBrowserNo(browserNo);
                        }else if(link!=null && !link.equals("")  && account.getMountState().equals("un_2fa")){
                            //之前状态：已挂到ads上
                            //当前状态，平台2fa验证成功，有主页链接
                            String rpa_state=(String)(list1.get(17));
                            if(rpa_state!=null && rpa_state.equals("ok")){
                                account.setMountState("ok");
                                account.setOnlineStatus("ok");
                            }
                            account.setLink(link);
                        }else if(browserNo!=null && !browserNo.equals("") && (link==null || link.equals(""))){
                            //无link 有browserNo  说明2fa失败
                            String rpa_state=(String)(list1.get(17));
                             if(rpa_state!=null && rpa_state.equals("err")){
                                account.setOnlineStatus("error");
                                account.setMountState("fail");
                            }
                        }
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();

        }
    }
    //更新账号表为ready状态
    public void mountAccountReady(GoogleCredentials credentials, FansCtrlAccount account) throws Exception{
            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",accountSheet,"account");
            String range=null;
            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();
                for(int i=1;i<list2.size();i++){
                    List<Object> list1=list2.get(i);
                    if(list1.size()>=17){
                        String browserNo=(String)(list1.get(14));
                        if(browserNo!=null && browserNo.equals(account.getBrowserNo())){
                            //更新rpa_state列为ready
                            int i1=i+1;
                            range="account!R"+i1+":R"+i1;
                        }
                    }
                }
            }
            List<Object> values1=new ArrayList<Object>();
            values1.add("ready");
            if(range!=null){
              //  System.out.println("range:"+range);
                sheetOper.updateValues(credentials,"fbdata",accountSheet,range,values1);
            }

    }
    //创建新账号,每个表都增加一个账号名同名sheet
    public SheetId createAccount(GoogleCredentials credentials, String username){
        try{
            SheetId sheetId=new SheetId();
            //发帖
            sheetOper.addSheet(credentials,"fbdata",fbPost,username);
            List<Object> values1=new ArrayList<Object>();
            values1.add("text");
            values1.add("postId");
            values1.add("time0");
            values1.add("imgs");
            values1.add("time");
            values1.add("url");
            values1.add("imgsArray");
            sheetOper.appendValues(credentials,"fbdata",fbPost,username,values1);

            //帖子运营数据
            Integer sheetIdFbPostData=sheetOper.addSheet(credentials,"fbdata",fbPostData,username);
            sheetId.setSheetIdFbPostData(sheetIdFbPostData);
            List<Object> values2=new ArrayList<Object>();
            values2.add("帖子url");
            values2.add("采集时间");
            values2.add("点赞数");
            values2.add("评论数");
            values2.add("分享数");
            sheetOper.appendValues(credentials,"fbdata",fbPostData,username,values2);

            //养号
            sheetOper.addSheet(credentials,"fbdata",fbActive,username);
            List<Object> values3=new ArrayList<Object>();
            values3.add("登录时间");
            sheetOper.appendValues(credentials,"fbdata",fbActive,username,values3);


            //同步个人信息
            sheetOper.addSheet(credentials,"fbdata",fbMyInfo,username);
            List<Object> values7=new ArrayList<Object>();
            values7.add("type");
            values7.add("info");
            values7.add("time0");
            values7.add("time");
            sheetOper.appendValues(credentials,"fbdata",fbMyInfo,username,values7);

            //获取好友名单
            Integer sheetIdFbFriendData=sheetOper.addSheet(credentials,"fbdata",fbFriendData,username);
            sheetId.setSheetIdFbFriendData(sheetIdFbFriendData);
            List<Object> values5=new ArrayList<Object>();
            values5.add("好友名称");
            values5.add("好友个人主页地址");
            values5.add("好友头像url");
            values5.add("采集时间");
            sheetOper.appendValues(credentials,"fbdata",fbFriendData,username,values5);

            //发送加好友请求
            sheetOper.addSheet(credentials,"fbdata",fbAddFriend,username);
            List<Object> values4=new ArrayList<Object>();
            values4.add("link");
            values4.add("time0");
            values4.add("time");
            sheetOper.appendValues(credentials,"fbdata",fbAddFriend,username,values4);

            //发私信
            sheetOper.addSheet(credentials,"fbdata",fbSendMsg,username);
            List<Object> values6=new ArrayList<Object>();
            values6.add("发送者名称");
            values6.add("私信内容");
            values6.add("私信发送时间");
            sheetOper.appendValues(credentials,"fbdata",fbSendMsg,username,values6);


            //获取私信结果
            Integer sheetIdFbMsgData=sheetOper.addSheet(credentials,"fbdata",fbMsgData,username);
            List<Object> values8=new ArrayList<Object>();
            values8.add("发送者名称");
            values8.add("私信内容");
            values8.add("采集时间");
            sheetOper.appendValues(credentials,"fbdata",fbMsgData,username,values8);
            sheetId.setSheetIdFbMsgData(sheetIdFbMsgData);

            return sheetId;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //小号创建搜粉相关sheet
    public void createSearchSheets(GoogleCredentials credentials, String username){

        try{
            //搜索指令
            sheetOper.addSheet(credentials,"fbdata",fbSearchFans,username);
            List<Object> values1=new ArrayList<Object>();
            values1.add("searchUrl");
            values1.add("beginNum");
            values1.add("beginPage");
            values1.add("endPage");
            values1.add("time0");
            values1.add("time");
            sheetOper.appendValues(credentials,"fbdata",fbSearchFans,username,values1);

            //搜索结果
            sheetOper.addSheet(credentials,"fbdata",fbSearchFansRet,username);
            List<Object> values2=new ArrayList<Object>();
            values2.add("searchUrl");
            values2.add("link");
            values2.add("profile");
            values2.add("name");
            values2.add("fNum");
            values2.add("fromLink");
            values2.add("info");
            values2.add("time");
            sheetOper.appendValues(credentials,"fbdata",fbSearchFansRet,username,values2);

            //搜索临时表
            sheetOper.addSheet(credentials,"fbdata",fbSearchFansTmp,username);
            List<Object> values3=new ArrayList<Object>();
            values3.add("link");
            values3.add("profile");
            values3.add("name");
            values3.add("searchUrl");
            values3.add("fromLink");
            values3.add("time");
            sheetOper.appendValues(credentials,"fbdata",fbSearchFansTmp,username,values3);
        }catch (Exception e){
            e.printStackTrace();;
        }

    }
    //小号新增搜粉记录
    public boolean addFbNewSearchValue(GoogleCredentials credentials, String username, SearchFansVo vo){
        try{
            List<Object> values=new ArrayList<Object>();
            values.add(vo.getSearchUrl());
            values.add(vo.getBeginNum());
            values.add(vo.getBeginPage());
            values.add(vo.getEndPage());
            values.add(vo.getTime0());
            sheetOper.appendValues(credentials,"fbdata",fbSearchFans,username,values);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //读取搜粉结果
    public List<FansLib> crawSearchFansRetData(GoogleCredentials credentials, String username){
        List<FansLib> fansLibList =new ArrayList<FansLib>();
        Date now=new Date();
        try{
            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",fbSearchFansRet,username);
            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();
                //倒序来查，总是查最新的
                for(int i=list2.size()-1;i>0;i--){
                    List<Object> list1=list2.get(i);
                    if(list1.size()>=8){
                        String searchUrl=(String)(list1.get(0));
                        String link=(String)(list1.get(1));
                        String profile=(String)(list1.get(2));
                        String name=(String)(list1.get(3));
                        String fNum=(String)(list1.get(4));
                        String fromLink=(String)(list1.get(5));
                        String info=(String)(list1.get(6));
                        String time=(String)(list1.get(7));
                      //  System.out.println("time:"+time);
                        if(time==null || time.equals("")){
                            continue;
                        }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = dateFormat.parse(time);
                            //超时2天不采集
                      //  System.out.println("now:"+now);
                      //  System.out.println("date:"+date);
                      //  System.out.println("Tools.subDay(now,date):"+Tools.subDay(now,date));
                            if(Tools.subDay(now,date)<=3){
                                FansLib fansLib=new FansLib();
                                fansLib.setFansNum(fNum);
                                fansLib.setSearchUrl(searchUrl);
                                fansLib.setLink(link);
                                fansLib.setProfile(profile);
                                fansLib.setName(name);
                                fansLib.setFromLink(fromLink);
                                fansLib.setCrawtime(new Date());
                                fansLib.setInfo(info);
                                fansLibList.add(fansLib);
                            }
                    }else{
                        System.out.println("list1.size():"+list1.size());
                    }
                }
            }else{
                System.out.println("valueRange null");
            }
            return  fansLibList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //创建爬虫sheet并写入值
    public int createSheetIdFbCraw(GoogleCredentials credentials, String username,List<CrawVo> list){
            try{
                int id=sheetOper.addSheet(credentials,"fbdata",fbCrawList,username);
                List<Object> values1=new ArrayList<Object>();
                values1.add("url");
                values1.add("username");
                List<List<Object>> values=new ArrayList<List<Object>>();
                values.add(values1);
                for (int i = 0; i < list.size(); i++) {
                    List<Object> valuesTmp=new ArrayList<Object>();
                    valuesTmp.add(list.get(i).getLink());
                    valuesTmp.add(list.get(i).getUsername());
                    values.add(valuesTmp);
                }
                sheetOper.batchAppendValues(credentials,"fbdata",fbCrawList,username,values);
                return id;
            }catch (Exception e){
                e.printStackTrace();
            }
           return 0;
    }
    //删除爬虫sheet
    public boolean deleteSheetIdFbCraw(GoogleCredentials credentials, String username,int sheetid){
        try {
            sheetOper.deleteSheet(credentials, "fbdata", fbCrawList, sheetid);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //同步个人信息 分钟级别定时器处理反馈
    public boolean syncInfo(GoogleCredentials credentials,String type,String into, String username){
        try{
            List<Object> values=new ArrayList<Object>();
            values.add(type);
            values.add(into);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = dateFormat.format(new Date());
            values.add(date);
            sheetOper.appendValues(credentials,"fbdata",fbMyInfo,username,values);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //确认信息同步结果
    public boolean checkSyncInfo(GoogleCredentials credentials, String type, String username){
        try{
            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",fbMyInfo,username);
            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();
                Date now=new Date();
                Date newDate0=null;
                Date newDate1=null;
                for(int i=1;i<list2.size();i++){
                    List<Object> list1=list2.get(i);
                    if(list1.size()>=4){
                        String type0=(String)(list1.get(0));
                        if(!type0.equals(type)){
                            continue;
                        }
                        String time0=(String)(list1.get(2));
                     //   System.out.println("time0:"+time0);
                        String time=(String)(list1.get(3));
                        if(time0!=null && !time0.equals("") && time!=null && !time.equals("")){
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date0 = dateFormat.parse(time0);
                            Date date = dateFormat.parse(time);
                  //          System.out.println("i:"+i);
                    //        System.out.println("date0:"+date0);
                      //      System.out.println("newDate0:"+newDate0);
                            //找最新的时间

                                if(newDate0==null){
                                    newDate0=date0;
                                }
                                if(newDate1==null){
                                    newDate1=date;
                                }
                                if(newDate0.before(date0)){
                                    newDate0=date0;
                                }
                                if(newDate1.before(date)){
                                    newDate1=date;
                                }
                        }
                    }
                }
                if(newDate0!=null && newDate1!=null){
                    //最新同步信息反馈完成时间必须大于最新开始时间，否则容易把之前完成结果算在内
              //      System.out.println("now:"+now);
                    Long days=Tools.subDay(newDate1,newDate0);

             //       System.out.println("days:"+days);
                    if(days>=0){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
            return  false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //读取养号结果 日级别定时器
    public Date checkActive(GoogleCredentials credentials, String username, Date syncActiveDateTime){
        try{

            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",fbActive,username);

            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();
                Date now=new Date();
                Date newDate=null;
                for(int i=1;i<list2.size();i++){
                    List<Object> list1=list2.get(i);
                    if(list1.size()>=1){
                        String str0=(String)(list1.get(0));
                        if(str0!=null && !str0.equals("")){
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = dateFormat.parse(str0);
                            //找最新的时间
                                if(newDate==null){
                                    newDate=date;
                                }
                                if(newDate.before(date)){
                                    newDate=date;
                                }
                        }
                    }


                }
                if(newDate!=null){
                    //与当前时间比较，在两天之内才有效
                //    System.out.println("now:"+now);
              //      System.out.println("newDate:"+newDate);
                    Long days=Tools.subDay(now,newDate);

              //      System.out.println("username:"+username);
               //     System.out.println("days:"+days);
                    if(days<=3){
                        return newDate;
                    }else{
                        return null;
                    }
                }
            }
            return  null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //发新帖
    public boolean sendPost(GoogleCredentials credentials, String content,String imgs,String username,String postId){
        try{
            List<Object> values=new ArrayList<Object>();
            values.add(content);
            values.add(postId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            values.add(sdf.format(new Date()));
            String newStr="";
            if(imgs!=null && !imgs.equals("")){
                String[] imgsArr= StringUtils.split(imgs,",");
                if(imgsArr!=null && imgsArr.length>0){
                    for (int i = 0; i < imgsArr.length; i++) {
                        if(i==0){
                            newStr=uploadAccess+imgsArr[i];
                        }else{
                            newStr=newStr+","+uploadAccess+imgsArr[i];
                        }

                    }
                    values.add(newStr);
                }

            }


            sheetOper.appendValues(credentials,"fbdata",fbPost,username,values);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //确认帖子发布结果
    public String checkSendPostStatus(GoogleCredentials credentials,String postId,String username)  {
        try{
            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",fbPost,username);
            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for(int i=1;i<list2.size();i++){
                    List<Object> list1=list2.get(i);
                    if(list1.size()>=6){
                        String strPostId=(String)(list1.get(1));
                        String strUrl=(String)(list1.get(5));
                        if(strPostId!=null && postId.equals(strPostId)){
                            if(strUrl!=null && !strUrl.equals("")){
                                return strUrl;
                            }else{
                                //无帖子url
                                String time0=(String)(list1.get(2));//开始发送时间
                                //超过1小时无响应，视为失败
                                Date datetime0=formatter.parse(time0);
                                if(new Date().getTime()-datetime0.getTime()>1000*60*60){
                                    return null;
                                }
                            }
                        }

                    }

                }
            }

            return  "";
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    //读取帖子运营数据  日级别定时器
    public List<PostDataVo> crawPostData(GoogleCredentials credentials, String username){
        List<PostDataVo> postDataVos =new ArrayList<PostDataVo>();
        try{
            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",fbPostData,username);
            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();

                Set<String> urlSet=new HashSet<String>();
                for(int i=list2.size()-1;i>0;i--){
                    List<Object> list1=list2.get(i);
                    if(list1.size()>=5){
                        String strUrl=(String)(list1.get(0));
                        if(urlSet.contains(strUrl)){
                            continue;
                        }else{
                            urlSet.add(strUrl);
                        }
                        String strTime=(String)(list1.get(1));
                        String str2=(String)(list1.get(2));
                        String str3=(String)(list1.get(3));
                        String str4=(String)(list1.get(4));
                        if(strTime!=null && !strTime.equals("")){
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = dateFormat.parse(strTime);
                                PostDataVo postDataVo=new PostDataVo();
                                postDataVo.setPostUrl(strUrl);
                                postDataVo.setSyncPostDataDateTime(date);

                                int goodNum=0;
                                //if (str2!=null && str2.length()>0 && !Character.isDigit(str2.charAt(0))) {
                                 //   str2=str2.substring(1);
                                //}
                                try{
                                    if(str2!=null){
                                        int pos=str2.length();
                                        for (int j = 0; j < str2.length(); j++) {
                                            if (!Character.isDigit(str2.charAt(j))) {
                                                pos=j;
                                                break;
                                            }
                                        }
                                        goodNum=Integer.parseInt(str2.substring(0,pos));
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                postDataVo.setGoodNum(goodNum);

                              //  if (str3!=null && str3.length()>0 && !Character.isDigit(str3.charAt(0))) {
                             //       str3=str3.substring(1);
                             //   }
                                int commentNum=0;
                                if(str3!=null){
                                    int pos=str3.length();
                                for (int k = 0; k < str3.length(); k++) {
                                        if (!Character.isDigit(str3.charAt(k))) {
                                            pos=k;
                                            break;
                                        }
                                    }
                                    commentNum=Integer.parseInt(str3.substring(0,pos));
                                }
                                postDataVo.setCommentNum(commentNum);

                           //     if(str4!=null && str4.length()>0 && !Character.isDigit(str4.charAt(0))) {
                           //         str4=str4.substring(1);
                           //     }
                                int shareNum=0;
                                int pos=str4.length();
                                if(str4!=null){
                                    for (int kk = 0; kk < str4.length(); kk++) {
                                        if (!Character.isDigit(str4.charAt(kk))) {
                                            pos=kk;
                                            break;
                                        }
                                    }
                                    shareNum=Integer.parseInt(str4.substring(0,pos));
                                }
                                postDataVo.setShareNum(shareNum);
                                postDataVos.add(postDataVo);
                        }
                    }
                }
            }
            return  postDataVos;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //读取好友名单数据
    public List<FansVo> crawFansData(GoogleCredentials credentials, String username){
        List<FansVo> fansVos =new ArrayList<FansVo>();
        Date now=new Date();
        try{
            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",fbFriendData,username);
            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();
                //倒序来查，总是查最新的，如果存在则不覆盖
                Set<String> urlSet=new HashSet<String>();
                for(int i=list2.size()-1;i>0;i--){
                    List<Object> list1=list2.get(i);
                    if(list1.size()>=4){
                        String strName=(String)(list1.get(0));
                        String strUrl=(String)(list1.get(1));
                        if(urlSet.contains(strUrl)){
                            continue;
                        }else{
                            urlSet.add(strUrl);
                        }
                        String strProfile=(String)(list1.get(2));
                        String strTime=(String)(list1.get(3));

                        if(strTime!=null && !strTime.equals("")){
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = dateFormat.parse(strTime);
                    //        System.out.println("Tools.subDay(now,date):"+Tools.subDay(now,date));
                            if(Tools.subDay(now,date)<=2){
                                //只查最近两天的
                                FansVo fansVo=new FansVo();
                                fansVo.setFansLink(strUrl);
                                fansVo.setFansNickname(strName);
                                fansVo.setFansProfile(strProfile);
                                fansVo.setSyncFansDataDateTime(date);
                                fansVos.add(fansVo);
                                //    System.out.println("strName:"+strName);
                            }

                        }
                    }
                }
            }
            return  fansVos;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //加好友请求
    public boolean addFriend(GoogleCredentials credentials, String username,String link){
        try{
            List<Object> values=new ArrayList<Object>();
            values.add(link);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            values.add(sdf.format(new Date()));
            sheetOper.appendValues(credentials,"fbdata",fbAddFriend,username,values);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //确认好友请求发出结果
    public String checkAddFriendStatus(GoogleCredentials credentials,String username,String friendName)  {
        try{
            ValueRange valueRange=sheetOper.getValues(credentials,"fbdata",fbAddFriend,username);
            if(valueRange!=null){
                List<List<Object>> list2=valueRange.getValues();
                for(int i=1;i<list2.size();i++){
                    List<Object> list1=list2.get(i);
                    if(list1.size()>=3){
                        String strFriendName=(String)(list1.get(0));
                        String strTime=(String)(list1.get(1));
                        String strStatus=(String)(list1.get(2));
                        if(strFriendName!=null && strFriendName.equals(friendName) && strStatus!=null && !strStatus.equals("")){
                            return strStatus;
                        }
                    }

                }
            }
            return  null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //发私信  分钟级别定时器处理反馈
    //确认私信发出结果


    //读取私信数据  15分钟定时器


    //清空指定账号好友名单数据
    //清空指定账号帖子运营数据 日级别定时器
    //清空指定账号私信数据




}
