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
 * @Description: 平台账号
 * @Author: jeecg-boot
 * @Date:   2024-02-11
 * @Version: V1.0
 */
@Data
@TableName("fans_ctrl_account")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fans_ctrl_account对象", description="平台账号")
public class FansCtrlAccount implements Serializable {
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
	/**养号所在设备*/
	@Excel(name = "养号所在设备", width = 15, dictTable = "fans_ctrl_node", dicText = "ip", dicCode = "ip")
	@Dict(dictTable = "fans_ctrl_node", dicText = "id", dicCode = "id")
    @ApiModelProperty(value = "养号所在设备")
    private String fansCtrlNodeId;
	/**浏览器账号*/
	@Excel(name = "浏览器账号", width = 15)
    @ApiModelProperty(value = "浏览器账号")
    private String browserNo;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "养号开始日期")
	private Date beginDate;
	/**平台域名*/
	@Excel(name = "平台域名", width = 15, dicCode = "fans_domain")
	@Dict(dicCode = "fans_domain")
    @ApiModelProperty(value = "平台域名")
    private String fansDomain;
	/**平台账号名*/
	@Excel(name = "平台账号名", width = 15)
    @ApiModelProperty(value = "平台账号名")
    private String username;
	@ApiModelProperty(value = "平台密码")
	private String password;
	/**绑定人设模版*/
	@Excel(name = "绑定人设模版", width = 15, dictTable = "fans_role_temple", dicText = "role_temple_name", dicCode = "id")
	@Dict(dictTable = "fans_role_temple", dicText = "role_temple_name", dicCode = "id")
    @ApiModelProperty(value = "绑定人设模版")
    private String roleTempleId;
	/**最近活跃时间*/
	@Excel(name = "最新互动时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最近活跃时间")
    private Date activetime;
	/**账号在线状态*/
	@Excel(name = "账号在线状态", width = 15, dicCode = "online_status")
	@Dict(dicCode = "online_status")
    @ApiModelProperty(value = "账号在线状态")
    private String onlineStatus;
	 /**平台用户名*/
	@Excel(name = "平台用户名", width = 15)
	@ApiModelProperty(value = "平台用户名")
	private String nickname;
	@Excel(name = "个人主页链接", width = 15)
	@ApiModelProperty(value = "个人主页链接")
	private String link;
	/**发帖数*/
	@Excel(name = "发帖数", width = 15)
	@ApiModelProperty(value = "发帖数")
	private Integer postNum;
	/**粉丝数*/
	@Excel(name = "粉丝数", width = 15)
	@ApiModelProperty(value = "粉丝数")
	private Integer fansNum;
	/**点赞数*/
	@Excel(name = "点赞数", width = 15)
	@ApiModelProperty(value = "点赞数")
	private Integer goodNum;
	/**好友数*/
	@Excel(name = "好友数", width = 15)
	@ApiModelProperty(value = "好友数")
	private Integer friendNum;

	@Excel(name = "未处理的互动消息数", width = 15)
	@ApiModelProperty(value = "未处理的互动消息数")
	private Integer undealMsgNum;

	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
    private String profile;

	@ApiModelProperty(value = "封面图片")
	private String cover;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
	@Dict(dicCode = "sex")
    private String sex;
	/**出生年月*/
	@Excel(name = "出生年月", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "出生年月")
	private Date birthYm;
	/**简介*/
	@Excel(name = "简介", width = 15)
    @ApiModelProperty(value = "简介")
    private String intro;


    @ApiModelProperty(value = "姓名")
    private String name;
	/**英文姓名*/
	@Excel(name = "英文姓名", width = 15)
    @ApiModelProperty(value = "英文姓名")
    private String englishName;
	/**国家*/
	@Excel(name = "国家", width = 15, dicCode = "country")
	@Dict(dicCode = "country")
    @ApiModelProperty(value = "国家")
    private String country;
	/**年龄段*/
	@Excel(name = "年龄段", width = 15, dicCode = "ages")
	@Dict(dicCode = "ages")
    @ApiModelProperty(value = "年龄段")
    private String ages;
	/**宗教信仰*/
	@Excel(name = "宗教信仰", width = 15, dicCode = "religion")
	@Dict(dicCode = "religion")
    @ApiModelProperty(value = "宗教信仰")
    private String religion;
	/**星座*/
	@Excel(name = "星座", width = 15, dicCode = "constellation")
	@Dict(dicCode = "constellation")
    @ApiModelProperty(value = "星座")
    private String constellation;
	/**血型*/
	@Excel(name = "血型", width = 15, dicCode = "blood_type")
	@Dict(dicCode = "blood_type")
    @ApiModelProperty(value = "血型")
    private String bloodType;
	/**胸围*/
	@Excel(name = "胸围", width = 15)
    @ApiModelProperty(value = "胸围")
    private Integer chest;
	/**腰围*/
	@Excel(name = "腰围", width = 15)
    @ApiModelProperty(value = "腰围")
    private Integer waistline;
	/**臀围*/
	@Excel(name = "臀围", width = 15)
    @ApiModelProperty(value = "臀围")
    private Integer hipline;
	/**身高*/
	@Excel(name = "身高", width = 15)
    @ApiModelProperty(value = "身高")
    private Integer height;
	/**体重*/
	@Excel(name = "体重", width = 15)
    @ApiModelProperty(value = "体重")
    private Integer weight;
	/**家庭成员*/
	@Excel(name = "家庭成员", width = 15)
    @ApiModelProperty(value = "家庭成员")
    private String family;
	/**兴趣爱好*/
	@Excel(name = "兴趣爱好", width = 15)
    @ApiModelProperty(value = "兴趣爱好")
    private String interest;
	/**职业身份*/
	@Excel(name = "行业", width = 15)
	@ApiModelProperty(value = "行业")
	@Dict(dicCode = "ability")
	private String ability;
	@ApiModelProperty(value = "年收入")
	@Dict(dicCode = "income")
	private String income;
	/**职业经历*/
	@Excel(name = "职业经历", width = 15)
    @ApiModelProperty(value = "职业经历")
    private String profession;
	/**教育经历*/
	@Excel(name = "教育经历", width = 15)
    @ApiModelProperty(value = "教育经历")
    private String education;
	/**感情经历*/
	@Excel(name = "感情经历", width = 15)
    @ApiModelProperty(value = "感情经历")
    private String love;
	/**性格特点*/
	@Excel(name = "性格特点", width = 15)
    @ApiModelProperty(value = "性格特点")
    private String nature;

	@ApiModelProperty(value = "住址周围环境")
	private String addrEnvironment;

	@ApiModelProperty(value = "常去餐厅")
	private String restaurant;

	@ApiModelProperty(value = "fb帖子运营数据 google sheetid")
	private Integer sheetIdFbPostData;

	@ApiModelProperty(value = "fb好友名单数据 google sheetid")
	private Integer sheetIdFbFriendData;

	@ApiModelProperty(value = "私信数据 google sheetid")
	private Integer sheetIdFbMsgData;
	@ApiModelProperty(value = "fb爬虫管理清单 google sheetid")
	private Integer sheetIdFbCraw;

	@Dict(dicCode = "sync_status")
	@ApiModelProperty(value = "平台同步结果-个人信息")
	private String syncStatusInfo;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "本次个人信息同步开始时间")
	private Date startSyncInfoDateTime;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "上一次个人信息同步时间")
	private Date syncInfoDateTime;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "上一次养号同步时间")
	private Date syncActiveDateTime;

	@ApiModelProperty(value = "发色")
	@Dict(dicCode = "hair_color")
	private String hairColor;

	@ApiModelProperty(value = "账号类型")
	@Dict(dicCode = "account_type")
	private String accountType;

	@ApiModelProperty(value = "关联的爬虫小号")
	private String accountSmall;

	@ApiModelProperty(value = "客群")
	@Dict(dicCode = "fans_cg,code,code")
	private String cg;

	@ApiModelProperty(value = "2FA信息")
	private String fakey;

	@ApiModelProperty(value = "关联ads代理Id")
	private String agentId;
	@ApiModelProperty(value = "关联ip代理Id")
	private String proxyId;

	@ApiModelProperty(value = "是否本国号源")
	@Dict(dicCode = "yn")
	private Integer localSrc;

	@ApiModelProperty(value = "上号阶段")
	@Dict(dicCode = "mount_state")
	private String mountState;

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

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "最近换头像时间")
	private Date lastProfileDate;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "最近改名时间")
	private Date lastNameDate;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "最近改封面时间")
	private Date lastCoverDate;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "最近改简介时间")
	private Date lastIntroDate;

	@ApiModelProperty(value = "换头像同步状态")
	@Dict(dicCode = "info_state")
	private String profileInfoState;

	@ApiModelProperty(value = "改名同步状态")
	@Dict(dicCode = "info_state")
	private String nameInfoState;

	@ApiModelProperty(value = "换封面同步状态")
	@Dict(dicCode = "info_state")
	private String coverInfoState;

	@ApiModelProperty(value = "改个人简介同步状态")
	@Dict(dicCode = "info_state")
	private String introInfoState;

	@ApiModelProperty(value = "账户异常种类")
	@Dict(dicCode = "account_err")
	private String accountErr;


}
