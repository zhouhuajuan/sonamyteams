package com.somanyteam.event.dto.result.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: somanyteams
 * @description: 未回答问题的结果集
 * @author: 周华娟
 * @create: 2021-11-20 10:06
 **/
@Data
@ApiModel("未回答问题的结果")
public class VariousQuestionsListResult implements Comparable<VariousQuestionsListResult>{

    @ApiModelProperty("问题id")
    private Long id;

    @ApiModelProperty("问题内容")
    private String content;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("问题的更新时间")
    private Date updateTime;

    @ApiModelProperty("new标志位，默认为0，在已回答接口接口中，若为1，则表示父问题下存在未回答子问题，若为0，表示父问题及子问题全部已回答")
    private Byte newFlag;

    @Override
    public int compareTo(VariousQuestionsListResult o) {
        long anotherTime = o.getUpdateTime().getTime();
        long thisTime = updateTime.getTime();

        return (thisTime>anotherTime ? -1 : (thisTime==anotherTime ? 0 : 1));
    }
}
