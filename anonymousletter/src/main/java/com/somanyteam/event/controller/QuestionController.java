package com.somanyteam.event.controller;


import com.somanyteam.event.dto.request.question.AddOrUpdateAnswerReqDTO;
import com.somanyteam.event.dto.request.question.QuestionAddReqDTO;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.BlacklistService;
import com.somanyteam.event.service.QuestionService;
import com.somanyteam.event.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private BlacklistService blacklistService;

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

        //这里删除问题是假删除，将del_flag置为1即可

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
        //1.先判断自己是不是被她拉黑了
            //拉黑则不能向她提问，也看不见她的提问箱
        int i = blacklistService.gotBlacklisted(questionAddReqDTO.getAId(), loginUserId);
        if (i==1){
            //被拉黑了，不能提问
            return ResponseMessage.newSuccessInstance("当前用户被回答者拉黑，不能提问");
        }

        //2.判断是不是父问题
            //夫问题直接提问
            //子问题的话要先看上一个问题有没有回答，没有回答就不能提问
        if (questionAddReqDTO.getParentQuestion()!=0){
            //不是父问题
            //拿到该父问题下所有问题的数量
            int questionCount = questionService.getQuestionCount(questionAddReqDTO.getParentQuestion(),
                    loginUserId, questionAddReqDTO.getAId());
            //拿到该父问题下所有问题的回答的数量

            //比较问题和回答的数量是否一致，不一致说明回答者还未回答完上一个问题，不能提问
            //一致说明回答者 回答完了，可以发出新的子问题
        }

        //3.添加问题成功后邮箱提醒

        return null;
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


    @ApiOperation("添加或者编辑回答")
    @PostMapping("/addOrUpdateAnswer")
    public ResponseMessage<Long> addOrUpdateAnswer(MultipartFile[] multipartFiles,  AddOrUpdateAnswerReqDTO dto) throws IOException {
        Subject subject = SecurityUtils.getSubject();
//        System.out.println(multipartFiles.length);
//        System.out.println(dto);
        return ResponseMessage.newSuccessInstance(questionService.addOrUpdateAnswer(multipartFiles, (User) subject.getPrincipal(), dto), "获取成功");
//        return ResponseMessage.newSuccessInstance("200");
    }

}
