package com.somanyteam.event.service;

import com.somanyteam.event.dto.result.blacklist.GetBlacklistResult;
import com.somanyteam.event.entity.User;

import java.util.List;

public interface BlacklistService {


    List<GetBlacklistResult> getBlacklist(User curUser);

    int addBlacklist(Long id,String userId);

    int deleteBlacklist(Long id,String userId);

}
