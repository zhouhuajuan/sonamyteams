package com.somanyteam.event.shiro;

import com.somanyteam.event.entity.User;

import com.somanyteam.event.enums.UserType;
import com.somanyteam.event.exception.user.UserBlockedException;
import com.somanyteam.event.exception.user.UserEmailNotMatchException;
import com.somanyteam.event.exception.user.UserEnterEmptyException;
import com.somanyteam.event.exception.user.UserNotExistException;
import com.somanyteam.event.exception.user.UserPasswordNotMatchException;
import com.somanyteam.event.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author zbr
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(com.somanyteam.event.shiro.UserRealm.class);
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("进入授权流程");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UsernamePasswordToken token = (UsernamePasswordToken) principalCollection.getPrimaryPrincipal();
        User user = (User) token.getPrincipal();
        authorizationInfo.addRole(UserType.getTypeByCode(user.getIdentity()).getType());
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("进入认证流程");
        System.out.println("进入认证流程");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String email = token.getUsername();
        String password = "";
        if(token.getPassword() != null){
            password = new String(token.getPassword());
        }

        User user = null;
        try {
            user = userService.login(email, password);
        } catch (UserEnterEmptyException | UserNotExistException | UserEmailNotMatchException e) {
            throw new UnknownAccountException(e.getMessage(), e);
        } catch (UserPasswordNotMatchException e) {
            throw new IncorrectCredentialsException(e.getMessage(), e);
        } catch (UserBlockedException e) {
            throw new LockedAccountException(e.getMessage(), e);
        } catch (Exception e) {
            logger.error("用户{}登录验证未通过: {}", email, e.getMessage());
            throw new AuthenticationException(e.getMessage(), e);
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }
}
