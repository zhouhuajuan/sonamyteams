package com.somanyteam.event.controller;

import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.BlacklistService;
import com.somanyteam.event.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: somanyteams
 * @description: 拉黑表控制层
 * @author: 周华娟
 * @create: 2021-11-22 18:47
 **/
@Api(tags = "拉黑相关接口")
@RestController
@RequestMapping("/blacklist")
public class BlackListController {

    @Autowired
    private BlacklistService blacklistService;

    @ApiOperation("根据问题把某用户加入到黑名单")
    @PostMapping("/addBlacklist")
    public ResponseMessage addBlacklist(@RequestParam("问题id") Long id) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();
        int i = blacklistService.addBlacklist(id, loginUserId);
        return i>0 ? ResponseMessage.newSuccessInstance("拉入黑名单成功") : ResponseMessage.newErrorInstance("拉入黑名单失败");
    }

    @ApiOperation("根据问题把某用户从黑名单移除")
    @PostMapping("/deleteBlacklist")
    public ResponseMessage deleteBlacklist(@RequestParam("问题id") Long id){
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();
        int i = blacklistService.deleteBlacklist(id, loginUserId);
        return i>0 ? ResponseMessage.newSuccessInstance("移除黑名单成功") : ResponseMessage.newErrorInstance("移除黑名单失败");
    }

}
