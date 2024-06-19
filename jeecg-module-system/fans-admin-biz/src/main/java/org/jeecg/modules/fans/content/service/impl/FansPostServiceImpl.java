package org.jeecg.modules.fans.content.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.fans.Tools;
import org.jeecg.modules.fans.content.entity.FansPost;
import org.jeecg.modules.fans.content.mapper.FansPostMapper;
import org.jeecg.modules.fans.content.service.IFansPostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 帖子库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Service
public class FansPostServiceImpl extends ServiceImpl<FansPostMapper, FansPost> implements IFansPostService {
    //将FansPost对象的imgpath字段中的外部图片下载到本地，并且改成相对地址

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    public String dealImgPathLocal(String imgPath){
        String newImgPath="";

            String[] paths= StringUtils.split(imgPath,",");
            if(paths!=null && paths.length>0){
                for (int i = 0; i < paths.length; i++) {
                    if(paths[i].startsWith("http")){
                        //外部图片需下载
                        String[] strs= StringUtils.split(paths[i],"/");
                        if(strs!=null && strs.length>0){
                            String savepath =  "/dealimg/" + strs[strs.length-1];
                            String savepathWhole =  uploadpath+savepath;
                            try {
                                Tools.FileDownloader(paths[i], savepathWhole);
                                newImgPath=newImgPath+','+savepath;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        newImgPath=newImgPath+','+paths[i];
                    }
                }

            }
            if(newImgPath.equals("")){
                newImgPath=imgPath;
            }else{
                if(newImgPath.startsWith(",") && newImgPath.length()>1){
                    newImgPath=newImgPath.substring(1);
                }
            }


        return newImgPath;
    }
}
