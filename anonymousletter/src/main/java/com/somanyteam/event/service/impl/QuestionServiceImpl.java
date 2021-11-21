package com.somanyteam.event.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.constant.CommonConstant;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;

import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.User;

import com.somanyteam.event.exception.question.QuestionIdEmptyException;

import com.somanyteam.event.exception.question.UserIdIsEmptyException;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.service.QuestionService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public int deleteQuestion(String userId, long id) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        if (id==0){
            //获取不到问题id
            throw new QuestionIdEmptyException();
        }

        return questionMapper.updateDelFlag(userId, id);
        //return questionMapper.deleteQuestion(userId, id);
    }

    /*@Override
    public int deleteQuestion(String userId, long id) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        if (StrUtil.isEmpty(id)){
            //获取不到问题id
            throw new QuestionIdEmptyException();
        }

        return questionMapper.updateDelFlag(userId, id);

    }*/

    @Override
    public List<VariousQuestionsListResult> getPublicQuestions(String userId) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        return questionMapper.getPublicQuestions(userId);
    }

    /**
     * 获取已收到回答的问题列表
     * @param curUser 用户身份
     * @return
     */
    @Override
    public List<VariousQuestionsListResult> getReceivedAnswerQuestionList(User curUser) {

        List<Question> questionList = questionMapper.getReceivedAnswerQuestionList(curUser.getId());

        List<VariousQuestionsListResult> resultList = new ArrayList<>();

        for(Question question : questionList){
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            result.setNewFlag(CommonConstant.NO);
            resultList.add(result);
        }
        return resultList;

    }

    /**
     * 获取未回答问题列表
     * @param curUser 用户身份
     * @return
     */
    @Override
    public List<VariousQuestionsListResult> getUnreceivedAnswerQuestionList(User curUser) {
        List<Question> unreceivedAnswerQuestionList = questionMapper.getUnreceivedAnswerQuestionList(curUser.getId());
        List<VariousQuestionsListResult> resultList = new ArrayList<>();

        for(Question question : unreceivedAnswerQuestionList){
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            result.setNewFlag(CommonConstant.NO);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public int getQuestionCount(long id, String q_id, String a_id) {
        return questionMapper.getQuestionCount(id, q_id, a_id);
    }

    /**
     * 获取已回答问题列表
     * @return
     */
    @Override
    public List<VariousQuestionsListResult> getAnsweredQuestion(User curUser) {
        //获取父问题及子问题全部回答了的父问题list
        List<Question> allAnsweredParentQuestion = questionMapper.getAllAnsweredParentQuestion(curUser.getId());

        //获取父问题下存在未回答子问题的父问题list
        List<Question> notAllAnsweredParentQuestion = questionMapper.getNotAllAnsweredParentQuestion(curUser.getId());
        List<VariousQuestionsListResult> resultList = new ArrayList<>();

        for(Question question : allAnsweredParentQuestion) {
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            result.setNewFlag(CommonConstant.NO);
            resultList.add(result);
        }

        for(Question question : notAllAnsweredParentQuestion){
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            //存在未回答的子问题，newFlag为1
            result.setNewFlag(CommonConstant.YES);
            resultList.add(result);
        }

        //对问题列表进行按时间进行排序，由最新的时间降序排
        CollectionUtil.sort(resultList, VariousQuestionsListResult::compareTo);

        return resultList;
    }
}
