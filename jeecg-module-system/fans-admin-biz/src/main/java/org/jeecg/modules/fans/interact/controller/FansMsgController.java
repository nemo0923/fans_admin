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
import org.jeecg.modules.fans.interact.entity.FansMsg;
import org.jeecg.modules.fans.interact.service.IFansMsgService;

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
 * @Description: 私信列表
 * @Author: jeecg-boot
 * @Date:   2024-02-11
 * @Version: V1.0
 */
@Api(tags="私信列表")
@RestController
@RequestMapping("/interact/fansMsg")
@Slf4j
public class FansMsgController extends JeecgController<FansMsg, IFansMsgService> {
	@Autowired
	private IFansMsgService fansMsgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param fansMsg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "私信列表-分页列表查询")
	@ApiOperation(value="私信列表-分页列表查询", notes="私信列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansMsg>> queryPageList(FansMsg fansMsg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansMsg> queryWrapper = QueryGenerator.initQueryWrapper(fansMsg, req.getParameterMap());
		Page<FansMsg> page = new Page<FansMsg>(pageNo, pageSize);
		IPage<FansMsg> pageList = fansMsgService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 @ApiOperation(value="私信列表-所有列表查询", notes="私信列表-所有列表查询")
	 @GetMapping(value = "/listall")
	 public Result<List<FansMsg>> listall(FansMsg fansMsg,
												 HttpServletRequest req) {
		 QueryWrapper<FansMsg> queryWrapper = QueryGenerator.initQueryWrapper(fansMsg, req.getParameterMap());
		 queryWrapper.orderByAsc("msg_time");
		 return Result.OK(fansMsgService.list(queryWrapper));
	 }
	/**
	 *   添加
	 *
	 * @param fansMsg
	 * @return
	 */
	@AutoLog(value = "私信列表-添加")
	@ApiOperation(value="私信列表-添加", notes="私信列表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansMsg fansMsg) {
		fansMsg.setMsgTime(new Date());
		fansMsgService.save(fansMsg);
		return Result.OK("私信发送成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansMsg
	 * @return
	 */
	@AutoLog(value = "私信列表-编辑")
	@ApiOperation(value="私信列表-编辑", notes="私信列表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansMsg fansMsg) {
		fansMsgService.updateById(fansMsg);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "私信列表-通过id删除")
	@ApiOperation(value="私信列表-通过id删除", notes="私信列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansMsgService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "私信列表-批量删除")
	@ApiOperation(value="私信列表-批量删除", notes="私信列表-批量删除")
	@RequiresPermissions("interact:fans_msg:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansMsgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "私信列表-通过id查询")
	@ApiOperation(value="私信列表-通过id查询", notes="私信列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansMsg> queryById(@RequestParam(name="id",required=true) String id) {
		FansMsg fansMsg = fansMsgService.getById(id);
		if(fansMsg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansMsg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansMsg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansMsg fansMsg) {
        return super.exportXls(request, fansMsg, FansMsg.class, "私信列表");
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
        return super.importExcel(request, response, FansMsg.class);
    }

}
