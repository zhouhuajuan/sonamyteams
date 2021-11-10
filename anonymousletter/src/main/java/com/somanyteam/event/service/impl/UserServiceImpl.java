package com.somanyteam.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.exception.user.UserEmailNotMatchException;
import com.somanyteam.event.exception.user.UserEnterEmptyException;
import com.somanyteam.event.exception.user.UserNotExistException;
import com.somanyteam.event.mapper.UserMapper;
import com.somanyteam.event.service.UserService;


import com.somanyteam.event.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String email, String password) {

        if(StrUtil.isEmpty(email) || StrUtil.isEmpty(password)){
            throw new UserEnterEmptyException();
        }
        if(!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")){
            throw new UserEmailNotMatchException();
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        User user = userMapper.selectOne(wrapper);

        if(ObjectUtil.isNull(user)){
            throw new UserNotExistException();
        }
        PasswordUtil.matches(user, password);
        return user;
    }

    @Override
    public Integer modifyPassword(User curUser, String newPassword) throws ParseException {
//        String encryptOldPwd = PasswordUtil.encryptPassword(curUser.getEmail(), userModifyPasswordReqDTO.getOriginalPassword(), curUser.getSalt());
//        if(!encryptOldPwd.equals(curUser.getPassword())){
//            throw new UserOldPwdNotMatchException();
//        }
        String encryptNewPwd = PasswordUtil.encryptPassword(curUser.getEmail(), newPassword, curUser.getSalt());
        User user = new User();
        user.setId(curUser.getId());
        user.setPassword(encryptNewPwd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(new Date()));
        user.setUpdateTime(date);
        //        if(update >= 1) {
//            curUser.setPassword(encryptNewPwd);
//        }
        return userMapper.updateById(user);
    }
}
