package com.somanyteam.event.dto.request.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @program: somanyteams
 * @description: 添加问题请求DTO
 * @author: 周华娟
 * @create: 2021-11-21 10:18
 **/
@Data
@ApiModel("添加问题DTO")
public class QuestionAddReqDTO {

    @ApiModelProperty(value = "被提问者id", required = true)
    @NotBlank(message = "被提问者id不能为空")
    private String aId;

    @ApiModelProperty(value = "问题内容", required = true)
    private String content;

    @ApiModelProperty(value = "父问题id", required = true)
    private Long parentQuestion;
}
