package org.jeecg.modules.fans.content.controller;

import java.util.Arrays;
import java.util.Date;
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
import org.jeecg.modules.fans.content.entity.FansContentImg;
import org.jeecg.modules.fans.content.entity.FansPost;
import org.jeecg.modules.fans.content.entity.FansRoleTemple;
import org.jeecg.modules.fans.content.service.IFansAigcFeatureService;
import org.jeecg.modules.fans.content.service.IFansPostPubService;
import org.jeecg.modules.fans.content.service.IFansPostService;

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
 * @Description: 帖子库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Api(tags="帖子库")
@RestController
@RequestMapping("/content/fansPost")
@Slf4j
public class FansPostController extends JeecgController<FansPost, IFansPostService> {
	@Autowired
	private IFansPostService fansPostService;

	 @Autowired
	 private IFansPostPubService fansPostPubService;
	 @Autowired
	 private IFansAigcFeatureService fansAigcFeatureService;
	/**
	 * 分页列表查询
	 *
	 * @param fansPost
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "帖子库-分页列表查询")
	@ApiOperation(value="帖子库-分页列表查询", notes="帖子库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansPost>> queryPageList(FansPost fansPost,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansPost> queryWrapper = QueryGenerator.initQueryWrapper(fansPost, req.getParameterMap());

		if(fansPost.getFeatures()!=null) {
			String[] tags=fansPost.getFeatures().split(",");
			if(tags!=null){
				queryWrapper.lambda().or(wrapper ->{
					for(String str : tags){
						wrapper.or().like(FansPost::getFeatures, str);
					}
				});
			}
		}
		queryWrapper.orderByDesc("genarate_time");
		Page<FansPost> page = new Page<FansPost>(pageNo, pageSize);
		IPage<FansPost> pageList = fansPostService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansPost
	 * @return
	 */
	@AutoLog(value = "帖子库-添加")
	@ApiOperation(value="帖子库-添加", notes="帖子库-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansPost fansPost) {
		fansPost.setGenarateTime(new Date());
		fansPost.setImgPath(this.fansPostService.dealImgPathLocal(fansPost.getImgPath()));
		//fansPost.setIsFinish("1");
		fansPostService.save(fansPost);
		fansAigcFeatureService.saveNewTagsCustom(fansPost.getFeatures(),"贴子");

		//this.fansPostPubService.toFansPostPub(fansPost.getId());
		return Result.OK("帖子成功录入！");
	}

	 @ApiOperation(value="帖子-通过id查询", notes="帖子-通过id查询")
	 @GetMapping(value = "/queryPostById")
	 public Result<FansPost> queryPostById(@RequestParam(name="id",required=true) String id) {
		 FansPost fansPost = this.fansPostService.getById(id);
		 if(fansPost==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(fansPost);
	 }

	/**
	 *  编辑
	 *
	 * @param fansPost
	 * @return
	 */
	@AutoLog(value = "帖子库-编辑")
	@ApiOperation(value="帖子库-编辑", notes="帖子库-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansPost fansPost) {
		fansPost.setImgPath(this.fansPostService.dealImgPathLocal(fansPost.getImgPath()));
		fansPostService.updateById(fansPost);
		fansAigcFeatureService.saveNewTagsCustom(fansPost.getFeatures(),"贴子");
		return Result.OK("帖子修改成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "帖子库-通过id删除")
	@ApiOperation(value="帖子库-通过id删除", notes="帖子库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansPostService.removeById(id);
		return Result.OK("删除成功!");
	}
	 @AutoLog(value = "帖子库-完成修改")
	 @ApiOperation(value="帖子库-完成修改", notes="帖子库-完成修改")
	 @PostMapping(value = "/finishOne")
	 public Result<String> finishOne(@RequestParam(name="id",required=true) String id) {
		 //待发布的帖子、同人设的账号，同步到发布表
		 this.fansPostPubService.toFansPostPub(id);
		 FansPost fansPost=new FansPost();
		 fansPost.setId(id);
		 fansPost.setIsFinish("1");
		 fansPostService.updateById(fansPost);

		 return Result.OK("帖子已生效!");
	 }
	 @AutoLog(value = "帖子库-重新修改")
	 @ApiOperation(value="帖子库-重新修改", notes="帖子库-重新修改")
	 @PostMapping(value = "/unFinishOne")
	 public Result<String> unFinishOne(@RequestParam(name="id",required=true) String id) {
		 FansPost fansPost=new FansPost();
		 fansPost.setId(id);
		 fansPost.setIsFinish("0");
		 fansPostService.updateById(fansPost);

		 return Result.OK("确认重新调整操作成功!");
	 }
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "帖子库-批量删除")
	@ApiOperation(value="帖子库-批量删除", notes="帖子库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansPostService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "帖子库-通过id查询")
	@ApiOperation(value="帖子库-通过id查询", notes="帖子库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansPost> queryById(@RequestParam(name="id",required=true) String id) {
		FansPost fansPost = fansPostService.getById(id);
		if(fansPost==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansPost);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansPost
    */
    @RequiresPermissions("content:fans_post:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansPost fansPost) {
        return super.exportXls(request, fansPost, FansPost.class, "帖子库");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("content:fans_post:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansPost.class);
    }

}
