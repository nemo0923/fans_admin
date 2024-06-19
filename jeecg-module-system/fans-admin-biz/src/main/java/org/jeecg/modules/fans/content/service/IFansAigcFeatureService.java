package org.jeecg.modules.fans.content.service;

import org.jeecg.modules.fans.content.entity.FansAigcFeature;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: AIGC主题库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
public interface IFansAigcFeatureService extends IService<FansAigcFeature> {
    //自定义保存标签——from图片库、帖子库等业务页面
    void saveNewTagsCustom(String tagJosnArray,String scene);
}
