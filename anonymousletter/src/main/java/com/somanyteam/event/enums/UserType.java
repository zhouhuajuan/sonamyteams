package com.somanyteam.event.enums;

import lombok.Getter;

@Getter
public enum UserType {

    NORMAL("0", "普通用户"),
    MANAGER("1", "管理员");

    private final String code;

    private final String type;

    UserType(String code, String type) {
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
