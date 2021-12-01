package com.somanyteam.event.controller;



import com.somanyteam.event.dto.request.question.AddOrUpdateAnswerReqDTO;
import com.somanyteam.event.dto.request.question.QuestionAddReqDTO;
import com.somanyteam.event.dto.result.question.QuestionAddResult;
import com.somanyteam.event.dto.result.question.QuestionAndAnswerResult;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.Answer;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.QuestionService;
import com.somanyteam.event.util.ResponseMessage;
import com.somanyteam.event.util.ShiroUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
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

    @RequiresAuthentication
    @ApiOperation("获取所有未回答问题")
    @GetMapping("/getUnansweredQuestion")
    public ResponseMessage getUnansweredQuestion() {
        User loginUser = ShiroUtil.getUser();
        String loginUserId = loginUser.getId(); //当前登录用户的id
        List<VariousQuestionsListResult> questionList = questionService.getUnansweredQuestion(loginUserId);
        return (questionList.isEmpty() ? ResponseMessage.newSuccessInstance("当前用户没有未回答的问题") : ResponseMessage.newSuccessInstance(questionList));
    }

    @RequiresAuthentication
    @ApiOperation("删除问题(被提问者)")
    @PostMapping("/deleteQuestion")
    public ResponseMessage deleteQuestion(@RequestParam("问题id") Long id) {
        User loginUser = ShiroUtil.getUser();
        String loginUserId = loginUser.getId(); //当前登录用户的id
        int i = questionService.deleteQuestion(loginUserId, id);
        return (i==1 ? ResponseMessage.newSuccessInstance("删除成功") : ResponseMessage.newErrorInstance("删除失败"));
    }

    @RequiresAuthentication
    @ApiOperation("获取已回答问题")
    @GetMapping("/getAnsweredQuestion")
    public ResponseMessage<List<VariousQuestionsListResult>> getAnsweredQuestion(){
//        Subject subject = SecurityUtils.getSubject();
        return ResponseMessage.newSuccessInstance(questionService.getAnsweredQuestion(ShiroUtil.getUser()), "获取成功");
    }

    @ApiOperation("获取公开父问题列表")
    @GetMapping("/getPublicQuestions/{userId}")
    public ResponseMessage getPublicQuestions(@PathVariable("userId")String userId) {
        List<VariousQuestionsListResult> publicQuestions = questionService.getPublicQuestions(userId);
        return (publicQuestions.isEmpty() ? ResponseMessage.newSuccessInstance("公开父问题列表为空") : ResponseMessage.newSuccessInstance(publicQuestions));
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
            questionService.sendEmail(question1,questionAddReqDTO.getAId());
            return ResponseMessage.newSuccessInstance(result,"添加问题成功");
        }
    }

    @RequiresAuthentication
    @ApiOperation("获取已收到回答问题的列表")
    @GetMapping("/getReceivedAnswerQuestionList")
    public ResponseMessage<List<VariousQuestionsListResult>> getReceivedAnswerQuestionList(){
//        Subject subject = SecurityUtils.getSubject();
        return ResponseMessage.newSuccessInstance(questionService.getReceivedAnswerQuestionList(ShiroUtil.getUser()), "获取成功");
    }

    @RequiresAuthentication
    @ApiOperation("获取未收到回答问题的列表")
    @GetMapping("/getUnreceivedAnswerQuestionList")
    public ResponseMessage<List<VariousQuestionsListResult>> getUnreceivedAnswerQuestionList(){
//        Subject subject = SecurityUtils.getSubject();
        return ResponseMessage.newSuccessInstance(questionService.getUnreceivedAnswerQuestionList(ShiroUtil.getUser()));
    }

    @RequiresAuthentication
    @ApiOperation("添加或者编辑回答")
    @PostMapping("/addOrUpdateAnswer")
    public ResponseMessage<Long> addOrUpdateAnswer(MultipartFile[] multipartFiles,  AddOrUpdateAnswerReqDTO dto) throws IOException {
//        Subject subject = SecurityUtils.getSubject();

        return ResponseMessage.newSuccessInstance(questionService.addOrUpdateAnswer(multipartFiles, ShiroUtil.getUser(), dto), "获取成功");
//        return ResponseMessage.newSuccessInstance("200");
    }

    @ApiOperation("获取父问题和答案及以下的子问题和答案")
    @GetMapping("/getQuestionAndAnswer/{parentId}")
    public ResponseMessage getQuestionAndAnswer(@PathVariable("parentId") Long parentId,
                                                @RequestParam("表示是否要获取到被删掉的问题") Boolean flag) {
        QuestionAndAnswerResult res = questionService.getAllQuestionAndAnswer(parentId,flag);
        return res==null ? ResponseMessage.newErrorInstance("获取问题答案失败") : ResponseMessage.newSuccessInstance(res,"获取问题答案成功");
    }

}
