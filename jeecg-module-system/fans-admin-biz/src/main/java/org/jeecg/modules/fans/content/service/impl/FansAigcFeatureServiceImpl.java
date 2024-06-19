package org.jeecg.modules.fans.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.fans.content.entity.FansAigcFeature;
import org.jeecg.modules.fans.content.mapper.FansAigcFeatureMapper;
import org.jeecg.modules.fans.content.service.IFansAigcFeatureService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: AIGC主题库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Service
public class FansAigcFeatureServiceImpl extends ServiceImpl<FansAigcFeatureMapper, FansAigcFeature> implements IFansAigcFeatureService {
 public void saveNewTagsCustom(String tagJosnArray,String scene){
     JSONArray jsonArray = JSON.parseArray(tagJosnArray);
     if(jsonArray!=null){
         for(int i=0;i<jsonArray.size();i++){
             JSONObject object=(JSONObject)jsonArray.get(i);
             FansAigcFeature fansAigcFeature=new FansAigcFeature();
             fansAigcFeature.setScene(scene);
             fansAigcFeature.setIsHot(0);
             fansAigcFeature.setFeatureName(object.getString("featureName"));
             fansAigcFeature.setTagName(object.getString("tagName"));

             QueryWrapper<FansAigcFeature> queryWrapper=new QueryWrapper<FansAigcFeature>();
             queryWrapper.eq("feature_name",fansAigcFeature.getFeatureName());
             queryWrapper.eq("tag_name",fansAigcFeature.getTagName());
             FansAigcFeature fansAigcFeatureNew=this.baseMapper.selectOne(queryWrapper);
             if(fansAigcFeatureNew==null){
                 this.save(fansAigcFeature);
             }
         }
     }
 }
}
