package org.jeecg.modules.fans.rpa.vo;

import lombok.Data;

@Data
public class SearchFansVo {
    private String searchUrl;
    private Integer beginNum;
    private Integer beginPage;
    private Integer endPage;
    private String time0;
}
