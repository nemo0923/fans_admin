package org.jeecg.modules.fans.interact.controller;

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
import org.jeecg.modules.fans.interact.entity.FansCtrlNode;
import org.jeecg.modules.fans.interact.service.IFansCtrlNodeService;

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
 * @Description: 云控设备
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Api(tags="云控设备")
@RestController
@RequestMapping("/interact/fansCtrlNode")
@Slf4j
public class FansCtrlNodeController extends JeecgController<FansCtrlNode, IFansCtrlNodeService> {
	 @Autowired
	 HttpServletRequest request;
	@Autowired
	private IFansCtrlNodeService fansCtrlNodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param fansCtrlNode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "云控设备-分页列表查询")
	@ApiOperation(value="云控设备-分页列表查询", notes="云控设备-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansCtrlNode>> queryPageList(FansCtrlNode fansCtrlNode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansCtrlNode> queryWrapper = QueryGenerator.initQueryWrapper(fansCtrlNode, req.getParameterMap());
		Page<FansCtrlNode> page = new Page<FansCtrlNode>(pageNo, pageSize);
		IPage<FansCtrlNode> pageList = fansCtrlNodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansCtrlNode
	 * @return
	 */
	@AutoLog(value = "云控设备-添加")
	@ApiOperation(value="云控设备-添加", notes="云控设备-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansCtrlNode fansCtrlNode) {
		fansCtrlNodeService.save(fansCtrlNode);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansCtrlNode
	 * @return
	 */
	@AutoLog(value = "云控设备-编辑")
	@ApiOperation(value="云控设备-编辑", notes="云控设备-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansCtrlNode fansCtrlNode) {
		fansCtrlNodeService.updateById(fansCtrlNode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "云控设备-通过id删除")
	@ApiOperation(value="云控设备-通过id删除", notes="云控设备-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansCtrlNodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "云控设备-批量删除")
	@ApiOperation(value="云控设备-批量删除", notes="云控设备-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansCtrlNodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}


	 @ApiOperation(value="代理心跳", notes="代理心跳")
	 @GetMapping(value = "/agentHeartBeat")
	 public Result<String> agentHeartBeat(@RequestParam(name="count",required=true) String count,@RequestParam(name="agentId",required=true) String agentId) {
		 FansCtrlNode fansCtrlNode = fansCtrlNodeService.getById(agentId);
		 if(fansCtrlNode==null ) {
			 //新注册
			 fansCtrlNode=new FansCtrlNode();
			 fansCtrlNode.setId(agentId);
			 fansCtrlNode.setRegtime(new Date());
			 fansCtrlNode.setActivetime(new Date());
			 fansCtrlNode.setIp(request.getRemoteAddr());
			 fansCtrlNode.setOnlineNum(count);
			 fansCtrlNode.setCtrlStatus("run");
			 this.fansCtrlNodeService.save(fansCtrlNode);
		 }else{
			 fansCtrlNode.setActivetime(new Date());
			 fansCtrlNode.setIp(request.getRemoteAddr());
			 fansCtrlNode.setOnlineNum(count);
			 fansCtrlNode.setCtrlStatus("run");
			 this.fansCtrlNodeService.updateById(fansCtrlNode);
		 }
		 return Result.OK("ok");
	 }
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "云控设备-通过id查询")
	@ApiOperation(value="云控设备-通过id查询", notes="云控设备-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansCtrlNode> queryById(@RequestParam(name="id",required=true) String id) {
		FansCtrlNode fansCtrlNode = fansCtrlNodeService.getById(id);
		if(fansCtrlNode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansCtrlNode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansCtrlNode
    */
    @RequiresPermissions("interact:fans_ctrl_node:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansCtrlNode fansCtrlNode) {
        return super.exportXls(request, fansCtrlNode, FansCtrlNode.class, "云控设备");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("interact:fans_ctrl_node:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansCtrlNode.class);
    }

}
