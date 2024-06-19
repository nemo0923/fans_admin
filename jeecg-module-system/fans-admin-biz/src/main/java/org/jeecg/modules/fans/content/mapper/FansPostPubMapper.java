package org.jeecg.modules.fans.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.fans.content.entity.FansPostPub;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 帖子发布
 * @Author: jeecg-boot
 * @Date:   2024-02-17
 * @Version: V1.0
 */
public interface FansPostPubMapper extends BaseMapper<FansPostPub> {
    //查询待发布的帖子
    List<FansPostPub> selectToFansPostPub(String id);
    //查询已发布帖子的账号
    List<String> selectHasFansPostPub(String domain);
    //保存（替换）待发布的帖子
    void replaceToFansPostPub(FansPostPub fansPostPub);
}
