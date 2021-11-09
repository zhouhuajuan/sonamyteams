package com.somanyteam.event.controller;

import com.somanyteam.event.dto.request.user.UserLoginReqDTO;
import com.somanyteam.event.dto.result.user.UserLoginResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.enums.UserType;
import com.somanyteam.event.service.UserService;
import com.somanyteam.event.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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




}
