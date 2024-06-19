package org.jeecg.modules.fans.interact.controller;

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
import org.jeecg.modules.fans.interact.entity.FansList;
import org.jeecg.modules.fans.interact.service.IFansListService;

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
 * @Description: 粉丝列表
 * @Author: jeecg-boot
 * @Date:   2024-02-11
 * @Version: V1.0
 */
@Api(tags="粉丝列表")
@RestController
@RequestMapping("/interact/fansList")
@Slf4j
public class FansListController extends JeecgController<FansList, IFansListService> {
	@Autowired
	private IFansListService fansListService;
	 @Autowired
	 private SheetLaunch sheetLaunch;
	/**
	 * 分页列表查询
	 *
	 * @param fansList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "粉丝列表-分页列表查询")
	@ApiOperation(value="粉丝列表-分页列表查询", notes="粉丝列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansList>> queryPageList(FansList fansList,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansList> queryWrapper = QueryGenerator.initQueryWrapper(fansList, req.getParameterMap());
		queryWrapper.orderByDesc("join_time");
		Page<FansList> page = new Page<FansList>(pageNo, pageSize);
		IPage<FansList> pageList = fansListService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansList
	 * @return
	 */
	@AutoLog(value = "粉丝列表-添加")
	@ApiOperation(value="粉丝列表-添加", notes="粉丝列表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansList fansList) {
		fansListService.save(fansList);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansList
	 * @return
	 */
	@AutoLog(value = "粉丝列表-编辑")
	@ApiOperation(value="粉丝列表-编辑", notes="粉丝列表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansList fansList) {
		fansListService.updateById(fansList);
		return Result.OK("编辑成功!");
	}

	 @AutoLog(value = "粉丝列表-加好友请求")
	 @ApiOperation(value="粉丝列表-加好友请求", notes="粉丝列表-加好友请求")
	 @RequestMapping(value = "/addFriend", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> addFriend(@RequestBody FansList fansList) {
		 if(this.sheetLaunch.addFriend(fansList.getUsername(),fansList.getFansNickname())){
			 FansList fansListNew =new FansList();
			 fansListNew.setId(fansList.getId());
			 fansListNew.setFansStatus("toFriend");
			 fansListService.updateById(fansListNew);
			 return Result.OK("加好友请求发送成功!");
		 }else{
			 return Result.error("加好友请求发送失败，请检查google sheet通信情况!");
		 }
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "粉丝列表-通过id删除")
	@ApiOperation(value="粉丝列表-通过id删除", notes="粉丝列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansListService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "粉丝列表-批量删除")
	@ApiOperation(value="粉丝列表-批量删除", notes="粉丝列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansListService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "粉丝列表-通过id查询")
	@ApiOperation(value="粉丝列表-通过id查询", notes="粉丝列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansList> queryById(@RequestParam(name="id",required=true) String id) {
		FansList fansList = fansListService.getById(id);
		if(fansList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansList
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansList fansList) {
        return super.exportXls(request, fansList, FansList.class, "粉丝列表");
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
        return super.importExcel(request, response, FansList.class);
    }

}
