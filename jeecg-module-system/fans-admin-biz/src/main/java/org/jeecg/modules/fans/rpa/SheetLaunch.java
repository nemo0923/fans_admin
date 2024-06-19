package org.jeecg.modules.fans.rpa;

import com.google.auth.oauth2.GoogleCredentials;
import org.jeecg.modules.fans.content.entity.FansPost;
import org.jeecg.modules.fans.interact.entity.FansCtrlAccount;
import org.jeecg.modules.fans.rpa.vo.SearchFansVo;
import org.jeecg.modules.fans.rpa.vo.SheetId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SheetLaunch {
    @Autowired
    private Fb2Sheet fb2Sheet;
    @Autowired
    private SheetOper sheetOper;

    public boolean mountAccountReady(FansCtrlAccount account){
        try{
            GoogleCredentials credentials=sheetOper.genarateGoogleCredentials();
             fb2Sheet.mountAccountReady(credentials,account);
             return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;

        }
    }

    //创建账号，无需ads。只需sheet
    public SheetId createAccount(String username){
        try{
            GoogleCredentials credentials=sheetOper.genarateGoogleCredentials();
            return fb2Sheet.createAccount(credentials,username);
        }catch (Exception e){
            return null;
        }
    }
    //小号创建搜粉相关sheet
    public void createSearchSheets(String username){
        try{
            GoogleCredentials credentials=sheetOper.genarateGoogleCredentials();
             fb2Sheet.createSearchSheets(credentials,username);
        }catch (Exception e){

        }
    }
    //小号新增搜粉记录
    public boolean addFbNewSearchValue(String username, SearchFansVo vo){
        try{
            GoogleCredentials credentials=sheetOper.genarateGoogleCredentials();
            return fb2Sheet.addFbNewSearchValue(credentials,username,vo);
        }catch (Exception e){
            return false;
        }
    }
    //发新帖
    public boolean sendPost( String content,String imgs,String username,String postId){
        try{
            GoogleCredentials credentials=sheetOper.genarateGoogleCredentials();
            return fb2Sheet.sendPost( credentials, content,imgs, username, postId);
        }catch (Exception e){
            return false;
        }
    }
    //加好友请求
    public boolean addFriend( String username,String friendName){
        try{
            GoogleCredentials credentials=sheetOper.genarateGoogleCredentials();
            return fb2Sheet.addFriend( credentials, username, friendName);
        }catch (Exception e){
            return false;
        }
    }
    //发私信

    //同步个人信息
    public boolean syncInfo( FansCtrlAccount fansCtrlAccount){
        //作废
        return false;
    }
}
