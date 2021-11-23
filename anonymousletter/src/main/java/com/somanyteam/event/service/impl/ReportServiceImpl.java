package com.somanyteam.event.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.dto.request.report.AddReportReqDTO;
import com.somanyteam.event.dto.request.report.HandleReportReqDTO;
import com.somanyteam.event.dto.result.question.QuestionAndAnswerResult;
import com.somanyteam.event.dto.result.question.QuestionResult;
import com.somanyteam.event.dto.result.report.GetReportContentResult;
import com.somanyteam.event.dto.result.report.GetReportListResult;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.Report;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.enums.ReportStatusEnums;
import com.somanyteam.event.enums.ReportTypeEnums;
import com.somanyteam.event.exception.CommonException;
import com.somanyteam.event.exception.report.ReportEnterEmptyException;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.mapper.ReportMapper;
import com.somanyteam.event.mapper.UserMapper;
import com.somanyteam.event.service.QuestionService;
import com.somanyteam.event.service.ReportService;
import com.somanyteam.event.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private QuestionService questionService;

    /**
     * 获取由该用户处理的举报
     * @return
     */
//    @Override
//    public List<GetHandledReportListResult> getHandledReportList(User curUser) {
//        List<GetHandledReportListResult> reportListResults = new ArrayList<>();
//
//        QueryWrapper<Report> reportQueryWrapper = new QueryWrapper<>();
//        reportQueryWrapper.select("id", "update_time", "question_id")
//                .eq("handler", curUser.getId())
//                .ne("status", ReportStatusEnums.NOT_HANDLE.getCode())
//                .orderByDesc("update_time");
//        List<Report> reportList = reportMapper.selectList(reportQueryWrapper);
//
//        if(reportList.size() == 0){
//            return reportListResults;
//        }
//
//        List<Long> questionIdList = new ArrayList<>();
//        Map<Long, Report> reportMap = new HashMap<>();
//        for(Report report : reportList) {
//            questionIdList.add(report.getQuestionId());
//            reportMap.put(report.getQuestionId(), report);
//        }
//
//        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
//        questionQueryWrapper.select("id", "content").in("id", questionIdList);
//        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);
//
//        Map<Long, Question> questionMap = new HashMap<>();
//        for(Question question : questionList) {
//            questionMap.put(question.getId(), question);
//        }
//
//        for(Long id : questionIdList) {
//            GetHandledReportListResult result = new GetHandledReportListResult();
//            Question question = questionMap.get(id);
//            Report report = reportMap.get(id);
//
//            result.setContent(question.getContent());
//            result.setId(report.getId());
//            result.setQuestionId(id);
//            result.setTime(report.getUpdateTime());
//            reportListResults.add(result);
//        }
//
//        return reportListResults;
//
//
//    }

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

        cn.hutool.json.JSONArray list = JSONUtil.parseArray(defendantId);
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

    @Override
    public List<GetReportListResult> getReportList(User curUser, Integer type) {
        List<GetReportListResult> reportListResults = new ArrayList<>();

        QueryWrapper<Report> reportQueryWrapper = new QueryWrapper<>();
        reportQueryWrapper.select("id", "update_time", "question_id")
//                .eq("handler", curUser.getId())
//                .ne("status", ReportStatusEnums.NOT_HANDLE.getCode())
                .orderByDesc("update_time");
        if (type == 0){
            reportQueryWrapper.eq("status",ReportStatusEnums.NOT_HANDLE.getCode());
        }else {
            reportQueryWrapper.eq("handler", curUser.getId()).ne("status", ReportStatusEnums.NOT_HANDLE.getCode());
        }

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
            GetReportListResult result = new GetReportListResult();
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
    public int addReport(AddReportReqDTO addReportReqDTO, String userId) {
        if (ObjectUtil.isEmpty(addReportReqDTO)){
            throw new ReportEnterEmptyException();
        }

        QueryWrapper<Question> wrapper =new QueryWrapper<>();
        wrapper.eq("id",addReportReqDTO.getQuestionId());
        Question question = questionMapper.selectOne(wrapper);

        Report report = new Report();
        report.setQuestionId(addReportReqDTO.getQuestionId());
        report.setType(addReportReqDTO.getType());
        Date date = new Date();
        report.setCreateTime(date);
        report.setUpdateTime(date);
        List<String> list = new ArrayList<>();
        if (userId == null){
            //说明是游客登录
            list.add(question.getQId());
            list.add(question.getAId());
            String json = JSONArray.toJSONString(list);
            report.setDefendant(json);
        }else{
            if (question.getAId().equals(userId)){
                //举报人是回答者
                report.setComplaintant(userId);

                list.add(question.getQId());
                String json2 = JSONArray.toJSONString(list);
                report.setDefendant(json2);
            }else {
                //举报者是提问者
                report.setComplaintant(userId);

                list.add(question.getAId());
                String json2 = JSONArray.toJSONString(list);
                report.setDefendant(json2);
            }
        }
        return reportMapper.insert(report);
    }

    @Override
    public int handleReport(HandleReportReqDTO handleReportReqDTO, String userId) {
        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.eq("id",handleReportReqDTO.getId());
        Report report = reportMapper.selectOne(wrapper);
        report.setHandler(userId);
        report.setStatus(handleReportReqDTO.getStatus());
        Date date = new Date();
        report.setUpdateTime(date);
        int i = reportMapper.updateById(report);
        if (i>0){
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",report.getQuestionId());
            Question question = questionMapper.selectOne(queryWrapper);

            net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(handleReportReqDTO.getUserId());
            List list = (List)net.sf.json.JSONArray.toCollection(jsonArray, String.class);
            Iterator it = list.iterator();
            List<String> userIdList = new ArrayList<>();
            while(it.hasNext()){
                String defendant = (String) it.next();
                userIdList.add(defendant);
            }

            for (String id : userIdList) {
                QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id",id);
                User user = userMapper.selectOne(queryWrapper1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formatDate = sdf.format(date);
                String content = user.getUsername()+": \r\n"
                        + "hello，您由于"+ReportTypeEnums.getTypeByCode(report.getType())
                        + "在"+question.getContent()+"的相关内容中涉嫌违规，现对您进行举报。\r\n"
                        +formatDate;
                emailUtil.sendEmail(user.getEmail(),content);
                //xxx:hello，您由于（举报原因）在（）的相关内容中涉嫌违规，现对您进行举报。+时间
            }

        }
        return i;
    }
}
