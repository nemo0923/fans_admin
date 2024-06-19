package org.jeecg.modules.fans.market.entity;

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
 * @Description: 搜粉名单
 * @Author: jeecg-boot
 * @Date:   2024-04-05
 * @Version: V1.0
 */
@Data
@TableName("fans_lib")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_lib对象", description="搜粉名单")
public class FansLib implements Serializable {
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
	/**搜粉url*/
	@Excel(name = "搜粉url", width = 15, dictTable = "fans_search", dicText = "name", dicCode = "url")
	@Dict(dictTable = "fans_search", dicText = "name", dicCode = "url")
    @ApiModelProperty(value = "搜粉url")
    private String searchUrl;
	/**客群*/
	@Excel(name = "客群", width = 15, dicCode = "cg")
	@Dict(dicCode = "fans_cg,code,code")
    @ApiModelProperty(value = "客群")
    private String cg;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private String name;
	/**好友信息*/
	@Excel(name = "好友信息", width = 15)
    @ApiModelProperty(value = "好友信息")
    private String fansNum;
	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
    private String profile;
	/**主页*/
	@Excel(name = "主页", width = 15)
    @ApiModelProperty(value = "主页")
    private String link;
	/**个人信息*/
	@Excel(name = "个人信息", width = 15)
    @ApiModelProperty(value = "个人信息")
    private String info;
	/**上层好友链接*/
	@Excel(name = "上层好友链接", width = 15)
    @ApiModelProperty(value = "上层好友链接")
    private String fromLink;
	/**采集时间*/
	@Excel(name = "采集时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "采集时间")
    private Date crawtime;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "search_state")
	@Dict(dicCode = "search_state")
    @ApiModelProperty(value = "状态")
    private String searchState;
	/**已加好友账号*/
	@Excel(name = "已加好友账号", width = 15)
    @ApiModelProperty(value = "已加好友账号")
    private String account;
	/**加好友时间*/
	@Excel(name = "加好友时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "加好友时间")
    private Date friendTime;
	/**最近发私信内容*/
	@Excel(name = "最近发私信内容", width = 15)
    @ApiModelProperty(value = "最近发私信内容")
    private String msgContent;
	/**最近发私信时间*/
	@Excel(name = "最近发私信时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最近发私信时间")
    private Date msgTime;
}
