package com.somanyteam.event.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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
        // 设置所有接口需要认证访问
//        filterMap.put("/**", "authc");
        return filterMap;
    }
    /**
     * 自定义Realm
     */
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    /**
     * 安全管理器
     */
    @Bean
    public SecurityManager securityManager(UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }
}
