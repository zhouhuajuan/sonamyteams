package com.somanyteam.event;

import com.somanyteam.event.util.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@SpringBootTest
class AnonymousletterApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    /**
     *  发送简单邮件
     */
    @Test
    public void sendSimpleMail() {
        SimpleMailMessage msg = new SimpleMailMessage();    //构建一个邮件对象
        msg.setSubject("这是一封测试邮件"); // 设置邮件主题
        msg.setFrom("1247054987@qq.com"); // 设置邮箱发送者
        msg.setTo("zhj15767394098@163.com"); // 设置邮件接收者，可以有多个接收者
        msg.setSentDate(new Date());    // 设置邮件发送日期
        msg.setText("这是测试邮件的正文");   // 设置邮件的正文
        javaMailSender.send(msg);
    }

    @Test
    public void sendEmail() {
        SimpleMailMessage msg = EmailUtil.sendEmail("zhj15767394098@163.com");
        javaMailSender.send(msg);
    }

    /**
     *  使用 Thymeleaf 作为邮件模板
     */
    @Test
    public void sendThymeleafMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("1247054987@qq.com");
        helper.setTo("zhj15767394098@163.com");
        helper.setSentDate(new Date());
        Context context = new Context();
        context.setVariable("code","13hi3n");
        String process = templateEngine.process("mail.html",context);
        helper.setText(process,true);
        javaMailSender.send(mimeMessage);
    }
}
