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
 * @Description: 帖子发布
 * @Author: jeecg-boot
 * @Date:   2024-02-17
 * @Version: V1.0
 */
@Data
@TableName("fans_post_pub")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_post_pub对象", description="帖子发布")
public class FansPostPub implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键 数据库序列自增*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;
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
	/**帖子id*/
	@Excel(name = "帖子id", width = 15)
    @ApiModelProperty(value = "帖子id")
    private String postId;
	/**帖子标题*/
    @ApiModelProperty(value = "帖子标题")
    private String postTitle;
	/**关联人设*/
	@Excel(name = "关联人设", width = 15)
    @Dict(dictTable = "fans_role_temple", dicText = "role_temple_name", dicCode = "id")
    @ApiModelProperty(value = "关联人设")
    private String roleId;
	/**发布时间*/
	@Excel(name = "发布时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;
	/**是否发布*/
	@Excel(name = "是否发布", width = 15, dicCode = "yn")
    @Dict(dicCode = "sync_status")
    @ApiModelProperty(value = "是否发布")
    private String isPublish;
	/**发布平台*/
	@Excel(name = "发布平台", width = 15, dicCode = "domain")
	@Dict(dicCode = "domain")
    @ApiModelProperty(value = "发布平台")
    private String domain;
	/**发布账号*/
	@Excel(name = "发布账号", width = 15, dictTable = "fans_ctrl_account", dicText = "username", dicCode = "username")
	@Dict(dictTable = "fans_ctrl_account", dicText = "username", dicCode = "username")
    @ApiModelProperty(value = "发布账号")
    private String domainAccount;
	/**点赞数*/
	@Excel(name = "点赞数", width = 15)
    @ApiModelProperty(value = "点赞数")
    private Integer goodNum;
	/**评论数*/
	@Excel(name = "评论数", width = 15)
    @ApiModelProperty(value = "评论数")
    private Integer commentNum;
	/**分享数*/
	@Excel(name = "分享数", width = 15)
    @ApiModelProperty(value = "分享数")
    private Integer shareNum;

    @ApiModelProperty(value = "帖子在平台的访问url")
    private String postUrl;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上一次运营数据同步时间")
    private Date syncPostDataDateTime;

    @ApiModelProperty(value = "帖子内容")
    private String content;

    @ApiModelProperty(value = "帖子标签")
    private String features;

    @ApiModelProperty(value = "最终图片存储地址")
    private String imgPath;

    @ApiModelProperty(value = "账号头像")
    private String profile;

    @ApiModelProperty(value = "账号姓名")
    private String name;

    @Dict(dicCode = "country")
    @ApiModelProperty(value = "账号国家")
    private String country;
    @ApiModelProperty(value = "个人主页链接")
    private String link;

    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否换脸")
    private Integer isFace;
}
