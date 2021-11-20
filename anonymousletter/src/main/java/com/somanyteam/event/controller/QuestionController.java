package com.somanyteam.event.controller;


import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.QuestionService;
import com.somanyteam.event.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/deleteQuestion")
    public ResponseMessage deleteQuestion(@RequestParam("问题id") String id) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId(); //当前登录用户的id

        int i = questionService.deleteQuestion(loginUserId, id);
        if (i>0){
            return ResponseMessage.newSuccessInstance("删除成功");
        }else {
            return ResponseMessage.newErrorInstance("删除失败");
        }
    }

    @ApiOperation("获取公开父问题列表")
    @GetMapping("/getPublicQuestions")
    public ResponseMessage getPublicQuestions() {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();

        List<VariousQuestionsListResult> publicQuestions = questionService.getPublicQuestions(loginUserId);
        if (publicQuestions.isEmpty()){
            return ResponseMessage.newSuccessInstance("公开父问题列表为空");
        }else {
            System.out.println(publicQuestions);
            return ResponseMessage.newSuccessInstance(publicQuestions);
        }
    }

    @ApiOperation("添加问题(提问者)")
    @PostMapping("/addQuestion")
    public ResponseMessage addQuestion(@RequestParam("被提问者id") String answerId) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();
        //1.先判断自己是不是被她拉黑了
            //拉黑则不能向她提问，也看不见她的提问箱

        //2.判断是不是父问题
            //夫问题直接提问
            //子问题的话要先看上一个问题有没有回答，没有回答就不能提问

        //3.添加问题成功后邮箱提醒

        return null;
    }
}
