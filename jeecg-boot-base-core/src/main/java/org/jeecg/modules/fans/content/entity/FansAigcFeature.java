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
 * @Description: 标签库
 * @Author: jeecg-boot
 * @Date:   2024-02-27
 * @Version: V1.0
 */
@Data
@TableName("fans_aigc_feature")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_aigc_feature对象", description="标签库")
public class FansAigcFeature implements Serializable {
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
	/**标签主题*/
	@Excel(name = "标签主题", width = 15, dicCode = "feature_name")
	@Dict(dicCode = "feature_name")
    @ApiModelProperty(value = "标签主题")
    private String featureName;
	/**标签名称*/
	@Excel(name = "标签名称", width = 15)
    @ApiModelProperty(value = "标签名称")
    private String tagName;
	/**是否热点*/
	@Excel(name = "是否热点", width = 15)
    @ApiModelProperty(value = "是否热点")
    private Integer isHot;
    /**是否热点*/
    @ApiModelProperty(value = "适用场景")
    private String scene;
}
