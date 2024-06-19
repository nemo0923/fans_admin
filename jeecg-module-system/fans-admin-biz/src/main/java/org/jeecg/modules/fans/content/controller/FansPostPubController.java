package org.jeecg.modules.fans.content.controller;

import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.fans.content.entity.FansPost;
import org.jeecg.modules.fans.content.entity.FansPostPub;
import org.jeecg.modules.fans.content.service.IFansPostPubService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.fans.content.service.IFansPostService;
import org.jeecg.modules.fans.rpa.SheetLaunch;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 帖子发布
 * @Author: jeecg-boot
 * @Date:   2024-02-17
 * @Version: V1.0
 */
@Api(tags="帖子发布")
@RestController
@RequestMapping("/content/fansPostPub")
@Slf4j
public class FansPostPubController extends JeecgController<FansPostPub, IFansPostPubService> {
	@Autowired
	private IFansPostPubService fansPostPubService;
	 @Autowired
	 private IFansPostService fansPostService;

	 @Autowired
	 private SheetLaunch launchAction;

	
	/**
	 * 分页列表查询
	 *
	 * @param fansPostPub
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "帖子发布-分页列表查询")
	@ApiOperation(value="帖子发布-分页列表查询", notes="帖子发布-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansPostPub>> queryPageList(FansPostPub fansPostPub,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansPostPub> queryWrapper = QueryGenerator.initQueryWrapper(fansPostPub, req.getParameterMap());
		Page<FansPostPub> page = new Page<FansPostPub>(pageNo, pageSize);
		IPage<FansPostPub> pageList = fansPostPubService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansPostPub
	 * @return
	 */
	@AutoLog(value = "帖子发布-添加")
	@ApiOperation(value="帖子发布-添加", notes="帖子发布-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansPostPub fansPostPub) {
		fansPostPubService.save(fansPostPub);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansPostPub
	 * @return
	 */
	@AutoLog(value = "帖子发布-编辑")
	@ApiOperation(value="帖子发布-编辑", notes="帖子发布-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansPostPub fansPostPub) {
		fansPostPubService.updateById(fansPostPub);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "帖子发布-通过id删除")
	@ApiOperation(value="帖子发布-通过id删除", notes="帖子发布-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansPostPubService.removeById(id);
		return Result.OK("删除成功!");
	}
	 @AutoLog(value = "帖子发布")
	 @ApiOperation(value="帖子发布", notes="帖子发布")
	 @PostMapping(value = "/publishOne")
	 public Result<String> publishOne(@RequestParam(name="id",required=true) Long id) {
		 //发布帖子
		 //查询发布对象
		 //找到贴子
		 FansPostPub fansPostPub=this.fansPostPubService.getById(id);
		 if(fansPostPub!=null){
				 boolean ret=launchAction.sendPost(fansPostPub.getContent(),fansPostPub.getImgPath(),fansPostPub.getDomainAccount(),fansPostPub.getPostId());
			 	if(ret){
					fansPostPub.setIsPublish("start");
					this.fansPostPubService.updateById(fansPostPub);
					return Result.ok("正在发布中，请稍后查看发布结果。");
				}else{
					fansPostPub.setIsPublish("error");
					this.fansPostPubService.updateById(fansPostPub);
					return Result.error("发布失败，google sheet通信异常!");
				}

		 }
		 return Result.error("发布失败!");
	 }

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "帖子发布-批量删除")
	@ApiOperation(value="帖子发布-批量删除", notes="帖子发布-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansPostPubService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "帖子发布-通过id查询")
	@ApiOperation(value="帖子发布-通过id查询", notes="帖子发布-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansPostPub> queryById(@RequestParam(name="id",required=true) String id) {
		FansPostPub fansPostPub = fansPostPubService.getById(id);
		if(fansPostPub==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansPostPub);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansPostPub
    */
    @RequiresPermissions("content:fans_post_pub:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansPostPub fansPostPub) {
        return super.exportXls(request, fansPostPub, FansPostPub.class, "帖子发布");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("content:fans_post_pub:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansPostPub.class);
    }

}
