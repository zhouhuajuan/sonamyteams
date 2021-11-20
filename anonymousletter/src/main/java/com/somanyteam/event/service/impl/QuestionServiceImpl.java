package com.somanyteam.event.service.impl;

import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.exception.question.QuestionIdEmptyException;
import com.somanyteam.event.exception.question.UserIdIsEmptyException;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: somanyteams
 * @description:
 * @author: 周华娟
 * @create: 2021-11-19 20:36
 **/
@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<VariousQuestionsListResult> getUnansweredQuestion(String userId) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        return questionMapper.getUnansweredQuestion(userId);
    }

    @Override
    public int deleteQuestion(String userId, String id) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        if (StrUtil.isEmpty(id)){
            //获取不到问题id
            throw new QuestionIdEmptyException();
        }

        return questionMapper.deleteQuestion(userId, id);
    }
}
