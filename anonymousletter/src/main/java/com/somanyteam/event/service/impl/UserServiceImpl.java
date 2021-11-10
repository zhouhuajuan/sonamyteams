package com.somanyteam.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.exception.user.UserEmailNotMatchException;
import com.somanyteam.event.exception.user.UserEnterEmptyException;
import com.somanyteam.event.exception.user.UserExistException;
import com.somanyteam.event.exception.user.UserNotExistException;
import com.somanyteam.event.mapper.UserMapper;
import com.somanyteam.event.service.UserService;


import com.somanyteam.event.util.PasswordUtil;
import com.somanyteam.event.util.RandomCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate redisTemplate;

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
    public int saveUser(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail());
        User existUser = userMapper.selectOne(wrapper);
        if (ObjectUtil.isNotNull(existUser)) {
            throw new UserExistException();
        }
        return userMapper.insert(user);
    }

    @Override
    public String sendEmail(String email) {
        int count;
        if (StrUtil.isEmpty(email)){
            throw new UserEnterEmptyException();
        }
        if(!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")){
            throw new UserEmailNotMatchException();
        }
        SimpleMailMessage msg = new SimpleMailMessage();
        String code = RandomCodeUtil.getRandom();
        msg.setSubject("匿名信邮箱验证");
        msg.setFrom("1247054987@qq.com");
        msg.setTo(email); // 设置邮件接收者，可以有多个接收者
        msg.setSentDate(new Date());
        msg.setText("hello 欢迎访问匿名信网站，您的验证码为："+code);
        try {
            javaMailSender.send(msg);
            redisTemplate.opsForValue().set("code_"+email,code);
            redisTemplate.expire("code_"+email,10, TimeUnit.MINUTES);
            count = 1;
        }catch (MailSendException e){
            System.out.println(e);
            count = 0;
        }

        if (count == 1){
            //发送成功
            return code;
        }else {
            return null;
        }
    }

    @Override
    public boolean checkCode(String email,String userCode) {
        if (StrUtil.isEmpty(email) || StrUtil.isEmpty(userCode)){
            throw new UserEnterEmptyException();
        }
        //从数据库拿到该邮箱的验证码
        String code = (String) redisTemplate.opsForValue().get("code_"+email);
        //验证码一致，返回true
        return userCode.equals(code);
    }
}
