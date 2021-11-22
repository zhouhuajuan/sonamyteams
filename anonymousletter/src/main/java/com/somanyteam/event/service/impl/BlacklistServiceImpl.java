package com.somanyteam.event.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.entity.Blacklist;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.exception.blacklist.BlacklistEnterEmptyException;
import com.somanyteam.event.mapper.BlacklistMapper;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

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

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public int addBlacklist(Long id,String userId) {
        if (id == null || StrUtil.isEmpty(userId)){
            throw new BlacklistEnterEmptyException();
        }
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("a_id",userId);
        Question question = questionMapper.selectOne(wrapper);
        Blacklist blacklist = new Blacklist();
        blacklist.setComplaintant(userId);
        blacklist.setDefendant(question.getQId());
        blacklist.setQuestionId(id);
        blacklist.setCreateTime(new Date());
        return blacklistMapper.insert(blacklist);
    }

    @Override
    public int deleteBlacklist(Long id,String userId) {
        if (id == null || StrUtil.isEmpty(userId)){
            throw new BlacklistEnterEmptyException();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("question_id", id);
        map.put("complaintant", userId);
        return blacklistMapper.deleteByMap(map);
    }
}
