package org.jeecg.modules.fans.interact.vo;

import java.util.List;

import org.jeecg.modules.fans.interact.entity.FansList;
import org.jeecg.modules.fans.interact.entity.FansMsg;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: ai生成模版请求
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Data
@ApiModel(value="ai生成模版请求", description="ai生成模版请求")
public class AiRoleReqVO {
	@ApiModelProperty(value = "模版id")
	private String id;
	@ApiModelProperty(value = "模版名称")
	private String roleTempleName;
	@ApiModelProperty(value = "生成描述")
	private String desc;

	@ApiModelProperty(value = "国家")
	private String country;
	@ApiModelProperty(value = "行业")
	@Dict(dicCode = "ability")
	private String ability;
	@ApiModelProperty(value = "年收入")
	@Dict(dicCode = "income")
	private String income;
	@Dict(dicCode = "ages")
	@ApiModelProperty(value = "年龄段")
	private String ages;
	@ApiModelProperty(value = "性别")
	@Dict(dicCode = "sex")
	private String sex;
	@ApiModelProperty(value = "发色")
	@Dict(dicCode = "hair_color")
	private String hairColor;

	@ApiModelProperty(value = "生成数量")
	private String num;

	@ApiModelProperty(value = "工作内容要求")
	private String abilityReq;

	@ApiModelProperty(value = "个人介绍要求")
	private String introReq;

	@ApiModelProperty(value = "工作经历要求")
	private String professionReq;

	@ApiModelProperty(value = "性格要求要求")
	private String natureReq;

	@ApiModelProperty(value = "爱好要求要求")
	private String interestReq;

	@ApiModelProperty(value = "感情经历要求")
	private String loveReq;

	@ApiModelProperty(value = "教育经历要求")
	private String educationReq;

	@ApiModelProperty(value = "参考头像地址")
	private String refProfilePath;

	@ApiModelProperty(value = "头像")
	private String profile;

	@ApiModelProperty(value = "头像其他需求描述")
	private String profileDesc;

	@ApiModelProperty(value = "人设生成信息")
	private String roleRaw;

	@ApiModelProperty(value = "人设机器人线程id")
	private String leaderThreadId;

}
