package com.somanyteam.event.dto.request.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("添加或者编辑回答请求DTO")
@Data
public class AddOrUpdateAnswerReqDTO {

    @ApiModelProperty("问题id")
    private Long questionId;

//    @ApiModelProperty("父问题id")
//    private Long parentQuestion;

    @ApiModelProperty("答案id")
    private Long id;

    @ApiModelProperty("回答内容")
    private String content;

    @ApiModelProperty("答案的状态，公开回答-0 定向回答-1")
    private Byte answerStatus;


}
