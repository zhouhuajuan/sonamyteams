package com.somanyteam.event.service;

import com.somanyteam.event.dto.request.user.UserModifyPasswordReqDTO;
import com.somanyteam.event.entity.User;

import java.text.ParseException;

public interface UserService {

    User login(String email, String password);

    Integer modifyPassword(User curUser, String newPassword) throws ParseException;
}
