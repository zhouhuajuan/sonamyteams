package com.somanyteam.event.enums;

import lombok.Getter;

@Getter
public enum ReportTypeEnums {
    VIOLENCE((byte)0, "低俗暴力色情"),
    POLITICS((byte)1, "政治敏感内容"),
    ASSAULT((byte)2, "人身攻击"),
    OTHER((byte)3, "其他");


    private final Byte code;

    private final String type;

    ReportTypeEnums(Byte code, String type) {
        this.code = code;
        this.type = type;
    }

    public static ReportTypeEnums getTypeByCode(Byte code){
        for(ReportTypeEnums type: values()){
            if(code.equals(type.getCode())){
                return type;
            }
        }
        return null;
    }

}
