package com.somanyteam.event.dto.result.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: somanyteams
 * @description: 添加问题请求DTO
 * @author: 周华娟
 * @create: 2021-11-21 10:18
 **/
@Data
@ApiModel("回答结果集")
public class AnswerResult {

    @ApiModelProperty(value = "回答内容", required = true)
    private String content;

    @ApiModelProperty("回答id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("问题的更新时间")
    private Date updateTime;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("问题id")
    private Long questionId;
}
