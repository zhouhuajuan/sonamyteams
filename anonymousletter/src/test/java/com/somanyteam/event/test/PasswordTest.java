package com.somanyteam.event.test;

import org.junit.jupiter.api.Test;

/**
 * @program: somanyteams
 * @description:
 * @author: 周华娟
 * @create: 2021-11-10 20:09
 **/
public class PasswordTest {
    @Test
    public void test(){
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        String passRegex1 = "/^\\w{6,16}$/"; //wrong
        String password="benbanv";
        boolean matches = password.matches(passRegex);
        System.out.println(matches);
    }
}
