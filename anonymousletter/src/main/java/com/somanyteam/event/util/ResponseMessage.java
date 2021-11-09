package com.somanyteam.event.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 统一返回结果集
 * @param <T>
 */
@ApiModel("统一返回结果集(json)")
public class ResponseMessage<T> {
    // 成功返回状态码
    public static final int STATUS_SUCCESS = 200;
    // 失败返回状态码
    public static final int STATUS_ERROR = 400;

    @ApiModelProperty(value = "状态码")
    private int status;
    @ApiModelProperty(value = "数据对象")
    private T data;
    @ApiModelProperty(value = "描述")
    private String message;
    @ApiModelProperty(value = "时间戳")
    private Long timestamp;

    public ResponseMessage(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public static ResponseMessage newSuccessInstance(Object data, String message) {
        return new ResponseMessage(ResponseMessage.STATUS_SUCCESS, data, message);
    }

    public static ResponseMessage newSuccessInstance(String message) {
        return new ResponseMessage(ResponseMessage.STATUS_SUCCESS, null, message);
    }

    public static ResponseMessage newSuccessInstance(Object data) {
        return new ResponseMessage(ResponseMessage.STATUS_SUCCESS, data, null);
    }

    public static ResponseMessage newErrorInstance(String message) {
        return new ResponseMessage(ResponseMessage.STATUS_ERROR, null, message);
    }

    public static ResponseMessage newErrorInstance(Object data, int status) {
        return new ResponseMessage(status, data, null);
    }

    public static int getStatusSuccess() {
        return STATUS_SUCCESS;
    }

    public static int getStatusError() {
        return STATUS_ERROR;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}