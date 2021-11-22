package com.somanyteam.event.dto.request.question;

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
@ApiModel("根据创建时间获取问题DTO")
public class GetQuestionByCreateTimeReqDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("问题的创建时间")
    private Date createTime;
}
