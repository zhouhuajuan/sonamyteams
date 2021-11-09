package com.somanyteam.event.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: somanyteams
 * @description: 发送邮件工具类
 * @author: 周华娟
 * @create: 2021-11-09 09:58
 **/
public class EmailUtil {

    // 发送邮件
    public static SimpleMailMessage sendEmail(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();    //构建一个邮件对象
        String code = RandomCodeUtil.getRandom();
        msg.setSubject("匿名信邮箱验证"); // 设置邮件主题
        msg.setFrom("1247054987@qq.com"); // 设置邮箱发送者
        msg.setTo(email); // 设置邮件接收者，可以有多个接收者
        msg.setSentDate(new Date());    // 设置邮件发送日期
        msg.setText("hello 欢迎访问匿名信网站，您的验证码为："+code);   // 设置邮件的正文
        return msg;
    }

}
