package org.jeecg.modules.fans.content.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: 图片库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Data
@TableName("fans_content_img")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_content_img对象", description="图片库")
public class FansContentImg implements Serializable {
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
    @ApiModelProperty(value = "图片标题")
    private String imgName;
    @ApiModelProperty(value = "图片描述")
    private String imgDesc;
	/**图片存储地址*/
	@Excel(name = "人工图片存储地址", width = 15)
    @ApiModelProperty(value = "人工图片存储地址")
    private String path;
    @ApiModelProperty(value = "最终图片存储地址")
    private String imgPath;
	/**图片生成时间*/
	@Excel(name = "图片生成时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "图片生成时间")
    private Date genarateTime;
	/**生成方式*/
	@Excel(name = "生成方式", width = 15)
    @ApiModelProperty(value = "生成方式")
    @Dict(dicCode = "genarate_way")
    private String genarateWay;
    @ApiModelProperty(value = "图片分类")
    @Dict(dicCode = "img_type")
    private String imgType;

	/**使用数量*/
	@Excel(name = "使用数量", width = 15)
    @ApiModelProperty(value = "使用数量")
    private Integer useNum;
	/**比例*/
	@Excel(name = "比例", width = 15)
    @ApiModelProperty(value = "比例")
    @Dict(dicCode = "ratio")
    private String ratio;
	/**AI参照图片*/
	@Excel(name = "AI参照图片", width = 15)
    @ApiModelProperty(value = "AI参照图片")
    private String refImg;
	/**标签信息*/
    @ApiModelProperty(value = "标签信息")
    private String features;

    @Dict(dicCode = "img_style")
    @ApiModelProperty(value = "风格")
    private String imgStyle;

    @Dict(dicCode = "img_choose")
    @ApiModelProperty(value = "使用AI还是人工的图")
    private String imgChoose;


    @Dict(dictTable = "fans_role_temple", dicText = "role_temple_name", dicCode = "id")
    @ApiModelProperty(value = "关联人设")
    private String roleId;

    @ApiModelProperty(value = "生成数量")
    @TableField(exist = false)
    private Integer reqNum;
}
