package org.jeecg.modules.fans.interact.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.fans.content.service.IFansPostService;
import org.jeecg.modules.fans.interact.entity.FansCtrlAccount;
import org.jeecg.modules.fans.interact.service.IFansCtrlAccountService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.fans.interact.vo.AccontBatchVO;
import org.jeecg.modules.fans.rpa.Fb2Sheet;
import org.jeecg.modules.fans.rpa.SheetLaunch;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.fans.rpa.vo.SheetId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 平台账号
 * @Author: jeecg-boot
 * @Date:   2024-02-11
 * @Version: V1.0
 */
@Api(tags="平台账号")
@RestController
@RequestMapping("/interact/fansCtrlAccount")
@Slf4j
public class FansCtrlAccountController extends JeecgController<FansCtrlAccount, IFansCtrlAccountService> {
	@Autowired
	private IFansCtrlAccountService fansCtrlAccountService;
	 @Autowired
	 private SheetLaunch sheetLaunch;
	@Autowired
	private IFansPostService fansPostService;
	/**
	 * 分页列表查询
	 *
	 * @param fansCtrlAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "平台账号-分页列表查询")
	@ApiOperation(value="平台账号-分页列表查询", notes="平台账号-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansCtrlAccount>> queryPageList(FansCtrlAccount fansCtrlAccount,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansCtrlAccount> queryWrapper = QueryGenerator.initQueryWrapper(fansCtrlAccount, req.getParameterMap());
		Page<FansCtrlAccount> page = new Page<FansCtrlAccount>(pageNo, pageSize);
		IPage<FansCtrlAccount> pageList = fansCtrlAccountService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	@ApiOperation(value="平台账号-获取最近同步的浏览器账号", notes="平台账号-获取最近同步的浏览器账号")
	@GetMapping(value = "/querySyncBrowserNoList")
	public List<String> querySyncBrowserNoList(@RequestParam(name="agentId", defaultValue="0") String agentId,
													   HttpServletRequest req) {
		QueryWrapper<FansCtrlAccount> qw =new QueryWrapper<FansCtrlAccount>();
		qw.eq("agent_id",agentId);
		qw.apply("profile_info_state='doing' or cover_info_state='doing'");

		List<FansCtrlAccount> list=this.fansCtrlAccountService.list(qw);
		List<String> ret=new ArrayList<String>();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				ret.add(list.get(i).getBrowserNo());
			}
		}
		return ret;
	}
	//
	@AutoLog(value = "平台账号-批量导入")
	@ApiOperation(value="平台账号-批量导入", notes="平台账号-批量导入")
	@PostMapping(value = "/batchSave")
	public Result<String> batchSave(@RequestBody AccontBatchVO accontBatchVO) {
		//txt格式验证是否正确
		String txt=accontBatchVO.getTxt();
		String[] txts= StringUtils.split(txt,"\n");
		if(txts!=null && txts.length>0){
			for (int i = 0; i < txts.length; i++) {
				String[] cols= StringUtils.split(txts[i],"----");
				if(cols!=null && cols.length==3){
					String username=cols[0];
					String password=cols[1];
					String fakey=cols[2];
					//逐条账号录入
					FansCtrlAccount fansCtrlAccount=new FansCtrlAccount();
					fansCtrlAccount.setFansDomain(accontBatchVO.getFansDomain());
					fansCtrlAccount.setRoleTempleId(accontBatchVO.getRoleTempleId());
					fansCtrlAccount.setProxyId(accontBatchVO.getProxyId());
					fansCtrlAccount.setCountry(accontBatchVO.getCountry());
					fansCtrlAccount.setUsername(username);
					fansCtrlAccount.setPassword(password);
					String newFakey="";
					String[] arr=StringUtils.split(fakey," ");
					if(arr==null || arr.length==0){
						newFakey=fakey;
					}else{
						for (int j = 0; j < arr.length; j++) {
							newFakey=newFakey+arr[j];
						}
					}
					fansCtrlAccount.setFakey(newFakey);
					fansCtrlAccount.setLocalSrc(accontBatchVO.getLocalSrc());
					fansCtrlAccount.setCg(accontBatchVO.getCg());
					fansCtrlAccount.setAutoFriend(accontBatchVO.getAutoFriend());
					fansCtrlAccount.setAutoName(accontBatchVO.getAutoName());
					fansCtrlAccount.setAutoPost(accontBatchVO.getAutoPost());
					fansCtrlAccount.setAutoProfile(accontBatchVO.getAutoProfile());

					this.fansCtrlAccountService.save(fansCtrlAccount);
				}else{
					int i1=i+1;
					return Result.error("号源文本格式错误！请检查第"+i1+"行！");
				}
			}

		}
		return Result.OK("批量导入账号成功！");
	}
	/**
	 *   添加
	 *
	 * @param fansCtrlAccount
	 * @return
	 */
	@AutoLog(value = "平台账号-添加")
	@ApiOperation(value="平台账号-添加", notes="平台账号-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansCtrlAccount fansCtrlAccount) {
		fansCtrlAccount.setSyncStatusInfo(null);
		fansCtrlAccount.setProfile(this.fansPostService.dealImgPathLocal(fansCtrlAccount.getProfile()));
		SheetId sheetId=sheetLaunch.createAccount(fansCtrlAccount.getBrowserNo());
		if(fansCtrlAccount.getAccountType().equals("small")){
			//小号，需要建搜粉sheet
			this.sheetLaunch.createSearchSheets(fansCtrlAccount.getUsername());
		}
		if(sheetId==null){
			return Result.error("养号信息登记失败，请检查账户是否重复！");
		}else{
			fansCtrlAccount.setSheetIdFbPostData(sheetId.getSheetIdFbPostData());
			fansCtrlAccount.setSheetIdFbFriendData(sheetId.getSheetIdFbFriendData());
			fansCtrlAccount.setSheetIdFbMsgData(sheetId.getSheetIdFbMsgData());
			fansCtrlAccountService.save(fansCtrlAccount);
		}
		return Result.OK("养号信息登记成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansCtrlAccount
	 * @return
	 */
	@AutoLog(value = "平台账号-开始同步到平台")
	@ApiOperation(value="平台账号-同步到平台", notes="平台账号-同步到平台")
	@RequestMapping(value = "/syncInfo", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> syncInfo(@RequestBody FansCtrlAccount fansCtrlAccount) {
		fansCtrlAccount.setSyncStatusInfo("start");
		fansCtrlAccount.setStartSyncInfoDateTime(new Date());

		if(sheetLaunch.syncInfo(fansCtrlAccount)){
			fansCtrlAccountService.updateById(fansCtrlAccount);
			return Result.OK("个人信息（头像、封面照片、简介）已开始同步平台，请等待返回结果!");
		}else{
			return Result.error("个人信息同步到平台失败!");
		}
	}
	@AutoLog(value = "平台账号-调整ads浏览器")
	@ApiOperation(value="平台账号-调整ads浏览器", notes="平台账号-调整ads浏览器")
	@RequestMapping(value = "/updateAds1", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> updateAds1(@RequestParam(name="id",required=true) String id) {
		FansCtrlAccount fansCtrlAccount=this.fansCtrlAccountService.getById(id);
		fansCtrlAccount.setMountState("un_2fa");
		// 更新googlesheet
		boolean ret=this.sheetLaunch.mountAccountReady(fansCtrlAccount);
		if(ret){
			this.fansCtrlAccountService.updateById(fansCtrlAccount);
			return Result.OK("确认调整ADS成功!");
		}
		return Result.error("确认调整ADS失败!");

	}

	@AutoLog(value = "平台账号-编辑")
	@ApiOperation(value="平台账号-编辑", notes="平台账号-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansCtrlAccount fansCtrlAccount) {
		FansCtrlAccount fansCtrlAccountOld=this.fansCtrlAccountService.getById(fansCtrlAccount.getId());
		if(fansCtrlAccountOld.getAccountType()!=null){
			if((fansCtrlAccountOld.getAccountType()==null || !fansCtrlAccountOld.getAccountType().equals("small")) && fansCtrlAccount.getAccountType().equals("small")){
				//改成小号，需要建搜粉sheet
				this.sheetLaunch.createSearchSheets(fansCtrlAccount.getUsername());
			}
		}

		//fansCtrlAccount.setSyncStatusInfo(null);
		String newProfile=this.fansPostService.dealImgPathLocal(fansCtrlAccount.getProfile());
		//信息手动修改时，与原数据对应，哪个变动哪个就更改状态值
		//头像
		if(newProfile!=null&& !newProfile.equals(fansCtrlAccountOld.getProfile())){
			fansCtrlAccount.setProfileInfoState("wait");
		}
		fansCtrlAccount.setProfile(newProfile);
		//名字
		if(fansCtrlAccount.getName()!=null && !fansCtrlAccount.getName().equals(fansCtrlAccountOld.getName())){
			fansCtrlAccount.setNameInfoState("wait");
		}
		//封面
		if(fansCtrlAccount.getCover()!=null  && !fansCtrlAccount.getCover().equals(fansCtrlAccountOld.getCover())){
			fansCtrlAccount.setCoverInfoState("wait");
		}
		//简介
		if(fansCtrlAccount.getIntro()!=null  && !fansCtrlAccount.getIntro().equals(fansCtrlAccountOld.getIntro())){
			fansCtrlAccount.setIntroInfoState("wait");
		}
		fansCtrlAccountService.updateById(fansCtrlAccount);

		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "平台账号-通过id删除")
	@ApiOperation(value="平台账号-通过id删除", notes="平台账号-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansCtrlAccountService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "平台账号-批量删除")
	@ApiOperation(value="平台账号-批量删除", notes="平台账号-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansCtrlAccountService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "平台账号-通过id查询")
	@ApiOperation(value="平台账号-通过id查询", notes="平台账号-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansCtrlAccount> queryById(@RequestParam(name="id",required=true) String id) {
		FansCtrlAccount fansCtrlAccount = fansCtrlAccountService.getById(id);
		if(fansCtrlAccount==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansCtrlAccount);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansCtrlAccount
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansCtrlAccount fansCtrlAccount) {
        return super.exportXls(request, fansCtrlAccount, FansCtrlAccount.class, "平台账号");
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
        return super.importExcel(request, response, FansCtrlAccount.class);
    }

	public static void main(String[] args) {
		String fakey="a b c ";
		String newFakey="";
		String[] arr=StringUtils.split(fakey," ");
		if(arr==null || arr.length==0){
			newFakey=fakey;
		}else{
			for (int i = 0; i < arr.length; i++) {
				newFakey=newFakey+arr[i];
			}
		}
		System.out.println(newFakey);

	}
}
