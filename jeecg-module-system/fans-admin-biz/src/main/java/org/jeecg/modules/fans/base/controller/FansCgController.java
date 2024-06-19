package org.jeecg.modules.fans.base.controller;

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
import org.jeecg.modules.fans.base.entity.FansCg;
import org.jeecg.modules.fans.base.service.IFansCgService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 客群库
 * @Author: jeecg-boot
 * @Date:   2024-05-19
 * @Version: V1.0
 */
@Api(tags="客群库")
@RestController
@RequestMapping("/base/fansCg")
@Slf4j
public class FansCgController extends JeecgController<FansCg, IFansCgService> {
	@Autowired
	private IFansCgService fansCgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param fansCg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "客群库-分页列表查询")
	@ApiOperation(value="客群库-分页列表查询", notes="客群库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansCg>> queryPageList(FansCg fansCg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansCg> queryWrapper = QueryGenerator.initQueryWrapper(fansCg, req.getParameterMap());
		Page<FansCg> page = new Page<FansCg>(pageNo, pageSize);
		IPage<FansCg> pageList = fansCgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansCg
	 * @return
	 */
	@AutoLog(value = "客群库-添加")
	@ApiOperation(value="客群库-添加", notes="客群库-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansCg fansCg) {
		fansCgService.save(fansCg);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansCg
	 * @return
	 */
	@AutoLog(value = "客群库-编辑")
	@ApiOperation(value="客群库-编辑", notes="客群库-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansCg fansCg) {
		fansCgService.updateById(fansCg);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客群库-通过id删除")
	@ApiOperation(value="客群库-通过id删除", notes="客群库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansCgService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客群库-批量删除")
	@ApiOperation(value="客群库-批量删除", notes="客群库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansCgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "客群库-通过id查询")
	@ApiOperation(value="客群库-通过id查询", notes="客群库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansCg> queryById(@RequestParam(name="id",required=true) String id) {
		FansCg fansCg = fansCgService.getById(id);
		if(fansCg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansCg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansCg
    */
    @RequiresPermissions("base:fans_cg:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansCg fansCg) {
        return super.exportXls(request, fansCg, FansCg.class, "客群库");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("base:fans_cg:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansCg.class);
    }

}
