package org.jeecg.modules.fans.interact.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: ai生成模版请求
 * @Author: jeecg-boot
 * @Date:   2024-02-04
 * @Version: V1.0
 */
@Data
public class AccontBatchVO {

	@ApiModelProperty(value = "平台域名")
	private String fansDomain;
	@ApiModelProperty(value = "号源文本")
	private String txt;
	@ApiModelProperty(value = "客群")
	@Dict(dicCode = "fans_cg,code,code")
	private String cg;
	@ApiModelProperty(value = "关联代理Id")
	private String proxyId;
	@ApiModelProperty(value = "国家")
	private String country;
	@ApiModelProperty(value = "是否本国号源")
	@Dict(dicCode = "yn")
	private Integer localSrc;


	@ApiModelProperty(value = "是否自动换头像")
	@Dict(dicCode = "yn")
	private Integer autoProfile;

	@ApiModelProperty(value = "是否自动改名")
	@Dict(dicCode = "yn")
	private Integer autoName;

	@ApiModelProperty(value = "是否自动加好友")
	@Dict(dicCode = "yn")
	private Integer autoFriend;

	@ApiModelProperty(value = "是否自动发帖")
	@Dict(dicCode = "yn")
	private Integer autoPost;

	@ApiModelProperty(value = "绑定人设模版")
	private String roleTempleId;

}
