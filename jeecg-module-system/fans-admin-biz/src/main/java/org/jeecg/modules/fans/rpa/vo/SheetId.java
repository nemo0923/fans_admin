package org.jeecg.modules.fans.rpa.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SheetId {
    @ApiModelProperty(value = "fb帖子运营数据 google sheetid")
    private Integer sheetIdFbPostData;

    @ApiModelProperty(value = "fb好友名单数据 google sheetid")
    private Integer sheetIdFbFriendData;

    @ApiModelProperty(value = "私信数据 google sheetid")
    private Integer sheetIdFbMsgData;
}
