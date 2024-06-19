package org.jeecg.modules.fans.interact.entity;

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
 * @Description: 粉丝列表
 * @Author: jeecg-boot
 * @Date:   2024-02-11
 * @Version: V1.0
 */
@Data
@TableName("fans_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_list对象", description="粉丝列表")
public class FansList implements Serializable {
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

    @Excel(name = "关联账号名称", width = 15)
    @ApiModelProperty(value = "关联账号名称")
    private String username;
	@Excel(name = "好友名称", width = 15)
    @ApiModelProperty(value = "好友名称")
    private String fansNickname;
    @ApiModelProperty(value = "好友个人主页")
    private String fansLink;
    @Excel(name = "好友头像", width = 15)
    @ApiModelProperty(value = "好友头像")
    private String fansProfile;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "最近互动时间")
    private Date interactTime;

	@Excel(name = "好友身份状态", width = 15, dicCode = "fans_status")
	@Dict(dicCode = "fans_status")
    @ApiModelProperty(value = "好友身份状态")
    private String fansStatus;

    @Excel(name = "加入方式", width = 15, dicCode = "join_way")
    @Dict(dicCode = "join_way")
    @ApiModelProperty(value = "加入方式 主动/被动/粉丝")
    private String joinWay;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "首次接触时间")
    private Date joinTime;

    @ApiModelProperty(value = "私信数量")
    private Integer msgNum;

    @Excel(name = "未处理的互动消息数", width = 15)
    @ApiModelProperty(value = "未处理的互动消息数")
    private Integer undealMsgNum;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上一次好友名单同步时间")
    private Date syncFansDataDateTime;


}