package com.somanyteam.event.controller;

import com.somanyteam.event.dto.result.question.VariousQuestionsListResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: somanyteams
 * @description: 拉黑表控制层
 * @author: 周华娟
 * @create: 2021-11-22 18:47
 **/
@Api(tags = "问题相关接口")
@RestController
@RequestMapping("/blacklist")
public class BlackListController {

    @GetMapping("/addBlacklist")
    public ResponseMessage addBlacklist() {
        return null;

    }
}
