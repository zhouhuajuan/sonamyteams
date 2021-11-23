package com.somanyteam.event.enums;

import lombok.Getter;

@Getter
public enum ReportStatusEnums {
    NOT_HANDLE((byte)0, "未处理"),
    PASS((byte)1, "举报通过"),
    NOT_PASS((byte)2, "举报未通过");

    private final Byte code;

    private final String type;

    ReportStatusEnums(Byte code, String type) {
        this.code = code;
        this.type = type;
    }

    public static ReportStatusEnums getTypeByCode(Byte code){
        for(ReportStatusEnums type: values()){
            if(code.equals(type.getCode())){
                return type;
            }
        }
        return null;
    }
}
