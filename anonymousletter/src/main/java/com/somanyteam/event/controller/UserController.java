package com.somanyteam.event.controller;

import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.request.user.UserLoginReqDTO;
import com.somanyteam.event.dto.request.user.UserModifyPasswordReqDTO;
import com.somanyteam.event.dto.result.user.UserLoginResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.UserService;
import com.somanyteam.event.util.PasswordUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
