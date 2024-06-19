package org.jeecg.modules.fans.content.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 人设模版
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Data
@TableName("fans_role_temple")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_role_temple对象", description="人设模版")
public class FansRoleTemple implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**生成时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@ApiModelProperty(value = "生成时间")
	private Date genareteTime;
	/**模版名称*/
	@Excel(name = "模版名称", width = 15)
    @ApiModelProperty(value = "模版名称")
    private String roleTempleName;
	/**帖子数*/
	@Excel(name = "帖子数", width = 15)
    @ApiModelProperty(value = "帖子数")
    private Integer postNum;
	/**粉丝数*/
	@Excel(name = "粉丝数", width = 15)
    @ApiModelProperty(value = "粉丝数")
    private Integer fansNum;
	/**好友数*/
	@Excel(name = "好友数", width = 15)
    @ApiModelProperty(value = "好友数")
    private Integer friendNum;
	/**点赞数*/
	@Excel(name = "点赞数", width = 15)
    @ApiModelProperty(value = "点赞数")
    private Integer goodNum;
	/**国家*/
	@Excel(name = "国家", width = 15, dicCode = "country")
	@Dict(dicCode = "country")
    @ApiModelProperty(value = "国家")
    private String country;
	/**性别*/
	@Excel(name = "性别", width = 15, dicCode = "sex")
	@Dict(dicCode = "sex")
    @ApiModelProperty(value = "性别")
    private String sex;
	/**宗教信仰*/
	@Excel(name = "宗教信仰", width = 15, dicCode = "religion")
	@Dict(dicCode = "religion")
    @ApiModelProperty(value = "宗教信仰")
    private String religion;
	/**年龄段*/
	@Excel(name = "年龄段", width = 15, dicCode = "ages")
	@Dict(dicCode = "ages")
    @ApiModelProperty(value = "年龄段")
    private String ages;
	/**血型*/
	@Excel(name = "血型", width = 15, dicCode = "blood_type")
	@Dict(dicCode = "blood_type")
    @ApiModelProperty(value = "血型")
    private String bloodType;
	/**星座*/
	@Excel(name = "星座", width = 15, dicCode = "constellation")
	@Dict(dicCode = "constellation")
    @ApiModelProperty(value = "星座")
    private String constellation;
	/**本国姓名*/
	@Excel(name = "本国姓名", width = 15)
    @ApiModelProperty(value = "本国姓名")
    private String name;
	/**英文姓名*/
	@Excel(name = "英文姓名", width = 15)
    @ApiModelProperty(value = "英文姓名")
    private String englishName;
	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
    private String profile;
	/**出生年月*/
	@Excel(name = "出生年月", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出生年月")
    private Date birthYm;
	/**身高*/
	@Excel(name = "身高", width = 15)
    @ApiModelProperty(value = "身高")
    private Integer height;
	/**体重*/
	@Excel(name = "体重", width = 15)
    @ApiModelProperty(value = "体重")
    private Integer weight;
	/**胸围*/
	@Excel(name = "胸围", width = 15)
    @ApiModelProperty(value = "胸围")
    private Integer chest;
	/**腰围*/
	@Excel(name = "腰围", width = 15)
    @ApiModelProperty(value = "腰围")
    private Integer waistline;
	/**臀围*/
	@Excel(name = "臀围", width = 15)
    @ApiModelProperty(value = "臀围")
    private Integer hipline;
	/**个人介绍*/
	@Excel(name = "个人介绍", width = 15)
    @ApiModelProperty(value = "个人介绍")
    private String intro;
	/**家庭成员*/
	@Excel(name = "家庭成员", width = 15)
    @ApiModelProperty(value = "家庭成员")
    private String family;
	/**性格特点*/
	@Excel(name = "性格特点", width = 15)
    @ApiModelProperty(value = "性格特点")
    private String nature;
	/**兴趣爱好*/
	@Excel(name = "兴趣爱好", width = 15)
    @ApiModelProperty(value = "兴趣爱好")
    private String interest;
	/**职业身份*/
	@Excel(name = "行业", width = 15)
    @ApiModelProperty(value = "行业")
	@Dict(dicCode = "ability")
    private String ability;
	@ApiModelProperty(value = "年收入")
	@Dict(dicCode = "income")
	private String income;
	/**职业经历*/
	@Excel(name = "职业经历", width = 15)
    @ApiModelProperty(value = "职业经历")
    private String profession;
	/**感情经历*/
	@Excel(name = "感情经历", width = 15)
    @ApiModelProperty(value = "感情经历")
    private String love;
	/**教育经历*/
	@Excel(name = "教育经历", width = 15)
    @ApiModelProperty(value = "教育经历")
    private String education;

	@Dict(dicCode = "yn")
	@ApiModelProperty(value = "是否可用")
	private String isFinish;

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

	@ApiModelProperty(value = "住址周围环境")
	private String addrEnvironment;

	@ApiModelProperty(value = "常去餐厅")
	private String restaurant;

	@ApiModelProperty(value = "人设生成信息")
	private String roleRaw;

	@ApiModelProperty(value = "发色")
	@Dict(dicCode = "hair_color")
	private String hairColor;

	@ApiModelProperty(value = "头像其他需求描述")
	private String profileDesc;

	@ApiModelProperty(value = "人设机器人线程id")
	private String leaderThreadId;

	@ApiModelProperty(value = "人设助手id")
	private String assistantId;

	@ApiModelProperty(value = "人设线程id")
	private String threadId;

	@ApiModelProperty(value = "ai提示词")
	private String prompt;
}
