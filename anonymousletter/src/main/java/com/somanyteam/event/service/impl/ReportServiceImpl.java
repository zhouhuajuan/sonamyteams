package com.somanyteam.event.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.dto.result.question.QuestionAndAnswerResult;
import com.somanyteam.event.dto.result.question.QuestionResult;
import com.somanyteam.event.dto.result.report.GetHandledReportListResult;
import com.somanyteam.event.dto.result.report.GetReportContentResult;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.Report;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.enums.ReportStatusEnums;
import com.somanyteam.event.enums.ReportTypeEnums;
import com.somanyteam.event.exception.CommonException;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.mapper.ReportMapper;
import com.somanyteam.event.mapper.UserMapper;
import com.somanyteam.event.service.QuestionService;
import com.somanyteam.event.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    /**
     * 获取由该用户处理的举报
     * @param curUser 当前用户
     * @return
     */
    @Override
    public List<GetHandledReportListResult> getHandledReportList(User curUser) {
        List<GetHandledReportListResult> reportListResults = new ArrayList<>();

        QueryWrapper<Report> reportQueryWrapper = new QueryWrapper<>();
        reportQueryWrapper.select("id", "update_time", "question_id")
                .eq("handler", curUser.getId())
                .ne("status", ReportStatusEnums.NOT_HANDLE.getCode())
                .orderByDesc("update_time");
        List<Report> reportList = reportMapper.selectList(reportQueryWrapper);

        if(reportList.size() == 0){
            return reportListResults;
        }

        List<Long> questionIdList = new ArrayList<>();
        Map<Long, Report> reportMap = new HashMap<>();
        for(Report report : reportList) {
            questionIdList.add(report.getQuestionId());
            reportMap.put(report.getQuestionId(), report);
        }

        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.select("id", "content").in("id", questionIdList);
        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);

        Map<Long, Question> questionMap = new HashMap<>();
        for(Question question : questionList) {
            questionMap.put(question.getId(), question);
        }

        for(Long id : questionIdList) {
            GetHandledReportListResult result = new GetHandledReportListResult();
            Question question = questionMap.get(id);
            Report report = reportMap.get(id);

            result.setContent(question.getContent());
            result.setId(report.getId());
            result.setQuestionId(id);
            result.setTime(report.getUpdateTime());
            reportListResults.add(result);
        }

        return reportListResults;


    }

    @Override
    public GetReportContentResult getReportContent(Long id) {
        if(id == null) {
            throw new CommonException("id不能为空");
        }
        GetReportContentResult result = new GetReportContentResult();
        Report report = reportMapper.selectById(id);

        //举报者的id，若为游客举报，则为null
        String complaintantId = report.getComplaintant();
        //被举报者id，["",""]的格式，可以有多个被举报者
        String defendantId = report.getDefendant();

        String complaintantName = "";
        //如果不是游客
        if(complaintantId != null){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("username").eq("id", complaintantId);
            User complaintant = userMapper.selectOne(queryWrapper);
            complaintantName = complaintant.getUsername();
        }
        JSONArray list = JSONUtil.parseArray(defendantId);
        List<String> defendantList = list.toList(String.class);

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("username").in("id", defendantList);
        List<User> defendantUser = userMapper.selectList(userQueryWrapper);

        List<String> defendantUsername = new ArrayList<>();
        for(User user : defendantUser){
            defendantUsername.add(user.getUsername());
        }

        //即使问题被删除了，也返回
        QuestionAndAnswerResult questionAndAnswer = questionService.getAllQuestionAndAnswer(report.getQuestionId(), true);
        List<QuestionResult> allQuestion = questionAndAnswer.getAllQuestion();
        //获取父问题的提问时间
        QuestionResult parentQuestion = allQuestion.get(0);

        result.setComplaintant(complaintantName);
        result.setDefendant(defendantUsername);
        result.setReportTime(report.getCreateTime());
        result.setStatus(report.getStatus());
        result.setType(ReportTypeEnums.getTypeByCode(report.getType()).getType());
        result.setQuestionAndAnswerResult(questionAndAnswer);
        result.setQuestionTime(parentQuestion.getCreateTime());

        return result;
    }
}
