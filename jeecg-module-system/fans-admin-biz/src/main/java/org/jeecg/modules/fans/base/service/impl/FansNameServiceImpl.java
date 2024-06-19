package org.jeecg.modules.fans.base.service.impl;

import org.jeecg.modules.fans.base.entity.FansName;
import org.jeecg.modules.fans.base.mapper.FansNameMapper;
import org.jeecg.modules.fans.base.service.IFansNameService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 姓名库
 * @Author: jeecg-boot
 * @Date:   2024-05-19
 * @Version: V1.0
 */
@Service
public class FansNameServiceImpl extends ServiceImpl<FansNameMapper, FansName> implements IFansNameService {

}
