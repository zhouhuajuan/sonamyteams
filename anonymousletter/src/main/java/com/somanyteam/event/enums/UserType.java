package com.somanyteam.event.enums;

import lombok.Getter;

@Getter
public enum UserType {

    NORMAL((byte)0, "普通用户"),
    MANAGER((byte)1, "管理员");

    private final Byte code;

    private final String type;

    UserType(Byte code, String type) {
        this.code = code;
        this.type = type;
    }

    public static UserType getTypeByCode(String code){
        for(UserType type: values()){
            if(code.equals(type.getCode())){
                return type;
            }
        }
        return null;
    }
}
