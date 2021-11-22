package com.somanyteam.event.util;

import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.exception.user.UserEmailNotMatchException;
import com.somanyteam.event.exception.user.UserEnterEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @program: somanyteams
 * @description: 发送邮件工具类
 * @author: 周华娟
 * @create: 2021-11-09 09:58
 **/
@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    // 发送邮件
    public int sendEmail(String email,String content) {
        //JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        if (StrUtil.isEmpty(email) || StrUtil.isEmpty(content)){
            throw new UserEnterEmptyException();
        }
        if(!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")){
            throw new UserEmailNotMatchException();
        }

        int count;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setSubject("匿名信邮箱验证");
        msg.setFrom("1247054987@qq.com");
        msg.setTo(email); // 设置邮件接收者，可以有多个接收者
        msg.setSentDate(new Date());
        msg.setText(content);
        try {
            javaMailSender.send(msg);
            count = 1;
        }catch (MailSendException e){
            System.out.println(e);
            count = 0;
        }
        return count;
    }

}
