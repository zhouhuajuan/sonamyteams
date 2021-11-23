package com.somanyteam.event.Interceptor;

import cn.hutool.json.JSONUtil;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.enums.UserType;
import com.somanyteam.event.util.ResponseMessage;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserTypeInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserTypeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("uri是:{}", request.getRequestURI());

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        logger.info("身份是:{}", UserType.getTypeByCode(user.getIdentity()));
//        String userType = user.get();
        if(user.getIdentity().equals(UserType.MANAGER.getCode())) {
            return true;
        }
        response.setHeader("Access-Control-Allow-Origin",  request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json; charset=utf-8");

        try {
            response.getWriter().print(JSONUtil.toJsonStr(ResponseMessage.newErrorInstance("非管理员账户")));
            response.flushBuffer();
        } catch (Exception e) {
        }
        return false;
    }
}
