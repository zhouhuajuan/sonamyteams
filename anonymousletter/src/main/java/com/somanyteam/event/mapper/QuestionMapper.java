package com.somanyteam.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.somanyteam.event.entity.Question;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface QuestionMapper extends BaseMapper<Question> {
//    int deleteByPrimaryKey(Long id);
//
//    int insert(Question record);
//
//    Question selectByPrimaryKey(Long id);
//
//    List<Question> selectAll();
//
//    int updateByPrimaryKey(Question record);

//    @Select("select * from question q,answer a where a.question_id=q.id and q.a_id ="+id)
//    List<Question> getUnansweredQuestion(String id);
}