package org.jeecg.modules.fans.market.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.fans.interact.entity.FansCtrlAccount;
import org.jeecg.modules.fans.interact.mapper.FansCtrlAccountMapper;
import org.jeecg.modules.fans.market.entity.FansSearch;
import org.jeecg.modules.fans.market.mapper.FansSearchMapper;
import org.jeecg.modules.fans.market.service.IFansSearchService;
import org.jeecg.modules.fans.rpa.SheetLaunch;
import org.jeecg.modules.fans.rpa.vo.SearchFansVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 搜粉计划
 * @Author: jeecg-boot
 * @Date:   2024-04-05
 * @Version: V1.0
 */
@Service
public class FansSearchServiceImpl extends ServiceImpl<FansSearchMapper, FansSearch> implements IFansSearchService {
    @Autowired
    private SheetLaunch sheetLaunch;
    @Autowired
    private FansCtrlAccountMapper fansCtrlAccountMapper;

    public boolean addFbNewSearchValue(FansSearch fansSearch){
        SearchFansVo vo=new SearchFansVo();

        QueryWrapper<FansCtrlAccount> qw=new QueryWrapper<FansCtrlAccount>();
        qw.eq("cg",fansSearch.getCg());
        qw.eq("online_status","ok");
        qw.eq("account_type","small");
        qw.orderByDesc("activetime");
        qw.last("limit 0,1");
        FansCtrlAccount fansCtrlAccount=this.fansCtrlAccountMapper.selectOne(qw);
        if(fansCtrlAccount==null){
            return false;
        }
        String username=fansCtrlAccount.getUsername();//随机找个同客群小号
        fansSearch.setUsername(username);
        vo.setSearchUrl(fansSearch.getUrl());
        vo.setBeginNum(0);
        vo.setBeginPage(1);
        vo.setEndPage(fansSearch.getPlanUserNum()>20?(fansSearch.getPlanUserNum()/20):1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            vo.setTime0(dateFormat.format(new Date()));
        }catch(Exception e){

        }

        return sheetLaunch.addFbNewSearchValue(username,vo);
    }
}
