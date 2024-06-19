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
 * @Description: 搜粉计划
 * @Author: jeecg-boot
 * @Date:   2024-04-05
 * @Version: V1.0
 */
@Data
@TableName("fans_search")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_search对象", description="搜粉计划")
public class FansSearch implements Serializable {
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
	/**计划名称*/
	@Excel(name = "计划名称", width = 15)
    @ApiModelProperty(value = "计划名称")
    private String name;
	/**客群*/
	@Excel(name = "客群", width = 15, dicCode = "cg")
	@Dict(dicCode = "fans_cg,code,code")
    @ApiModelProperty(value = "客群")
    private String cg;
	/**搜索网址*/
	@Excel(name = "搜索网址", width = 15)
    @ApiModelProperty(value = "搜索网址")
    private String url;
	/**计划数量*/
	@Excel(name = "计划数量", width = 15)
    @ApiModelProperty(value = "计划数量")
    private Integer planUserNum;
	/**实际数量*/
	@Excel(name = "实际数量", width = 15)
    @ApiModelProperty(value = "实际数量")
    private Integer userNum;

    @ApiModelProperty(value = "小号爬虫账号")
    private String username;
}
