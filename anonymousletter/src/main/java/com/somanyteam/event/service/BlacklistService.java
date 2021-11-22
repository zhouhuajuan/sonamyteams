package com.somanyteam.event.service;

public interface BlacklistService {

    int addBlacklist(Long id,String userId);

    int deleteBlacklist(Long id,String userId);
}
