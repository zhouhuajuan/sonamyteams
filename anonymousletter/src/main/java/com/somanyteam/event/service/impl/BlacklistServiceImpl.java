package com.somanyteam.event.service.impl;

import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.entity.Blacklist;
import com.somanyteam.event.mapper.BlacklistMapper;
import com.somanyteam.event.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: somanyteams
 * @description:
 * @author: 周华娟
 * @create: 2021-11-21 10:42
 **/
@Service
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Override
    public int gotBlacklisted(String complaintant, String defendant) {
        if (StrUtil.isEmpty(complaintant)||StrUtil.isEmpty(defendant)){
            //
        }
        Blacklist blacklist = blacklistMapper.gotBlacklisted(complaintant, defendant);
        return (blacklist == null ? 0 : 1);
    }
}
