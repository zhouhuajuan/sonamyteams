package com.somanyteam.event.service;

import com.somanyteam.event.entity.User;

import java.text.ParseException;
import java.util.List;

public interface UserService {

    User login(String email, String password);

    Integer modifyPassword(User curUser, String newPassword) throws ParseException;

    int saveUser(User user);

    int forgetPwd(String email,String modifyPwd);

    String sendEmail(String email);

    boolean checkCode(String email,String userCode);

    List<String> getAllUserId();

}
