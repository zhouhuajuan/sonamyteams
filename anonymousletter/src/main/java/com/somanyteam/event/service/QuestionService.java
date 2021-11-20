package com.somanyteam.event.service;

import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.User;

import java.util.List;

public interface QuestionService {

    /**
     * 获取所有未回答的问题
     * @param userId 用户唯一标识
     * @return List<UnansweredQuestionResult>
     */
    List<VariousQuestionsListResult> getUnansweredQuestion(String userId);

    int deleteQuestion(String userId,String id);

    /**
     * 获取已回答问题
     * @param curUser 当前用户
     * @return 已回答问题列表
     */
    List<VariousQuestionsListResult> getAnsweredQuestion(User curUser);

    /**
     * 获取公开父问题列表
     * @param userId 用户id
     * @return List<VariousQuestionsListResult>
     */
    List<VariousQuestionsListResult> getPublicQuestions(String userId);

    /**
     * 获取已收到回答的问题列表
     * @param curUser 用户身份
     * @return
     */
    List<VariousQuestionsListResult> getReceivedAnswerQuestionList(User curUser);
}
