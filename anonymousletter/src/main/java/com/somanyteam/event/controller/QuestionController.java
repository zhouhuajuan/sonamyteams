package com.somanyteam.event.controller;

import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.request.question.QuestionAddReqDTO;
import com.somanyteam.event.dto.result.question.QuestionAddResult;
import com.somanyteam.event.dto.result.question.QuestionAndAnswerResult;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.Answer;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.QuestionService;
import com.somanyteam.event.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    public ResponseMessage deleteQuestion(@RequestParam("问题id") long id) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId(); //当前登录用户的id

        int i = questionService.deleteQuestion(loginUserId, id);
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
    public ResponseMessage addQuestion(@RequestBody @Validated QuestionAddReqDTO questionAddReqDTO) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();

        Question question1 = questionService.addQuestion(questionAddReqDTO, loginUserId);
        if (question1 == null){
            return ResponseMessage.newErrorInstance("添加问题失败");
        }else {
            QuestionAddResult result = new QuestionAddResult();
            BeanUtils.copyProperties(question1,result);
            questionService.sendEmail(questionAddReqDTO.getAId());
            return ResponseMessage.newSuccessInstance(result,"添加问题成功");
        }
    }

    @ApiOperation("获取已收到回答问题的列表")
    @GetMapping("/getReceivedAnswerQuestionList")
    public ResponseMessage<List<VariousQuestionsListResult>> getReceivedAnswerQuestionList(){
        Subject subject = SecurityUtils.getSubject();
        return ResponseMessage.newSuccessInstance(questionService.getReceivedAnswerQuestionList((User) subject.getPrincipal()), "获取成功");
    }

    @ApiOperation("获取未收到回答问题的列表")
    @GetMapping("/getUnreceivedAnswerQuestionList")
    public ResponseMessage<List<VariousQuestionsListResult>> getUnreceivedAnswerQuestionList(){
        Subject subject = SecurityUtils.getSubject();
        return ResponseMessage.newSuccessInstance(questionService.getUnreceivedAnswerQuestionList((User) subject.getPrincipal()));
    }

    @ApiOperation("获取父问题和答案及以下的子问题和答案")
    @PostMapping("/getQuestionAndAnswer")
    public ResponseMessage getQuestionAndAnswer(long id) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();
        List<Question> allQuestion = questionService.getAllQuestion(id,loginUserId);
        List<Answer> allAnswer = questionService.getAllAnswer(id,loginUserId);
        QuestionAndAnswerResult result = new QuestionAndAnswerResult();
        result.setAllQuestion(allQuestion);
        result.setAllAnswer(allAnswer);
        return ResponseMessage.newSuccessInstance(result);
    }

}
