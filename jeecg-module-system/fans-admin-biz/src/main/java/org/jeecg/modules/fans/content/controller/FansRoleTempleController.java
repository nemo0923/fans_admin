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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.fans.Tools;
import org.jeecg.modules.fans.content.entity.FansRoleTemple;
import org.jeecg.modules.fans.content.service.IFansRoleTempleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.fans.interact.vo.AiRoleReqVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @Description: 人设模版
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Api(tags="人设模版")
@RestController
@RequestMapping("/content/fansRoleTemple")
@Slf4j
public class FansRoleTempleController extends JeecgController<FansRoleTemple, IFansRoleTempleService> {
	@Autowired
	private IFansRoleTempleService fansRoleTempleService;
	 @Value(value = "${jeecg.path.upload}")
	 private String uploadpath;
	 @Value(value = "${ai.sdPath}")
	 private String sdPath;
	 @Value(value = "${ai.gptPath}")
	 private String gptPath;
	/**
	 * 分页列表查询
	 *
	 * @param fansRoleTemple
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "人设模版-分页列表查询")
	@ApiOperation(value="人设模版-分页列表查询", notes="人设模版-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FansRoleTemple>> queryPageList(FansRoleTemple fansRoleTemple,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FansRoleTemple> queryWrapper = QueryGenerator.initQueryWrapper(fansRoleTemple, req.getParameterMap());
		Page<FansRoleTemple> page = new Page<FansRoleTemple>(pageNo, pageSize);
		IPage<FansRoleTemple> pageList = fansRoleTempleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fansRoleTemple
	 * @return
	 */
	@AutoLog(value = "人设模版-添加")
	@ApiOperation(value="人设模版-添加", notes="人设模版-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FansRoleTemple fansRoleTemple) {
		fansRoleTemple.setGenareteTime(new Date());
		fansRoleTempleService.save(fansRoleTemple);

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fansRoleTemple
	 * @return
	 */
	@AutoLog(value = "人设模版-编辑")
	@ApiOperation(value="人设模版-编辑", notes="人设模版-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FansRoleTemple fansRoleTemple) {
		//人工完善人设，需要同步到gpt
		//拼接人设信息
		String instructions="我重新更新了我的个人背景资料，请你记住。当文章的主题内容与我的个人背景资料相关时，请参考我的背景资料，与我的个人背景资料进行关联，并避免出现文章内容与我的个人背景资料的事实相冲突的情况。当文章的主题内容与我的个人背景资料无关时，请忽略我的背景资料，尽量不要牵强附会进行强关联。我的个人背景资料如下面文字所述：";
		if(fansRoleTemple.getPrompt()!=null && !fansRoleTemple.getPrompt().equals("")){
			instructions=instructions+"我的总述:"+fansRoleTemple.getPrompt()+".";
		}
		if(fansRoleTemple.getIntro()!=null && !fansRoleTemple.getIntro().equals("")){
			instructions=instructions+"我的个人简述:"+fansRoleTemple.getIntro()+".";
		}
		if(fansRoleTemple.getAbility()!=null && !fansRoleTemple.getAbility().equals("")){
			instructions=instructions+"我从事的行业:"+fansRoleTemple.getAbility()+".";
		}
		if(fansRoleTemple.getAddrEnvironment()!=null && !fansRoleTemple.getAddrEnvironment().equals("")){
			instructions=instructions+"我的住处周围环境:"+fansRoleTemple.getAddrEnvironment()+".";
		}
		if(fansRoleTemple.getAges()!=null && !fansRoleTemple.getAges().equals("")){
			instructions=instructions+"我的年龄:"+fansRoleTemple.getAges()+".";
		}
		if(fansRoleTemple.getCountry()!=null && !fansRoleTemple.getCountry().equals("")){
			instructions=instructions+"我的国籍:"+fansRoleTemple.getCountry()+".";
		}
		if(fansRoleTemple.getEducation()!=null && !fansRoleTemple.getEducation().equals("")){
			instructions=instructions+"我的教育背景:"+fansRoleTemple.getEducation()+".";
		}
		if(fansRoleTemple.getFamily()!=null && !fansRoleTemple.getFamily().equals("")){
			instructions=instructions+"我的家庭成员:"+fansRoleTemple.getFamily()+".";
		}
		if(fansRoleTemple.getHairColor()!=null && !fansRoleTemple.getHairColor().equals("")){
			instructions=instructions+"我的头发颜色:"+fansRoleTemple.getHairColor()+".";
		}
		if(fansRoleTemple.getHeight()!=null && !fansRoleTemple.getHeight().equals("")){
			instructions=instructions+"我的身高:"+fansRoleTemple.getHeight()+"cm.";
		}
		if(fansRoleTemple.getIncome()!=null && !fansRoleTemple.getIncome().equals("")){
			instructions=instructions+"我的年收入:"+fansRoleTemple.getIncome()+"美元.";
		}
		if(fansRoleTemple.getInterest()!=null && !fansRoleTemple.getInterest().equals("")){
			instructions=instructions+"我的兴趣爱好:"+fansRoleTemple.getInterest()+".";
		}

		if(fansRoleTemple.getWeight()!=null && !fansRoleTemple.getWeight().equals("")){
			instructions=instructions+"我的体重:"+fansRoleTemple.getWeight()+"kg.";
		}
		if(fansRoleTemple.getSex()!=null && !fansRoleTemple.getSex().equals("")){
			instructions=instructions+"我的性别:"+fansRoleTemple.getSex()+".";
		}
		if(fansRoleTemple.getRestaurant()!=null && !fansRoleTemple.getRestaurant().equals("")){
			instructions=instructions+"我常去的餐厅:"+fansRoleTemple.getRestaurant()+".";
		}
		if(fansRoleTemple.getProfession()!=null && !fansRoleTemple.getProfession().equals("")){
			instructions=instructions+"我的职业经历:"+fansRoleTemple.getProfession()+".";
		}
		if(fansRoleTemple.getNature()!=null && !fansRoleTemple.getNature().equals("")){
			instructions=instructions+"我的性格特点:"+fansRoleTemple.getNature()+".";
		}
		if(fansRoleTemple.getLove()!=null && !fansRoleTemple.getLove().equals("")){
			instructions=instructions+"我的情感恋爱经历:"+fansRoleTemple.getLove()+".";
		}
		//发送请求
		//System.out.println("人工完善人设内容：");
		//System.out.println(instructions);
		String url=gptPath+"/assistants/"+fansRoleTemple.getAssistantId();
		JSONObject postData=new JSONObject();
		String[] arr= new String[0];
		postData.put("name",fansRoleTemple.getName());
		postData.put("instructions",instructions);
		postData.put("tools",arr);
		postData.put("model","gpt-4");
		Tools.put(url,postData);

		fansRoleTempleService.updateById(fansRoleTemple);
		return Result.OK("人工完善人设内容保存成功!");
	}
	 @AutoLog(value = "人设模版-ai生成")
	 @ApiOperation(value="人设模版-ai生成", notes="人设模版-ai生成")
	 @RequestMapping(value = "/aiRoleGenarete", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> aiRoleGenarete(@RequestBody AiRoleReqVO vo) {
		//一些信息先保存入库
		 FansRoleTemple fansRoleTemple=new  FansRoleTemple();
		 fansRoleTemple.setRoleTempleName(vo.getRoleTempleName());
		 fansRoleTemple.setCountry(vo.getCountry());
		 fansRoleTemple.setAbility(vo.getAbility());
		 fansRoleTemple.setSex(vo.getSex());
		 fansRoleTemple.setAges(vo.getAges());
		 fansRoleTemple.setAbilityReq(vo.getAbilityReq());
		 fansRoleTemple.setIncome(vo.getIncome());
		 fansRoleTemple.setIntroReq(vo.getIntroReq());
		 fansRoleTemple.setNatureReq(vo.getNatureReq());
		 fansRoleTemple.setAbilityReq(vo.getAbilityReq());
		 fansRoleTemple.setProfessionReq(vo.getProfessionReq());
		 fansRoleTemple.setEducationReq(vo.getEducationReq());
		 fansRoleTemple.setLoveReq(vo.getLoveReq());
		 fansRoleTemple.setRefProfilePath(vo.getRefProfilePath());
		 fansRoleTemple.setHairColor(vo.getHairColor());
		 fansRoleTemple.setRoleRaw(vo.getRoleRaw());
		 fansRoleTemple.setProfileDesc(vo.getProfileDesc());
		 fansRoleTemple.setGenareteTime(new Date());
		 fansRoleTemple.setIncome(vo.getIncome());
		 fansRoleTemple.setAbility(vo.getAbility());
		 fansRoleTemple.setId(vo.getId());
		 fansRoleTemple.setLeaderThreadId(vo.getLeaderThreadId());

		 //新增加的才下载下载图片，保存为相对地址
		 if(vo.getProfile()!=null && !vo.getProfile().equals("") && vo.getProfile().startsWith("http")) {
			 String[] strs= StringUtils.split(vo.getProfile(),"/");
			 if(strs!=null && strs.length>0){
				 String savepath =  "profile/" + strs[strs.length-1];
				 String savepathWhole =  uploadpath+"/"+savepath;
				 try {
					 Tools.FileDownloader(vo.getProfile(), savepathWhole);
					 fansRoleTemple.setProfile(savepath);
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			 }

		 }


		 //后台保存时，将提示词发到第二个机器人，获取人设助手id

		 String url=gptPath+"/assistants";
		 JSONObject postData=new JSONObject();
		 String name = StringUtils.substringBetween(fansRoleTemple.getRoleRaw(),"Name:","\n");
		 if(name==null || name.equals("")){
			 name = StringUtils.substringBetween(fansRoleTemple.getRoleRaw(),"Name**","\n ");
		 }
		 if(name==null || name.equals("")){
			 name = StringUtils.substringBetween(fansRoleTemple.getRoleRaw(),"Name:","Age:");
		 }
		 if(name==null || name.equals("")){
			 name = StringUtils.substringBetween(fansRoleTemple.getRoleRaw(),"Name:","Gender: ");
		 }
		 if(name==null || name.equals("")){
			 name = StringUtils.substringBetween(fansRoleTemple.getRoleRaw(),"Name**: ","Gender:");
		 }
		 if(name==null || name.equals("")){
			 name = StringUtils.substringBetween(fansRoleTemple.getRoleRaw(),"Name**: ","Age:");
		 }

		 if(name==null || name.equals("")){
			 name = "nemo";
		 }
		 if(name.length()>30){
			 name = name.substring(0,29);
		 }
		 System.out.println("name："+name);
		 fansRoleTemple.setName(name);
		 String instructions = StringUtils.substringBetween(fansRoleTemple.getRoleRaw(),"Character Prompt","Facial Prompt");
/*
		 if(instructionsTmp==null || instructionsTmp.equals("")){
			 instructionsTmp = StringUtils.substringAfter(fansRoleTemple.getRoleRaw(),"Format");
		 }
		 if(instructionsTmp==null  || instructionsTmp.equals("")) {
			 instructionsTmp = StringUtils.substringAfter(fansRoleTemple.getRoleRaw(),"format");
		 }
		 if(instructionsTmp==null  || instructionsTmp.equals("")){
			 instructionsTmp = StringUtils.substringAfter(fansRoleTemple.getRoleRaw(),"prompt");
		 }

		 if(instructionsTmp==null  || instructionsTmp.equals("")){
			 instructionsTmp = StringUtils.substringAfter(fansRoleTemple.getRoleRaw(),"GPT-Compatible Character Information");
		 }
		 if(instructionsTmp==null  || instructionsTmp.equals("")){
			 instructionsTmp = StringUtils.substringAfter(fansRoleTemple.getRoleRaw(),"GPT-3 Compatible Character");
		 }
		 if(instructionsTmp==null  || instructionsTmp.equals("")){
			 instructionsTmp = StringUtils.substringAfter(fansRoleTemple.getRoleRaw(),"GPT-3 Formatted Character Information");
		 }
		 System.out.println("instructionsTmp begin：");
		 System.out.println(instructionsTmp);
		 System.out.println("instructionsTmp：end");
		 String instructions =null;
		 if(instructionsTmp!=null  || instructionsTmp.equals("")){
			 instructions = StringUtils.substringBefore(instructionsTmp,"\n\n");
		 }
		 if(instructionsTmp!=null  || instructionsTmp.equals("")){
			 instructions = StringUtils.substringBefore(instructionsTmp,"Markdown");
		 }
		 if(instructions==null  || instructions.equals("")){
			 instructions =instructionsTmp;
		 }
		 */
		 if(instructions!=null  && !instructions.equals("")){
			 String[] arr= new String[0];
			 postData.put("name",name);
			 String instructionsNew="我将我的个人背景资料告诉你，请你记住。当文章的主题内容与我的个人背景资料相关时，请参考我的背景资料，与我的个人背景资料进行关联，并避免出现文章内容与我的个人背景资料的事实相冲突的情况。当文章的主题内容与我的个人背景资料无关时，请忽略我的背景资料，尽量不要牵强附会进行强关联。我的个人背景资料如下面文字所述：";

			 postData.put("instructions",instructionsNew+instructions);
			 fansRoleTemple.setPrompt(instructions);
			 postData.put("tools",arr);
			 postData.put("model","gpt-4");
		 }else{
			 return Result.error("AI人设内容保存失败! GPT生成信息缺失（GPT Prompt字样文字）！");
		 }
		 JSONObject postReturnData=Tools.post(url,postData);
		 if(postReturnData==null){
			 return Result.error("AI人设内容保存失败! GPT通信异常！");
		 }
		 JSONObject response1=postReturnData.getJSONObject("response");
		 if(response1!=null){
			 String assistantId=response1.getString("id");
			 fansRoleTemple.setAssistantId(assistantId);
			 //并尝试首次发帖，发往设助手id，获取贴子线程id
			 String url2=gptPath+"/chat/"+assistantId;
			 JSONObject postData2=new JSONObject();
			 postData2.put("question","未来会持续让您帮我生产内容");
			 JSONObject postReturnData2=Tools.post(url2,postData2);
			 if(postReturnData2==null){
				 return Result.error("AI人设内容保存失败! GPT通信异常！");
			 }
			 String threadId=postReturnData2.getString("threadId");
			 fansRoleTemple.setThreadId(threadId);
		 }


		 if(fansRoleTemple.getId()!=null && !fansRoleTemple.getId().equals("")){

			 this.fansRoleTempleService.updateById(fansRoleTemple);
		 }else{

			 this.fansRoleTempleService.save(fansRoleTemple);
		 }

		 return Result.OK("AI人设内容保存成功!");
	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "人设模版-通过id删除")
	@ApiOperation(value="人设模版-通过id删除", notes="人设模版-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fansRoleTempleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "人设模版-批量删除")
	@ApiOperation(value="人设模版-批量删除", notes="人设模版-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fansRoleTempleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "人设模版-通过id查询")
	@ApiOperation(value="人设模版-通过id查询", notes="人设模版-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FansRoleTemple> queryById(@RequestParam(name="id",required=true) String id) {
		FansRoleTemple fansRoleTemple = fansRoleTempleService.getById(id);
		if(fansRoleTemple==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fansRoleTemple);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fansRoleTemple
    */
    @RequiresPermissions("content:fans_role_temple:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FansRoleTemple fansRoleTemple) {
        return super.exportXls(request, fansRoleTemple, FansRoleTemple.class, "人设模版");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("content:fans_role_temple:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FansRoleTemple.class);
    }

}
