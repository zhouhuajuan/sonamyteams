package com.somanyteam.event.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.dto.request.user.UserLoginReqDTO;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.UserService;
import com.somanyteam.event.shiro.AccountProfile;
import com.somanyteam.event.util.JwtUtils;
import com.somanyteam.event.util.PasswordUtil;
import com.somanyteam.event.util.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    @ApiOperation("登录")
    public Result login(@Validated @RequestBody UserLoginReqDTO dto, HttpServletResponse response) {
        User user = userService.getByEmail(dto.getEmail());
        Assert.notNull(user, "用户不存在");//断言拦截
        //判断账号密码是否错误 因为是md5加密所以这里md5判断
        if (!user.getPassword().equals(PasswordUtil.encryptPassword(user.getId(), dto.getPassword(), user.getSalt()))) {
            //密码不同则抛出异常
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());

        //将token 放在我们的header里面
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        Subject subject = SecurityUtils.getSubject();
        boolean authenticated = subject.isAuthenticated();
        System.out.println(authenticated);
        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("imgUrl", user.getImgUrl())
                .put("email", user.getEmail()).map()

        );
    }

    //需要认证权限才能退出登录
    @RequiresAuthentication
    @GetMapping("/logout")
    @ApiOperation("登出")
    public Result logout() {
        System.out.println("=========");
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.isAuthenticated());
        AccountProfile accountProfile = (AccountProfile) subject.getPrincipal();
        System.out.println(accountProfile);
        //退出登录
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }
}