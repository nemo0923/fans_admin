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
 * @Description: 云控设备
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Data
@TableName("fans_ctrl_node")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_ctrl_node对象", description="云控设备")
public class FansCtrlNode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ads代理id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ads代理id")
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
	/**设备ip*/
	@Excel(name = "设备ip", width = 15)
    @ApiModelProperty(value = "设备ip")
    private String ip;
	/**设备登记时间*/
	@Excel(name = "设备登记时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "设备登记时间")
    private Date regtime;
	/**最近活跃时间*/
	@Excel(name = "最近活跃时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最近活跃时间")
    private Date activetime;
	/**在线账户数量*/
	@Excel(name = "活跃账户数量", width = 15)
    @ApiModelProperty(value = "活跃账户数量")
    private String onlineNum;
	/**运行状态*/
	@Excel(name = "运行状态", width = 15)
    @Dict(dicCode = "ctrl_status")
    @ApiModelProperty(value = "运行状态")
    private String ctrlStatus;
}
