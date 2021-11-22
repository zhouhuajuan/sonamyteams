package com.somanyteam.event.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.entity.Blacklist;
import com.somanyteam.event.entity.Question;
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

//    @Override
//    public int gotBlacklisted(String complaintant, String defendant) {
//        QueryWrapper<Blacklist> wrapper = new QueryWrapper<>();
//        wrapper.eq("complaintant", complaintant);
//        wrapper.eq("defendant", defendant);
//        Blacklist blacklist = blacklistMapper.selectOne(wrapper);
//        return (blacklist == null ? 0 : 1);
//    }
}
