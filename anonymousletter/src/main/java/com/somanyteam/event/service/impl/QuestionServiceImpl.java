package com.somanyteam.event.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.exception.question.UserIdIsEmptyException;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.service.QuestionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
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
    public int deleteQuestion(String userId, String id) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }

        if (StrUtil.isEmpty(id)){
            //获取不到问题id

        }

        int i = 0;

//        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("aId",userId);
//        queryWrapper.eq("parentQuestion","");
//        List<Question> questionList = questionMapper.selectList(queryWrapper);
//        for (Question question: questionList) {
//            if (question.getId().equals(id)){
//                i = questionMapper.deleteById(id);
//                //还要删除父问题的子问题以及父子问题的回答
//                //现在只是删除了父问题
//            }
//        }
//        //int i = questionMapper.deleteById(id);
        return i;
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
            result.setNewFlag(0);
            resultList.add(result);
        }

        for(Question question : notAllAnsweredParentQuestion){
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            //存在未回答的子问题，newFlag为1
            result.setNewFlag(1);
            resultList.add(result);
        }

        //对问题列表进行按时间进行排序，由最新的时间降序排
        CollectionUtil.sort(resultList, VariousQuestionsListResult::compareTo);

        return resultList;
    }
}
