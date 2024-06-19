package org.jeecg.modules.fans.content.service;

import org.jeecg.modules.fans.content.entity.FansPostPub;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 帖子发布
 * @Author: jeecg-boot
 * @Date:   2024-02-17
 * @Version: V1.0
 */
public interface IFansPostPubService extends IService<FansPostPub> {
    //处理待发布帖子
    void toFansPostPub(String id);
}
