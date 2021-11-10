package com.somanyteam.event.controller;

import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.request.user.UserLoginReqDTO;
import com.somanyteam.event.dto.request.user.UserRegisterReqDTO;
import com.somanyteam.event.dto.result.user.UserLoginResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.UserService;
import com.somanyteam.event.util.PasswordUtil;
import com.somanyteam.event.util.RandomCodeUtil;
import com.somanyteam.event.util.ResponseMessage;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


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
    public ResponseMessage<UserLoginResult> login(@RequestBody @Validated UserLoginReqDTO userLoginReqDTO){

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token  = new UsernamePasswordToken(userLoginReqDTO.getEmail(), userLoginReqDTO.getPassword());
        subject.login(token);

        User loginUser = (User) subject.getPrincipal();
        UserLoginResult result = new UserLoginResult();
        BeanUtils.copyProperties(loginUser, result);

        return ResponseMessage.newSuccessInstance(result, "登录成功");
    }

    /**
     * 1.先发送邮件
     *      保存验证码进redis，并设置过期时间
     * 2.注册账号
     *      在service层验证 code是否一致
     *      邮箱，用户id（随机生成），密码
     *      验证码不一致不通过，为空也不通过
     *      必要选项每天也不通过
     *      密码不符合标准也不通过
     */
    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public ResponseMessage register(@RequestBody @Validated UserRegisterReqDTO userRegisterReqDTO,String userCode) {
        User user = new User();
        boolean b = userService.checkCode(userRegisterReqDTO.getEmail(), userCode);
        if (b){
            //true，验证码一致
            BeanUtils.copyProperties(userRegisterReqDTO, user);
            user.setSalt(PasswordUtil.randomSalt());
            // 原始密码默认与账号名称相同
            user.setPassword(PasswordUtil.encryptPassword(user.getUsername(), user.getUsername(), user.getSalt()));
            user.setIdentity(0);
            user.setId(RandomCodeUtil.getRandom()); //随机生成一个随机数
            user.setImgUrl("http://8.134.33.6/photo/a.png");//默认头像图片链接
            if (userService.saveUser(user) > 0) {
                return ResponseMessage.newSuccessInstance("注册成功");
            } else {
                return ResponseMessage.newErrorInstance("注册失败");
            }
        }else {
            //false，验证码不一致
            return ResponseMessage.newErrorInstance("验证码错误，注册失败");
        }
    }

    @ApiOperation(value = "发送邮件")
    @PostMapping("/sendEmail")
    public ResponseMessage sendEmail(@ApiParam("邮箱") @Param(value = "email") String email) {
        //拿到验证码，为空则发送邮件失败
        String code = userService.sendEmail(email);
        if (StrUtil.isEmpty(code)) {
            return ResponseMessage.newErrorInstance("发送失败");
        } else {
            //返回前端验证码，前端判断验证码是否输入正确
            return ResponseMessage.newSuccessInstance("发送成功",code);
        }
    }

}
