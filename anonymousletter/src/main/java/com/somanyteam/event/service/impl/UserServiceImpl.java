package com.somanyteam.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.exception.user.*;
import com.somanyteam.event.mapper.UserMapper;
import com.somanyteam.event.service.UserService;


import com.somanyteam.event.util.PasswordUtil;
import com.somanyteam.event.util.RandomCodeUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Override
    public int saveUser(User user) {
        if(!user.getEmail().matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")){
            //邮箱格式不规范
            throw new UserEmailNotMatchException();
        }

        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        boolean matches = (user.getPassword()).matches(passRegex);
        if (!matches){
            //密码不规范
            throw new PasswordNotStandardException();
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail());
        User existUser = userMapper.selectOne(wrapper);
        if (ObjectUtil.isNotNull(existUser)) {
            throw new UserExistException();
        }

        String random = RandomCodeUtil.getRandom();
        Set<String> allUserId = getAllUserId();
        if (allUserId.contains(random)){
            //已经有用户的id为random，不能给新注册的用户分配这个随机id
            throw new UserIdRepeatException();
        }

        user.setId(random); //生成一个随机数
        user.setPassword(PasswordUtil.encryptPassword(user.getId(), user.getPassword(), user.getSalt()));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return userMapper.insert(user);
    }

    @Override
    public int forgetPwd(String email, String modifyPwd) {
        if(!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")){
            //邮箱格式不规范
            throw new UserEmailNotMatchException();
        }

        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        boolean matches = modifyPwd.matches(passRegex);
        if (!matches){
            //密码不规范
            throw new PasswordNotStandardException();
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        User existUser = userMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(existUser)) {
            throw new UserNotExistException();
        }

        existUser.setPassword(PasswordUtil.encryptPassword(existUser.getUsername(),
                modifyPwd, existUser.getSalt()));
        existUser.setUpdateTime(new Date());
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email",email);
        return userMapper.update(existUser, updateWrapper);
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

    @Override
    public Set<String> getAllUserId() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        //将所有用户的id查询出来保存为list集合
        List<User> users = userMapper.selectList(wrapper);
        Set<String> set = new HashSet<>();
        for (User user : users) {
            set.add(user.getId());
        }
        return set;
    }

    @Override
    public int modifyImgUrl(String id, String imgUrl) {
        if (StrUtil.isEmpty(id) || StrUtil.isEmpty(imgUrl)){
            throw new UserEnterEmptyException();
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        User existUser = userMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(existUser)) {
            throw new UserNotExistException();
        }

        existUser.setImgUrl(imgUrl);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        return userMapper.update(existUser, updateWrapper);
    }

    @Override
    public User getUserInfo(String id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        User existUser = userMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(existUser)) {
            throw new UserNotExistException();
        }
        return existUser;
    }
}
