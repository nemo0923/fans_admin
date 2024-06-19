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
 * @Description: 帖子库
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Data
@TableName("fans_post")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_post对象", description="帖子库")
public class FansPost implements Serializable {
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
    @ApiModelProperty(value = "帖子标题")
    private String title;
	/**帖子内容*/
	@Excel(name = "帖子内容", width = 15)
    @ApiModelProperty(value = "帖子内容")
    private String content;
	/**生成方式*/
	@Excel(name = "生成方式", width = 15)
    @ApiModelProperty(value = "生成方式")
    @Dict(dicCode = "genarate_way")
    private String genarateWay;


	/**引用主题*/
	@Excel(name = "引用主题", width = 15)
    @ApiModelProperty(value = "引用主题")
    private String features;

	@Excel(name = "生成时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生成时间")
    private Date genarateTime;
	/**是否发布*/
	@Excel(name = "是否可用", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否可用")
    private String isFinish;

    @Dict(dictTable = "fans_role_temple", dicText = "role_temple_name", dicCode = "id")
    @ApiModelProperty(value = "关联人设")
    private String roleId;

    @ApiModelProperty(value = "帖子生成描述")
    private String postDesc;

    @ApiModelProperty(value = "生成数量")
    @TableField(exist = false)
    private Integer reqNum;

    @ApiModelProperty(value = "每个帖子中图片生成数量")
    private Integer imgReqNum;

    @Dict(dicCode = "img_style")
    @ApiModelProperty(value = "风格")
    private String imgStyle;

    @Dict(dicCode = "img_choose")
    @ApiModelProperty(value = "使用AI还是人工的图")
    private String imgChoose;

    @ApiModelProperty(value = "比例")
    @Dict(dicCode = "ratio")
    private String ratio;

    @ApiModelProperty(value = "图片分类")
    @Dict(dicCode = "img_type")
    private String imgType;

    @ApiModelProperty(value = "图片生成描述")
    private String imgDesc;

    @ApiModelProperty(value = "最终图片存储地址")
    private String imgPath;

    @ApiModelProperty(value = "AI参照图片")
    private String refImg;

    @ApiModelProperty(value = "人工图片存储地址")
    private String path;

    @ApiModelProperty(value = "图片库中图片存储地址")
    private String imgLibPath;


    @ApiModelProperty(value = "语言")
    @Dict(dicCode = "lang")
    private String lang;

    @ApiModelProperty(value = "是否emoji")
    @Dict(dicCode = "yn")
    private String emoji;

    @ApiModelProperty(value = "字数限制")
    @Dict(dicCode = "yn")
    private Integer wordcount;


    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否换脸")
    private Integer isFace;
}
