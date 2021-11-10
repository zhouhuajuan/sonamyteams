package com.somanyteam.event.controller;

import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.request.user.UserLoginReqDTO;
import com.somanyteam.event.dto.request.user.UserRegisterReqDTO;
import com.somanyteam.event.dto.result.user.UserLoginResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.exception.user.VerifyCodeNotMatchException;
import com.somanyteam.event.service.UserService;
import com.somanyteam.event.util.PasswordUtil;
import com.somanyteam.event.util.RandomCodeUtil;
import com.somanyteam.event.util.ResponseMessage;
import com.somanyteam.event.util.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author zbr
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public ResponseMessage<UserLoginResult> login(@RequestBody @Validated UserLoginReqDTO userLoginReqDTO, HttpSession session){
        String rightCode = (String) session.getAttribute("code");

        if(!rightCode.equals(userLoginReqDTO.getCode())){
            throw new VerifyCodeNotMatchException();
        }
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token  = new UsernamePasswordToken(userLoginReqDTO.getEmail(), userLoginReqDTO.getPassword());
        if(userLoginReqDTO.getRememberMe() != null && userLoginReqDTO.getRememberMe() == 1) {
            token.setRememberMe(true);
        }
        subject.login(token);

        User loginUser = (User) subject.getPrincipal();
        UserLoginResult result = new UserLoginResult();
        BeanUtils.copyProperties(loginUser, result);

        return ResponseMessage.newSuccessInstance(result, "登录成功");
    }

    //ViPJRC
    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public ResponseMessage register(@RequestBody @Validated UserRegisterReqDTO userRegisterReqDTO) {
        User user = new User();
        boolean b = userService.checkCode(userRegisterReqDTO.getEmail(), userRegisterReqDTO.getCode());
        if (b) {
            //true，验证码一致
            BeanUtils.copyProperties(userRegisterReqDTO, user); //邮箱，密码
            user.setId(RandomCodeUtil.getRandom()); //随机生成一个随机数
            user.setUsername("偷心盗贼");
            user.setIdentity(0);
            user.setSalt(PasswordUtil.randomSalt());
            user.setImgUrl("https://8.134.33.6/photo/a.png");//默认头像图片链接

            if (userService.saveUser(user) > 0) {
                return ResponseMessage.newSuccessInstance("注册成功");
            } else {
                return ResponseMessage.newErrorInstance("注册失败");
            }
        } else {
            //false，验证码不一致
            return ResponseMessage.newErrorInstance("验证码错误，注册失败");
        }
    }

    @ApiOperation(value = "发送邮件")
    @PostMapping("/sendEmail")
    public ResponseMessage sendEmail(String email) {
        //拿到验证码，为空则发送邮件失败
        String code = userService.sendEmail(email);
        if (StrUtil.isEmpty(code)) {
            return ResponseMessage.newErrorInstance("发送失败");
        } else {
            //返回前端验证码，前端判断验证码是否输入正确
            return ResponseMessage.newSuccessInstance("发送成功",code);
        }
    }

    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public ResponseMessage logout(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            subject.logout();
            return ResponseMessage.newSuccessInstance("登出成功");
        }else{
            return ResponseMessage.newErrorInstance("登出失败");

        }
    }

    @ApiOperation("获取验证码")
    @GetMapping("/getImage")
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException, IOException {
        //生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //验证码放入session
        session.setAttribute("code",code);
        //验证码存入图片
        ServletOutputStream os = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(220,60,os,code);
    }

}
