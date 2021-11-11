package com.somanyteam.event.controller;

import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.request.user.UserLoginReqDTO;
import com.somanyteam.event.dto.request.user.UserModifyPasswordReqDTO;
import com.somanyteam.event.dto.result.user.UserLoginResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.UserService;
import com.somanyteam.event.util.PasswordUtil;
import cn.hutool.core.util.ObjectUtil;
import com.somanyteam.event.dto.request.user.UserForgetPwdReqDTO;
import com.somanyteam.event.dto.request.user.UserRegisterReqDTO;
import com.somanyteam.event.exception.user.ConfirmPwdNotMatchException;
import com.somanyteam.event.exception.user.UserEnterEmptyException;
import com.somanyteam.event.exception.user.VerifyCodeNotMatchException;
import com.somanyteam.event.util.RandomCodeUtil;
import com.somanyteam.event.util.ResponseMessage;
import com.somanyteam.event.util.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;

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
//        String rightCode = (String) session.getAttribute("code");
//
//        if(!rightCode.equals(userLoginReqDTO.getCode())){
//            throw new VerifyCodeNotMatchException();
//        }
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

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public ResponseMessage register(@RequestBody @Validated UserRegisterReqDTO userRegisterReqDTO) {
        User user = new User();
        if (ObjectUtil.isEmpty(userRegisterReqDTO)){
            //输入为空
            throw new UserEnterEmptyException();
        }

        boolean equals = userRegisterReqDTO.getPassword().equals(userRegisterReqDTO.getConfirmPwd());
        if (!equals){
            //确认密码和密码不一致
            throw new ConfirmPwdNotMatchException();
        }

        boolean b = userService.checkCode(userRegisterReqDTO.getEmail(), userRegisterReqDTO.getCode());
        if (!b){
            //验证码错误
            throw new VerifyCodeNotMatchException();
        }

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
    }

    //82f3fbee2649b2f9a28b3a421f3bda97 修改前密码
    //f28e3f6beb5cdbe31f73eba5d612682c 修改后密码
    @ApiOperation(value = "忘记密码")
    @PostMapping("/forgetPwd")
    public ResponseMessage forgetPwd(@RequestBody @Validated UserForgetPwdReqDTO userForgetPwdReqDTO) {
        if (ObjectUtil.isEmpty(userForgetPwdReqDTO)){
            //输入为空
            throw new UserEnterEmptyException();
        }

        boolean equals = userForgetPwdReqDTO.getModifyPwd().equals(userForgetPwdReqDTO.getConfirmPwd());
        if (!equals){
            //确认密码和修改密码不一致
            throw new ConfirmPwdNotMatchException();
        }

        boolean b = userService.checkCode(userForgetPwdReqDTO.getEmail(), userForgetPwdReqDTO.getCode());
        if (!b){
            //验证码错误
            throw new VerifyCodeNotMatchException();
        }

        int i = userService.forgetPwd(userForgetPwdReqDTO.getEmail(), userForgetPwdReqDTO.getModifyPwd());
        if (i > 0) {
            return ResponseMessage.newSuccessInstance("找回密码成功");
        } else {
            return ResponseMessage.newErrorInstance("找回密码失败");
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

    @ApiOperation("修改密码")
    @PostMapping("/modifyPassword")
    public ResponseMessage modifyPassword(@RequestBody UserModifyPasswordReqDTO modifyPasswordReqDTO) throws ParseException {
        String originalPassword = modifyPasswordReqDTO.getOriginalPassword();
        String newPassword = modifyPasswordReqDTO.getNewPassword();
        String confirmPassword = modifyPasswordReqDTO.getConfirmPassword();
        System.out.println("旧密码:" + originalPassword);

        if(StrUtil.isEmpty(originalPassword) || StrUtil.isEmpty(newPassword) || StrUtil.isEmpty(confirmPassword)){
           return ResponseMessage.newErrorInstance("参数不能为空");
        }
        if(!newPassword.equals(confirmPassword)){
            return ResponseMessage.newErrorInstance("新密码和确认密码不一致");
        }
        //匹配8-16位，至少有一个大写字母和一个数字，不能有三个相同的字符，特殊字符包括
        if(!newPassword.matches("^(?=.*[A-Z])(?=.*[0-9])(?!.*([~!@&%$^\\(\\)#_]).*\\1.*\\1)[a-zA-Z0-9.~!@\";|:`&%$^\\(\\)#_]{8,16}$")){
            return ResponseMessage.newErrorInstance("新密码不符合格式要求");
        }
        Subject subject = SecurityUtils.getSubject();
        User curUser = (User) subject.getPrincipal();

        String encryptOldPwd = PasswordUtil.encryptPassword(curUser.getEmail(), originalPassword, curUser.getSalt());
        if(curUser.getPassword().equals(encryptOldPwd)){
            return ResponseMessage.newErrorInstance("旧密码不正确");
        }

        Integer update = userService.modifyPassword(curUser, newPassword);
        if(update >= 1){
            System.out.println("新密码:" + newPassword);
            //把新密码放入当用户信息里
            curUser.setPassword(encryptOldPwd);
           return ResponseMessage.newSuccessInstance("修改成功");
        }else{
           return ResponseMessage.newErrorInstance("修改失败");
        }

    }

    @ApiOperation("注销账号")
    public ResponseMessage deleteAccount(){
        return null;
    }

}
