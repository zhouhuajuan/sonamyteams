package com.somanyteam.event.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.somanyteam.event.constant.CommonConstant;
import com.somanyteam.event.dto.request.question.AddOrUpdateAnswerReqDTO;
import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;

import com.somanyteam.event.entity.Answer;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.User;

import com.somanyteam.event.exception.CommonException;
import com.somanyteam.event.exception.question.QuestionIdEmptyException;

import com.somanyteam.event.exception.question.UserIdIsEmptyException;
import com.somanyteam.event.mapper.AnswerMapper;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.service.QuestionService;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: somanyteams
 * @description:
 * @author: 周华娟
 * @create: 2021-11-19 20:36
 **/
@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Value("${upload.img.path}")
    private String imgPath;

    @Override
    public List<VariousQuestionsListResult> getUnansweredQuestion(String userId) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        return questionMapper.getUnansweredQuestion(userId);
    }

    @Override
    public int deleteQuestion(String userId, long id) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        if (id==0){
            //获取不到问题id
            throw new QuestionIdEmptyException();
        }

        return questionMapper.updateDelFlag(userId, id);
        //return questionMapper.deleteQuestion(userId, id);
    }

    /*@Override
    public int deleteQuestion(String userId, long id) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        if (StrUtil.isEmpty(id)){
            //获取不到问题id
            throw new QuestionIdEmptyException();
        }

        return questionMapper.updateDelFlag(userId, id);

    }*/

    @Override
    public List<VariousQuestionsListResult> getPublicQuestions(String userId) {
        if (StrUtil.isEmpty(userId)){
            //获取不到用户id
            throw new UserIdIsEmptyException();
        }
        return questionMapper.getPublicQuestions(userId);
    }

    /**
     * 获取已收到回答的问题列表,父问题以及子问题全部收到回答
     * @param curUser 用户身份
     * @return
     */
    @Override
    public List<VariousQuestionsListResult> getReceivedAnswerQuestionList(User curUser) {

        List<Question> questionList = questionMapper.getReceivedAnswerQuestionList(curUser.getId());

        List<VariousQuestionsListResult> resultList = new ArrayList<>();

        for(Question question : questionList){
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            result.setNewFlag(CommonConstant.NO);
            resultList.add(result);
        }
        return resultList;

    }

    /**
     * 获取未回答问题列表,父问题未收到回答或者父问题下存在未收到回答的子问题
     * @param curUser 用户身份
     * @return
     */
    @Override
    public List<VariousQuestionsListResult> getUnreceivedAnswerQuestionList(User curUser) {
        List<Question> unreceivedAnswerQuestionList = questionMapper.getUnreceivedAnswerQuestionList(curUser.getId());
        List<VariousQuestionsListResult> resultList = new ArrayList<>();

        for(Question question : unreceivedAnswerQuestionList){
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            result.setNewFlag(CommonConstant.NO);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public int getQuestionCount(long id, String q_id, String a_id) {
        return questionMapper.getQuestionCount(id, q_id, a_id);
    }

    @Override
    public Long addOrUpdateAnswer(MultipartFile[] multipartFiles, User curUser, AddOrUpdateAnswerReqDTO dto) throws IOException {
        //1.判断file的个数，还有大小，格式，生成的链接是什么

        String imgUrl = "";
        //如果用户的回答中有图片
        if(multipartFiles != null) {
            Map<String, MultipartFile> fileMap = new HashMap<>();
            List<String> filePaths = new ArrayList<>();
            if(multipartFiles.length > 4) {
                throw new CommonException("图片个数不能超过四个");
            }
            for (MultipartFile file : multipartFiles) {

                String originalFileName = file.getOriginalFilename();
                if (!originalFileName.endsWith("png") && !originalFileName.endsWith("jpg")) {
                    throw new CommonException(originalFileName + "文件格式不符合要求，请上传jpg或者png格式的文件");
                }
                //文件大小不超过5MB
                Float size = Float.parseFloat(String.valueOf(file.getSize())) / (1024 * 1024);
                BigDecimal b = new BigDecimal(size);
                // 2表示2位 ROUND_HALF_UP表明四舍五入，
                size = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

                if (size > 5) {
                    throw new CommonException(originalFileName + "图片大小过大，请选择小于5MB的图片");
                }

                String picName = StrUtil.subBefore(originalFileName, ".", true);

                String encryptName = new Md5Hash(curUser.getId() + picName + new Date()).toHex();
                String filePath = encryptName + originalFileName.substring(originalFileName.lastIndexOf("."));

                filePaths.add(filePath);
                fileMap.put(filePath, file);
            }

            //2.写图片
            for (String filePath : filePaths) {
                MultipartFile file = fileMap.get(filePath);

                FileUtil.writeBytes(file.getBytes(), filePath);
            }

            JSONArray array = JSONUtil.parseArray(filePaths);
           imgUrl = array.toString();
        }
        Answer answer = new Answer();
        BeanUtils.copyProperties(dto, answer);
        answer.setImage(imgUrl);
        //添加或编辑回答
        if(answer.getId() == null){
            answerMapper.insert(answer);
        } else{
            answerMapper.updateById(answer);
        }
        //更新问题
        Question question = new Question();
        question.setId(dto.getQuestionId());
        question.setAnswerStatus(dto.getAnswerStatus());
        questionMapper.updateById(question);

        //TODO 邮件通知提问者

        return answer.getId();
    }

    /**
     * 获取已回答问题列表
     * @return
     */
    @Override
    public List<VariousQuestionsListResult> getAnsweredQuestion(User curUser) {
        //获取父问题及子问题全部回答了的父问题list
        List<Question> allAnsweredParentQuestion = questionMapper.getAllAnsweredParentQuestion(curUser.getId());

        //获取父问题下存在未回答子问题的父问题list
        List<Question> notAllAnsweredParentQuestion = questionMapper.getNotAllAnsweredParentQuestion(curUser.getId());
        List<VariousQuestionsListResult> resultList = new ArrayList<>();

        for(Question question : allAnsweredParentQuestion) {
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            result.setNewFlag(CommonConstant.NO);
            resultList.add(result);
        }

        for(Question question : notAllAnsweredParentQuestion){
            VariousQuestionsListResult result = new VariousQuestionsListResult();
            BeanUtils.copyProperties(question, result);
            //存在未回答的子问题，newFlag为1
            result.setNewFlag(CommonConstant.YES);
            resultList.add(result);
        }

        //对问题列表进行按时间进行排序，由最新的时间降序排
        CollectionUtil.sort(resultList, VariousQuestionsListResult::compareTo);

        return resultList;
    }
}
