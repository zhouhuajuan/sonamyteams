package com.somanyteam.event.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.dto.result.blacklist.GetBlacklistResult;
import com.somanyteam.event.entity.Blacklist;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.exception.blacklist.BlacklistEnterEmptyException;
import com.somanyteam.event.mapper.BlacklistMapper;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

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
    public List<GetBlacklistResult> getBlacklist(User curUser) {
        QueryWrapper<Blacklist> blacklistQueryWrapper = new QueryWrapper<>();
        blacklistQueryWrapper.eq("complaintant", curUser.getId())
                .select("id", "question_id", "create_time").orderByDesc("create_time");
        List<Blacklist> blacklistList  = blacklistMapper.selectList(blacklistQueryWrapper);

        List<GetBlacklistResult> resultList = new ArrayList<>();
        if(blacklistList.size() == 0){
            return resultList;
        }
        //获取问题id列表
        List<Long> questionIdList = new ArrayList<>();
        Map<Long, Blacklist> blacklistMap = new HashMap<>();
        for(Blacklist blacklist : blacklistList) {
            questionIdList.add(blacklist.getQuestionId());
            blacklistMap.put(blacklist.getQuestionId(), blacklist);
        }

        //根据问题id获取问题内容
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.in("id", questionIdList).select("content", "id");

        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);
        Map<Long, Question> questionMap = new HashMap<>();
        for(Question question : questionList) {
            questionMap.put(question.getId(), question);
        }


        for(Long id : questionIdList) {
            GetBlacklistResult result = new GetBlacklistResult();
            Question question = questionMap.get(id);
            Blacklist blacklist = blacklistMap.get(id);

            result.setContent(question.getContent());
            result.setId(blacklist.getId());
            result.setQuestionId(id);
            result.setTime(blacklist.getCreateTime());
            resultList.add(result);
        }


        return resultList;
    }

//    @Override
//    public int gotBlacklisted(String complaintant, String defendant) {
//        QueryWrapper<Blacklist> wrapper = new QueryWrapper<>();
//        wrapper.eq("complaintant", complaintant);
//        wrapper.eq("defendant", defendant);
//        Blacklist blacklist = blacklistMapper.selectOne(wrapper);
//        return (blacklist == null ? 0 : 1);
//    }

    @Override
    public int addBlacklist(Long id, String userId) {
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
