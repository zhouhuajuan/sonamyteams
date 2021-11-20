package com.somanyteam.event.controller;


import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.QuestionService;
import com.somanyteam.event.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: somanyteams
 * @description: 问题controller
 * @author: 周华娟
 * @create: 2021-11-19 20:34
 **/



@Api(tags = "问题相关接口")
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @ApiOperation("获取所有未回答问题")
    @GetMapping("/getUnansweredQuestion")
    public ResponseMessage getUnansweredQuestion() {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId(); //当前登录用户的id

        List<VariousQuestionsListResult> questionList = questionService.getUnansweredQuestion(loginUserId);

        if (questionList.isEmpty()){
            return ResponseMessage.newSuccessInstance("当前用户没有未回答的问题");
        }else {
            return ResponseMessage.newSuccessInstance(questionList);
        }
    }

    @ApiOperation("删除问题(被提问者)")
    @GetMapping("/deleteQuestion/{userId}")
    public ResponseMessage deleteQuestion(@ApiParam(value = "用户id") @PathVariable("userId") String userId
            ,@ApiParam(value = "问题id") String id) {
//        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
//        String loginUserId = loginUser.getId(); //当前登录用户的id

        int i = questionService.deleteQuestion(userId, id);
        if (i>0){
            return ResponseMessage.newSuccessInstance("删除成功");
        }else {
            return ResponseMessage.newErrorInstance("删除失败");
        }
    }

    @ApiOperation("获取已回答问题")
    @GetMapping("/getAnsweredQuestion")
    public ResponseMessage<List<VariousQuestionsListResult>> getAnsweredQuestion(){
        Subject subject = SecurityUtils.getSubject();
        return ResponseMessage.newSuccessInstance(questionService.getAnsweredQuestion((User) subject.getPrincipal()), "获取成功");
    }

}
