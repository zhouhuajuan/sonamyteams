package com.somanyteam.event.dto.result.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: somanyteams
 * @description: 获取问题和答案结果集
 * @author: 周华娟
 * @create: 2021-11-21 22:56
 **/
@Data
@ApiModel("获取问题和答案结果集")
public class QuestionAndAnswerResult {

    @ApiModelProperty(value = "所有问题", required = true)
    private List<QuestionResult> allQuestion;

    @ApiModelProperty(value = "所有答案", required = true)
    private List<AnswerResult> allAnswer;
}
