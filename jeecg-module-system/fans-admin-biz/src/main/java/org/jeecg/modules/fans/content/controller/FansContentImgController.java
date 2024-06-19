package org.jeecg.modules.fans.content.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.util.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.fans.content.ManuaUploadVo;
import org.jeecg.modules.fans.content.entity.FansAigcFeature;
import org.jeecg.modules.fans.content.entity.FansContentImg;
import org.jeecg.modules.fans.content.service.IFansAigcFeatureService;
import org.jeecg.modules.fans.content.service.IFansContentImgService;

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
 * @Description: 图片库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Api(tags="图片库")
@RestController
@RequestMapping("/content/fansContentImg")
@Slf4j
public class FansContentImgController extends JeecgController<FansContentImg, IFansContentImgService> {
	@Autowired
	private IFansContentImgService fansContentImgService;
	 @Autowired
	 private IFansAigcFeatureService fansAigcFeatureService;

	/**
	 * 分页列表查询
	 *
	 * @param fansContentImg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "图片库-分页列表查询")
	@ApiOperation(value="图片库-分页列表查询", notes="图片库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansContentImg>> queryPageList(FansContentImg fansContentImg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansContentImg> queryWrapper = QueryGenerator.initQueryWrapper(fansContentImg, req.getParameterMap());



		if(fansContentImg.getFeatures()!=null) {
			String[] tags=fansContentImg.getFeatures().split(",");
			if(tags!=null){
				queryWrapper.lambda().or(wrapper ->{
					for(String str : tags){
						wrapper.or().like(FansContentImg::getFeatures, str);
					}
				});
			}
		}
		queryWrapper.orderByDesc("genarate_time");

		Page<FansContentImg> page = new Page<FansContentImg>(pageNo, pageSize);
		IPage<FansContentImg> pageList = fansContentImgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 @GetMapping(value = "/listAll")
	 public Result<List<FansContentImg>> listAll(FansContentImg fansContentImg,
														HttpServletRequest req) {
		 QueryWrapper<FansContentImg> queryWrapper = QueryGenerator.initQueryWrapper(fansContentImg, req.getParameterMap());


		 if(fansContentImg.getFeatures()!=null) {
			 String[] tags=fansContentImg.getFeatures().split(",");
			 if(tags!=null){
				 queryWrapper.lambda().or(wrapper ->{
					 for(String str : tags){
						 wrapper.or().like(FansContentImg::getFeatures, str);
					 }
				 });
			 }
		 }
		 queryWrapper.orderByDesc("genarate_time");
		 return Result.OK(fansContentImgService.list(queryWrapper));
	 }
	/**
	 *   添加
	 *
	 * @param fansContentImg
	 * @return
	 */
	@AutoLog(value = "图片库-添加")
	@ApiOperation(value="图片库-添加", notes="图片库-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansContentImg fansContentImg) {
		fansContentImg.setGenarateTime(new Date());
		fansContentImgService.save(fansContentImg);
		fansAigcFeatureService.saveNewTagsCustom(fansContentImg.getFeatures(),"图片");
		return Result.OK("已成功录入！");
	}

	 @AutoLog(value = "图片库-手动添加")
	 @ApiOperation(value="图片库-手动添加", notes="图片库-手动添加")
	 @PostMapping(value = "/manuaUploadSave")
	 public Result<String> manuaUploadSave(@RequestBody ManuaUploadVo manuaUploadVo) {
		String[] pathArray= StringUtils.split(manuaUploadVo.getPaths(),',');
		if(pathArray!=null && pathArray.length>0){
			for (int i = 0; i < pathArray.length; i++) {
				FansContentImg fansContentImg=new FansContentImg();
				fansContentImg.setGenarateTime(new Date());
				fansContentImg.setFeatures(manuaUploadVo.getFeatures());
				fansContentImg.setGenarateWay("manual");
				fansContentImg.setImgPath(pathArray[i]);
				fansContentImgService.save(fansContentImg);
			}
		}
		 fansAigcFeatureService.saveNewTagsCustom(manuaUploadVo.getFeatures(),"图片");

		 return Result.OK("已成功录入！");
	 }
	
	/**
	 *  编辑
	 *
	 * @param fansContentImg
	 * @return
	 */
	@AutoLog(value = "图片库-编辑")
	@ApiOperation(value="图片库-编辑", notes="图片库-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansContentImg fansContentImg) {
		fansContentImgService.updateById(fansContentImg);
		fansAigcFeatureService.saveNewTagsCustom(fansContentImg.getFeatures(),"图片");
		return Result.OK("人工修改成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "图片库-通过id删除")
	@ApiOperation(value="图片库-通过id删除", notes="图片库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansContentImgService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "图片库-批量删除")
	@ApiOperation(value="图片库-批量删除", notes="图片库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansContentImgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "图片库-通过id查询")
	@ApiOperation(value="图片库-通过id查询", notes="图片库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansContentImg> queryById(@RequestParam(name="id",required=true) String id) {
		FansContentImg fansContentImg = fansContentImgService.getById(id);
		if(fansContentImg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansContentImg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansContentImg
    */
    @RequiresPermissions("content:fans_content_img:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansContentImg fansContentImg) {
        return super.exportXls(request, fansContentImg, FansContentImg.class, "图片库");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("content:fans_content_img:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansContentImg.class);
    }

}
