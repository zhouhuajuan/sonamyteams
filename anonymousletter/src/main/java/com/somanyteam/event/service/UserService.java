package com.somanyteam.event.service;

import com.somanyteam.event.entity.User;

public interface UserService {

    User login(String email, String password);
}
