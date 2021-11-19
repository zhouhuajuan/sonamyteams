package com.somanyteam.event.service;

import com.somanyteam.event.entity.Question;

import java.util.List;

public interface QuestionService {

    /**
     * 获取所有未回答的问题
     * @param userId 用户唯一标识
     * @return List<Question>
     */
    List<Question> getUnansweredQuestion(String userId);

    int deleteQuestion(String userId,String id);
}
