package com.somanyteam.event;

import com.somanyteam.event.util.EmailUtil;
import com.somanyteam.event.util.RandomCodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import redis.clients.jedis.Jedis;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class AnonymousletterApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

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

    /**
     * 目前邮件功能缺少：
     *  将随即生成的验证码存进redis √√√
     *  key-value：“code_1247054987@qq.com”-“KxAf42” √√√
     *  并为每个验证码设置时效
     *  key-value：“time_1247054987@qq.com”-“120” --120秒也就是两分钟，超过立即销毁这个验证码 √√√
     *
     *  如果未收到验证码，可以再次发送
     *  如果redis已经存在这个Key，则重新赋值，把验证码和时间都修改了
     *  或者直接销毁原来的，重新创建key
     */
    @Test
    public void sendEmail() {
        String email = "zhj15767394098@163.com";
        SimpleMailMessage msg = new SimpleMailMessage();    //构建一个邮件对象
        String code = RandomCodeUtil.getRandom();
        msg.setSubject("匿名信邮箱验证"); // 设置邮件主题
        msg.setFrom("1247054987@qq.com"); // 设置邮箱发送者
        msg.setTo(email); // 设置邮件接收者，可以有多个接收者
        msg.setSentDate(new Date());    // 设置邮件发送日期
        msg.setText("hello 欢迎访问匿名信网站，您的验证码为："+code);   // 设置邮件的正文
        javaMailSender.send(msg);
        redisTemplate.opsForValue().set("code_"+email,code);
        redisTemplate.expire("code_"+email,10, TimeUnit.MINUTES);
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

    @Test
    public void test(){
        //创建Jedis对象
        Jedis jedis = new Jedis("8.134.33.6",6379);

        //测试
        String value = jedis.ping();
        System.out.println(value);
        //获取
        String name = jedis.get("code_zhj15767394098@163.com");
        System.out.println(name);
    }
}
