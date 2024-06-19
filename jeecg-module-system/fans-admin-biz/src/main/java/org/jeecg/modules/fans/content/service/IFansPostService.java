package org.jeecg.modules.fans.content.service;

import org.jeecg.modules.fans.content.entity.FansPost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 帖子库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
public interface IFansPostService extends IService<FansPost> {
    //将FansPost对象的imgpath字段中的外部图片下载到本地，并且改成相对地址
    String dealImgPathLocal(String imgPath);
}
