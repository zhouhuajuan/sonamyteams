package com.somanyteam.event.service;

import com.somanyteam.event.entity.User;

public interface UserService {

    User login(String email, String password);

    int saveUser(User user);

    int forgetPwd(String email,String modifyPwd);

    String sendEmail(String email);

    boolean checkCode(String email,String userCode);
}
