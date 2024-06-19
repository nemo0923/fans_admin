package org.jeecg.modules.fans.market.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.fans.market.entity.FansSearch;
import org.jeecg.modules.fans.market.service.IFansSearchService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.fans.rpa.SheetLaunch;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 搜粉计划
 * @Author: jeecg-boot
 * @Date:   2024-04-05
 * @Version: V1.0
 */
@Api(tags="搜粉计划")
@RestController
@RequestMapping("/market/fansSearch")
@Slf4j
public class FansSearchController extends JeecgController<FansSearch, IFansSearchService> {
	@Autowired
	private IFansSearchService fansSearchService;

	/**
	 * 分页列表查询
	 *
	 * @param fansSearch
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "搜粉计划-分页列表查询")
	@ApiOperation(value="搜粉计划-分页列表查询", notes="搜粉计划-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansSearch>> queryPageList(FansSearch fansSearch,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansSearch> queryWrapper = QueryGenerator.initQueryWrapper(fansSearch, req.getParameterMap());
		Page<FansSearch> page = new Page<FansSearch>(pageNo, pageSize);
		IPage<FansSearch> pageList = fansSearchService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansSearch
	 * @return
	 */
	@AutoLog(value = "搜粉计划-添加")
	@ApiOperation(value="搜粉计划-添加", notes="搜粉计划-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansSearch fansSearch) {
		if(this.fansSearchService.addFbNewSearchValue(fansSearch)){
			fansSearchService.save(fansSearch);
			return Result.OK("新增搜粉计划成功！");
		}else{
			return Result.error("新增搜粉计划失败！请检查小号状态是否正常！");
		}

	}
	
	/**
	 *  编辑
	 *
	 * @param fansSearch
	 * @return
	 */
	@AutoLog(value = "搜粉计划-编辑")
	@ApiOperation(value="搜粉计划-编辑", notes="搜粉计划-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansSearch fansSearch) {
		fansSearchService.updateById(fansSearch);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "搜粉计划-通过id删除")
	@ApiOperation(value="搜粉计划-通过id删除", notes="搜粉计划-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansSearchService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "搜粉计划-批量删除")
	@ApiOperation(value="搜粉计划-批量删除", notes="搜粉计划-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansSearchService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "搜粉计划-通过id查询")
	@ApiOperation(value="搜粉计划-通过id查询", notes="搜粉计划-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansSearch> queryById(@RequestParam(name="id",required=true) String id) {
		FansSearch fansSearch = fansSearchService.getById(id);
		if(fansSearch==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansSearch);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansSearch
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansSearch fansSearch) {
        return super.exportXls(request, fansSearch, FansSearch.class, "搜粉计划");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansSearch.class);
    }

}
