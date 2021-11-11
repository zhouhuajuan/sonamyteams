package com.somanyteam.event.config;

import com.somanyteam.event.shiro.ShiroLoginFilter;
import com.somanyteam.event.shiro.UserRealm;
import com.somanyteam.event.shiro.cache.RedisCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * shiro过滤器配置
     */
    @Bean
    public ShiroFilterFactoryBean ShiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 添加自定义过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        // filters.put("perms", new ShiroPermissionFilter());
        filters.put("authc", new ShiroLoginFilter());
        shiroFilterFactoryBean.setFilters(filters);
        // 添加shiro过滤链
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    /**
     * shiro过滤链配置
     */
    private LinkedHashMap<String, String> filterChainDefinitionMap() {
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        // 开发所有接口便于测试
        // filterMap.put("/user/**", "anon");
        // filterMap.put("/report/**", "anon");
        // 放行登陆接口
        filterMap.put("/user/login", "anon");
        filterMap.put("/user/sendEmail", "anon");
        filterMap.put("/user/register", "anon");
        filterMap.put("/user/forgetPwd", "anon");
        filterMap.put("/user/getAllUserId", "anon");
        // 放行druid进行测试
        filterMap.put("/druid/**", "anon");
        // 设置退出登录，shiro清除session
        filterMap.put("/user/logout", "anon");
        // 设置swagger-ui无需认证
        filterMap.put("/swagger-ui/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/v3/api-docs/**", "anon");
        filterMap.put("/webjars/springfox-swagger-ui/**", "anon");

        filterMap.put("/doc.html", "anon");
        filterMap.put("/doc.html#/**", "anon");
        filterMap.put("/webjars/**", "anon");
        //设置用户放行
        //注销账户需登录后才能注销，cookie状态下不能注销
        filterMap.put("/user/deleteAccount", "authc");
        filterMap.put("/user/**", "user");
        // 设置所有接口需要认证访问
        filterMap.put("/**", "authc");
//        filterMap.put("/**", "anon");
        return filterMap;
    }
    /**
     * 自定义Realm
     */
   @Bean
   public UserRealm userRealm(){
       UserRealm userRealm = new UserRealm();
       //开启缓存管理
//       userRealm.setCacheManager(new RedisCacheManager());
//       userRealm.setCachingEnabled(true);//开启全局缓存
//       userRealm.setAuthenticationCachingEnabled(true);//认证认证缓存
//       userRealm.setAuthenticationCacheName("authenticationCache");
//       userRealm.setAuthorizationCachingEnabled(true);//开启授权缓存
//       userRealm.setAuthorizationCacheName("authorizationCache");
       return userRealm;
   }

    /**
     * 安全管理器
     */
    @Bean
    public SecurityManager securityManager(UserRealm userRealm, CookieRememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
//        securityManager.setCacheManager(ehCacheManager());ehCacheManager
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    /**
     * 开启shiro注解通知器
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
