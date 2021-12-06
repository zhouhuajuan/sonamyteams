package com.somanyteam.event.dto.result.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("返回公开父问题的结果集")
public class GetPublicQuestionResult {

    @ApiModelProperty("公开父问题")
    private List<VariousQuestionsListResult> listResults;

    @ApiModelProperty("查看别人提问箱返回的用户信息")
    private UserInfoResult userInfo;

}
