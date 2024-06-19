package org.jeecg.modules.fans.rpa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class FansVo {
    @ApiModelProperty(value = "好友名称")
    private String fansNickname;
    @ApiModelProperty(value = "好友个人主页")
    private String fansLink;
    @ApiModelProperty(value = "好友头像")
    private String fansProfile;
    @JsonFormat(timezone = "GMT+0",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上一次好友名单同步时间")
    private Date syncFansDataDateTime;
}
