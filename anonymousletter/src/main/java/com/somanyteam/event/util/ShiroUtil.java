package com.somanyteam.event.util;

import com.somanyteam.event.entity.User;
import com.somanyteam.event.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;

public class ShiroUtil {

    public static User getUser() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        User loginUser = new User();
        BeanUtils.copyProperties(principal, loginUser);
        return loginUser;
    }

}