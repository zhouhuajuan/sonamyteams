package com.somanyteam.event.mapper;

import com.somanyteam.event.entity.Answer;
import java.util.List;

public interface AnswerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Answer record);

    Answer selectByPrimaryKey(Long id);

    List<Answer> selectAll();

    int updateByPrimaryKey(Answer record);
}