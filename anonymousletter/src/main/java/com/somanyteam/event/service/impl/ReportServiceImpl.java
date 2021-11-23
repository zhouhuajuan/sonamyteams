package com.somanyteam.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somanyteam.event.dto.result.report.GetHandledReportListResult;
import com.somanyteam.event.entity.Question;
import com.somanyteam.event.entity.Report;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.enums.ReportStatusEnums;
import com.somanyteam.event.mapper.QuestionMapper;
import com.somanyteam.event.mapper.ReportMapper;
import com.somanyteam.event.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
