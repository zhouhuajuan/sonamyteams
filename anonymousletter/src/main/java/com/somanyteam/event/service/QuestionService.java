package com.somanyteam.event.service;

import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;

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
     * 获取公开父问题列表
     * @param userId 用户id
     * @return List<VariousQuestionsListResult>
     */
    List<VariousQuestionsListResult> getPublicQuestions(String userId);
}
