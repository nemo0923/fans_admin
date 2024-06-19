package org.jeecg.modules.fans.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.modules.fans.content.entity.FansPostPub;
import org.jeecg.modules.fans.content.mapper.FansPostPubMapper;
import org.jeecg.modules.fans.content.service.IFansPostPubService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 帖子发布
 * @Author: jeecg-boot
 * @Date:   2024-02-17
 * @Version: V1.0
 */
@Service
public class FansPostPubServiceImpl extends ServiceImpl<FansPostPubMapper, FansPostPub> implements IFansPostPubService {
    public void toFansPostPub(String id){

        List<FansPostPub> list =this.baseMapper.selectToFansPostPub(id);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                //查询 同账号 同帖子 是否存在
                FansPostPub pub=list.get(i);
                //准备换脸
                if(pub.getImgPath()!=null && !pub.getImgPath().equals("") && pub.getIsFace()!=null && pub.getIsFace()==1){
                    pub.setIsPublish("changeFace");
                }else{
                    pub.setIsPublish("none");
                }
                QueryWrapper<FansPostPub> qw=new QueryWrapper<FansPostPub>();
                qw.eq("post_id",pub.getPostId());
                qw.eq("domain_account",pub.getDomainAccount());
                long count=this.baseMapper.selectCount(qw);
                if(count==0){
                    //不存在则插入
                    this.baseMapper.insert(pub);
                }else{
                    //如果存在更新
                    UpdateWrapper<FansPostPub> uw=new  UpdateWrapper<FansPostPub>();
                    uw.eq("post_id",pub.getPostId());
                    uw.eq("domain_account",pub.getDomainAccount());
                    uw.set("post_title",pub.getPostTitle());
                    uw.set("domain",pub.getDomain());
                    uw.set("role_id",pub.getRoleId());
                    uw.set("content",pub.getContent());
                    uw.set("features",pub.getFeatures());
                    uw.set("img_Path",pub.getImgPath());
                    uw.set("profile",pub.getProfile());
                    uw.set("name",pub.getName());
                    uw.set("country",pub.getCountry());
                    uw.set("link",pub.getLink());
                   uw.set("is_publish",pub.getIsPublish());
                    this.baseMapper.update(null,uw);
                }


                //this.baseMapper.replaceToFansPostPub(list.get(i));
            }
        }


    }
}
