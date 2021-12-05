package com.somanyteam.event.service;

import com.somanyteam.event.dto.request.user.UserModifyPasswordReqDTO;
import com.somanyteam.event.dto.request.user.UserUpdateInfoReqDTO;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.shiro.AccountProfile;
import org.apache.shiro.subject.Subject;


import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface UserService {

    User login(String email, String password);

    Integer modifyPassword(AccountProfile curUser, UserModifyPasswordReqDTO dto) throws ParseException;

    int saveUser(User user);

    int forgetPwd(String email,String modifyPwd);

    int sendEmail(String email,String content);

    boolean checkCode(String email,String userCode);

    Set<String> getAllUserId();

    int modifyImgUrl(String id,String imgUrl);

    User getUserInfo(String id) throws ParseException;

    boolean deleteAccount(User user);

    boolean updateInfo(UserUpdateInfoReqDTO dto, User curUser) throws ParseException;

    User getById(String id);

    User getByEmail(String email);
}
