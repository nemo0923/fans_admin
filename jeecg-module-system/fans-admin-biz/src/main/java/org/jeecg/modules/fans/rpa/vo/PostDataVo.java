package org.jeecg.modules.fans.rpa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class PostDataVo {
    @ApiModelProperty(value = "点赞数")
    private Integer goodNum;
    /**评论数*/
    @ApiModelProperty(value = "评论数")
    private Integer commentNum;
    /**分享数*/
    @ApiModelProperty(value = "分享数")
    private Integer shareNum;

    @ApiModelProperty(value = "帖子在平台的访问url")
    private String postUrl;

    @JsonFormat(timezone = "GMT+0",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上一次运营数据同步时间")
    private Date syncPostDataDateTime;
}
