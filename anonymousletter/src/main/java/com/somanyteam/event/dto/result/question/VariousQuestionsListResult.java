package com.somanyteam.event.dto.result.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: somanyteams
 * @description: 未回答问题的结果集
 * @author: 周华娟
 * @create: 2021-11-20 10:06
 **/
@Data
@ApiModel("未回答问题的结果")
public class VariousQuestionsListResult {

    @ApiModelProperty("问题id")
    private String id;

    @ApiModelProperty("问题内容")
    private String content;

    @ApiModelProperty("问题的更新时间")
    private Date updateTime;
}
