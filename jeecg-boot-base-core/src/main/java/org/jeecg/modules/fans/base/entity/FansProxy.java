package org.jeecg.modules.fans.base.entity;

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
 * @Description: 代理管理
 * @Author: jeecg-boot
 * @Date:   2024-05-19
 * @Version: V1.0
 */
@Data
@TableName("fans_proxy")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_proxy对象", description="代理管理")
public class FansProxy implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**代理国家*/
	@Excel(name = "代理国家", width = 15, dicCode = "country")
	@Dict(dicCode = "country")
    @ApiModelProperty(value = "代理国家")
    private java.lang.String country;
	/**代理平台*/
	@Excel(name = "代理平台", width = 15, dicCode = "proxy_soft")
	@Dict(dicCode = "proxy_soft")
    @ApiModelProperty(value = "代理平台")
    private java.lang.String proxySoft;
	/**代理地址*/
	@Excel(name = "代理地址", width = 15)
    @ApiModelProperty(value = "代理地址")
    private java.lang.String proxyName;
	/**代理类型*/
	@Excel(name = "代理类型", width = 15, dicCode = "proxy_type")
	@Dict(dicCode = "proxy_type")
    @ApiModelProperty(value = "代理类型")
    private java.lang.String proxyType;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**代理ip*/
	@Excel(name = "代理ip", width = 15)
    @ApiModelProperty(value = "代理ip")
    private java.lang.String proxyHost;
	/**代理端口*/
	@Excel(name = "代理端口", width = 15)
    @ApiModelProperty(value = "代理端口")
    private java.lang.String proxyPort;
	/**代理账号*/
	@Excel(name = "代理账号", width = 15)
    @ApiModelProperty(value = "代理账号")
    private java.lang.String proxyUser;
	/**代理密码*/
	@Excel(name = "代理密码", width = 15)
    @ApiModelProperty(value = "代理密码")
    private java.lang.String proxyPassword;
	/**FB账号数*/
	@Excel(name = "FB账号数", width = 15)
    @ApiModelProperty(value = "FB账号数")
    private java.lang.Integer fbUseNum;
}
