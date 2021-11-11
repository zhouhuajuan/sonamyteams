package com.somanyteam.event.dto.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel("用户信息更新请求DTO")
public class UserUpdateInfoReqDTO {

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "性别,0-男 1-女 可不填", required = true)
    private Integer sex;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生日 格式为yyyy-MM-dd",required = true)
    private Date birthday;

    @ApiModelProperty(value = "简介", required = true)
    private String profile;



}
