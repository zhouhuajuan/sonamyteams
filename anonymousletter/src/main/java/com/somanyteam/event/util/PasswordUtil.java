package com.somanyteam.event.util;

import com.somanyteam.event.entity.User;
import com.somanyteam.event.exception.user.UserPasswordNotMatchException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

public class PasswordUtil {

    /**
     * 密码校验
     * @param user
     * @param password
     */
    public static void validate(User user, String password) {
        if (!matches(user, password)) {
            throw new UserPasswordNotMatchException();
        }
    }

    /**
     * 密码校验判断
     * @param user
     * @param password
     * @return
     */
    public static boolean matches (User user, String password) {
        return user.getPassword().equals(encryptPassword(user.getId(), password, user.getSalt()));
    }

    /**
     * 明文密码加密
     * @param userName
     * @param password
     * @param salt
     * @return
     */
    public static String encryptPassword(String userName, String password, String salt) {
        return new Md5Hash(userName + password + salt).toHex();
    }

    /**
     * 生成随机盐
     * (用于创建账号)
     */
    public static String randomSalt() {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(3).toHex();
    }
}
