package com.somanyteam.event.service;

import com.somanyteam.event.dto.request.question.QuestionAddReqDTO;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.User;

import java.util.List;

public interface QuestionService {

    /**
     * 获取所有未回答的问题
     * @param userId 用户唯一标识
     * @return List<UnansweredQuestionResult>
     */
    List<VariousQuestionsListResult> getUnansweredQuestion(String userId);

    /**
     * 假删除问题
     * @param userId 用户id
     * @param id 问题id
     * @return int
     */
    int deleteQuestion(String userId,long id);

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
     * @return List<VariousQuestionsListResult>
     */
    List<VariousQuestionsListResult> getReceivedAnswerQuestionList(User curUser);

    /**
     * 获取未收到回答的问题列表
     * @param curUser 用户身份
     * @return List<VariousQuestionsListResult>
     */
    List<VariousQuestionsListResult> getUnreceivedAnswerQuestionList(User curUser);

    /**
     * 判断是否全部问题都已回答
     */
    int answerAllQuestion(long id,String q_id,String a_id);

    Question addQuestion(QuestionAddReqDTO questionAddReqDTO,String userId);

    int insertQuestion(Question question);

    int sendEmail(String a_id);
}
