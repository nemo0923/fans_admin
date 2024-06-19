package org.jeecg.modules.fans.market.service;

import org.jeecg.modules.fans.market.entity.FansSearch;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.fans.rpa.vo.SearchFansVo;

/**
 * @Description: 搜粉计划
 * @Author: jeecg-boot
 * @Date:   2024-04-05
 * @Version: V1.0
 */
public interface IFansSearchService extends IService<FansSearch> {
    boolean addFbNewSearchValue(FansSearch fansSearch);
}
