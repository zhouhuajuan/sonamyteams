package com.somanyteam.event.test;

import com.somanyteam.event.entity.User;
import com.somanyteam.event.mapper.UserMapper;
import com.somanyteam.event.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileReader;

/**
 * @program: somanyteams
 * @description:
 * @author: 周华娟
 * @create: 2021-11-10 20:09
 **/
@SpringBootTest
public class PasswordTest {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void test(){
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        String passRegex1 = "/^\\w{6,16}$/"; //wrong
        String password="benbanv";
        boolean matches = password.matches(passRegex);
        System.out.println(matches);
    }

    @Test
    public void test2(){
        String id = "12345678910";
        String email = "1234567@123.com";
        String pwd = "123456Aa";
        String salt = PasswordUtil.randomSalt();
        User user = new User();
        user.setPassword(PasswordUtil.encryptPassword(id, pwd, salt));
        user.setId(id);
        user.setEmail(email);
        user.setSalt(salt);
        userMapper.insert(user);

    }

    @Test
    public void test3(){
        String s = "123a~";
        String reg = ".*[!`~';.,/?@$%^&*()-+=]{1,}.*";
        System.out.println(s.matches(reg));
    }
}
