package org.jeecg.modules.fans.content.controller;

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
import org.jeecg.modules.fans.content.entity.FansAigcFeature;
import org.jeecg.modules.fans.content.service.IFansAigcFeatureService;

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
 * @Description: AIGC主题库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Api(tags="AIGC主题库")
@RestController
@RequestMapping("/content/fansAigcFeature")
@Slf4j
public class FansAigcFeatureController extends JeecgController<FansAigcFeature, IFansAigcFeatureService> {
	@Autowired
	private IFansAigcFeatureService fansAigcFeatureService;
	
	/**
	 * 分页列表查询
	 *
	 * @param fansAigcFeature
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "AIGC主题库-分页列表查询")
	@ApiOperation(value="AIGC主题库-分页列表查询", notes="AIGC主题库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansAigcFeature>> queryPageList(FansAigcFeature fansAigcFeature,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansAigcFeature> queryWrapper = QueryGenerator.initQueryWrapper(fansAigcFeature, req.getParameterMap());
		Page<FansAigcFeature> page = new Page<FansAigcFeature>(pageNo, pageSize);
		IPage<FansAigcFeature> pageList = fansAigcFeatureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansAigcFeature
	 * @return
	 */
	@AutoLog(value = "AIGC主题库-添加")
	@ApiOperation(value="AIGC主题库-添加", notes="AIGC主题库-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansAigcFeature fansAigcFeature) {
		fansAigcFeatureService.save(fansAigcFeature);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansAigcFeature
	 * @return
	 */
	@AutoLog(value = "AIGC主题库-编辑")
	@ApiOperation(value="AIGC主题库-编辑", notes="AIGC主题库-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansAigcFeature fansAigcFeature) {
		fansAigcFeatureService.updateById(fansAigcFeature);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "AIGC主题库-通过id删除")
	@ApiOperation(value="AIGC主题库-通过id删除", notes="AIGC主题库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansAigcFeatureService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "AIGC主题库-批量删除")
	@ApiOperation(value="AIGC主题库-批量删除", notes="AIGC主题库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansAigcFeatureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "AIGC主题库-通过id查询")
	@ApiOperation(value="AIGC主题库-通过id查询", notes="AIGC主题库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansAigcFeature> queryById(@RequestParam(name="id",required=true) String id) {
		FansAigcFeature fansAigcFeature = fansAigcFeatureService.getById(id);
		if(fansAigcFeature==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansAigcFeature);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansAigcFeature
    */
    @RequiresPermissions("content:fans_aigc_feature:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansAigcFeature fansAigcFeature) {
        return super.exportXls(request, fansAigcFeature, FansAigcFeature.class, "AIGC主题库");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("content:fans_aigc_feature:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansAigcFeature.class);
    }

}
