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
import org.jeecg.modules.fans.base.entity.FansName;
import org.jeecg.modules.fans.base.service.IFansNameService;

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
 * @Description: 姓名库
 * @Author: jeecg-boot
 * @Date:   2024-05-19
 * @Version: V1.0
 */
@Api(tags="姓名库")
@RestController
@RequestMapping("/base/fansName")
@Slf4j
public class FansNameController extends JeecgController<FansName, IFansNameService> {
	@Autowired
	private IFansNameService fansNameService;
	
	/**
	 * 分页列表查询
	 *
	 * @param fansName
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "姓名库-分页列表查询")
	@ApiOperation(value="姓名库-分页列表查询", notes="姓名库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansName>> queryPageList(FansName fansName,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansName> queryWrapper = QueryGenerator.initQueryWrapper(fansName, req.getParameterMap());
		Page<FansName> page = new Page<FansName>(pageNo, pageSize);
		IPage<FansName> pageList = fansNameService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansName
	 * @return
	 */
	@AutoLog(value = "姓名库-添加")
	@ApiOperation(value="姓名库-添加", notes="姓名库-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansName fansName) {
		fansNameService.save(fansName);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansName
	 * @return
	 */
	@AutoLog(value = "姓名库-编辑")
	@ApiOperation(value="姓名库-编辑", notes="姓名库-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansName fansName) {
		fansNameService.updateById(fansName);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "姓名库-通过id删除")
	@ApiOperation(value="姓名库-通过id删除", notes="姓名库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansNameService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "姓名库-批量删除")
	@ApiOperation(value="姓名库-批量删除", notes="姓名库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansNameService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "姓名库-通过id查询")
	@ApiOperation(value="姓名库-通过id查询", notes="姓名库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansName> queryById(@RequestParam(name="id",required=true) String id) {
		FansName fansName = fansNameService.getById(id);
		if(fansName==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansName);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansName
    */
    @RequiresPermissions("base:fans_name:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansName fansName) {
        return super.exportXls(request, fansName, FansName.class, "姓名库");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("base:fans_name:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansName.class);
    }

}
