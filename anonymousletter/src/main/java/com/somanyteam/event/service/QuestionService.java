package com.somanyteam.event.service;

import com.somanyteam.event.dto.request.question.AddOrUpdateAnswerReqDTO;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     * 获取父问题以及子问题的总数量
     * @param id 父问题id
     * @param q_id 提问者id
     * @param a_id 回答者id
     * @return int
     */
    int getQuestionCount(long id,String q_id,String a_id);

    /**
     * 添加或者更新回答
     * @param multipartFiles 图片
     * @param curUser 用户身份
     * @param dto dto
     * @return 问题id
     */
    Long addOrUpdateAnswer(MultipartFile[] multipartFiles, User curUser, AddOrUpdateAnswerReqDTO dto) throws IOException;
}
