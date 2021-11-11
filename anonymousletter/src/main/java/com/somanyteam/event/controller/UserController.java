package com.somanyteam.event.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.request.user.UserLoginReqDTO;
import com.somanyteam.event.dto.request.user.UserModifyPasswordReqDTO;
import com.somanyteam.event.dto.result.user.UserLoginResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.service.UserService;
import com.somanyteam.event.util.PasswordUtil;
import cn.hutool.core.util.ObjectUtil;
import com.somanyteam.event.dto.request.user.UserForgetPwdReqDTO;
import com.somanyteam.event.dto.request.user.UserRegisterReqDTO;
import com.somanyteam.event.exception.user.UserEnterEmptyException;
import com.somanyteam.event.util.ResponseMessage;
import com.somanyteam.event.util.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;

/**
 * @author zbr
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${upload.img.path}")
    private String uploadPathImg; //保存在服务器图片目录的路径

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public ResponseMessage<UserLoginResult> login(@RequestBody @Validated UserLoginReqDTO userLoginReqDTO, HttpSession session){
//        String rightCode = (String) session.getAttribute("code");
//
//        if(!rightCode.equals(userLoginReqDTO.getCode())){
//            throw new VerifyCodeNotMatchException();
//        }
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token  = new UsernamePasswordToken(userLoginReqDTO.getEmail(), userLoginReqDTO.getPassword());
        if(userLoginReqDTO.getRememberMe() != null && userLoginReqDTO.getRememberMe() == 1) {
            token.setRememberMe(true);
        }
        subject.login(token);

        User loginUser = (User) subject.getPrincipal();
        UserLoginResult result = new UserLoginResult();
        BeanUtils.copyProperties(loginUser, result);

        return ResponseMessage.newSuccessInstance(result, "登录成功");
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public ResponseMessage register(@RequestBody @Validated UserRegisterReqDTO userRegisterReqDTO) {
        User user = new User();
        if (ObjectUtil.isEmpty(userRegisterReqDTO)){
            //输入为空
            throw new UserEnterEmptyException();
        }

        boolean equals = userRegisterReqDTO.getPassword().equals(userRegisterReqDTO.getConfirmPwd());
        if (!equals){
            //确认密码和密码不一致
            return ResponseMessage.newErrorInstance("确认密码和密码不一致");
        }

        boolean b = userService.checkCode(userRegisterReqDTO.getEmail(), userRegisterReqDTO.getCode());
        if (!b){
            //验证码错误
            return ResponseMessage.newErrorInstance("验证码错误");
        }

        BeanUtils.copyProperties(userRegisterReqDTO, user); //邮箱，密码
        //user.setId(RandomCodeUtil.getRandom()); //随机生成一个随机数
        user.setUsername("偷心盗贼");
        user.setIdentity(0);
        user.setSalt(PasswordUtil.randomSalt());
        user.setImgUrl("https://8.134.33.6/photo/a.png");//默认头像图片链接

        if (userService.saveUser(user) > 0) {
            return ResponseMessage.newSuccessInstance("注册成功");
        } else {
            return ResponseMessage.newErrorInstance("注册失败");
        }
    }

    @ApiOperation(value = "忘记密码")
    @PostMapping("/forgetPwd")
    public ResponseMessage forgetPwd(@RequestBody @Validated UserForgetPwdReqDTO userForgetPwdReqDTO) {
        if (ObjectUtil.isEmpty(userForgetPwdReqDTO)){
            //输入为空
            throw new UserEnterEmptyException();
        }

        boolean equals = userForgetPwdReqDTO.getModifyPwd().equals(userForgetPwdReqDTO.getConfirmPwd());
        if (!equals){
            //确认密码和修改密码不一致
            return ResponseMessage.newErrorInstance("确认密码和修改密码不一致");
        }

        boolean b = userService.checkCode(userForgetPwdReqDTO.getEmail(), userForgetPwdReqDTO.getCode());
        if (!b){
            //验证码错误
            return ResponseMessage.newErrorInstance("验证码错误");
        }

        int i = userService.forgetPwd(userForgetPwdReqDTO.getEmail(), userForgetPwdReqDTO.getModifyPwd());
        if (i > 0) {
            return ResponseMessage.newSuccessInstance("找回密码成功");
        } else {
            return ResponseMessage.newErrorInstance("找回密码失败");
        }
    }

    @ApiOperation(value = "拿userid")
    @PostMapping("/getAllUserId")
    public ResponseMessage getAllUserId() {
        //拿到验证码，为空则发送邮件失败
        Set<String> allUserId = userService.getAllUserId();
        if (ObjectUtil.isNull(allUserId)) {
            return ResponseMessage.newErrorInstance("失败");
        } else {
            //返回前端验证码，前端判断验证码是否输入正确
            for (String s:allUserId) {
                System.out.println(s);
            }
            return ResponseMessage.newSuccessInstance("成功");
        }
    }

    @ApiOperation(value = "发送邮件")
    @PostMapping("/sendEmail")
    public ResponseMessage sendEmail(String email) {
        //拿到验证码，为空则发送邮件失败
        String code = userService.sendEmail(email);
        if (StrUtil.isEmpty(code)) {
            return ResponseMessage.newErrorInstance("发送失败");
        } else {
            //返回前端验证码，前端判断验证码是否输入正确
            return ResponseMessage.newSuccessInstance("发送成功",code);
        }
    }

    // /e094be639937a02d32fe42f1ade7bffc
    @ApiOperation("上传头像")
    @PostMapping("/uploadPhoto")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "选择需要上传的图片", required = true, paramType = "query"),
    })
    public ResponseMessage uploadPhoto(@RequestPart("file") MultipartFile file) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        try {
            //保存图片的名字为用户id+图片名称+上传图片时间
            String originalFileName = file.getOriginalFilename();
            String fileName =  loginUser.getId()+ originalFileName+(new Date());

            //加密后的图片名
            String md5FileName = new Md5Hash(fileName).toHex();

            //保存进服务器图片目录的路径
            String filePath = uploadPathImg+md5FileName+originalFileName.substring(originalFileName.lastIndexOf("."));;
            //File.separator 相当于/或者\
            FileUtil.writeBytes(file.getBytes(), filePath);

            if (userService.modifyImgUrl(loginUser.getId(),filePath) > 0) {
                return ResponseMessage.newSuccessInstance("上传图像成功");
            } else {
                return ResponseMessage.newErrorInstance("上传图像失败");
            }
        } catch (Exception e) {
            //logger.error("上传图像失败: {}", e.getMessage());
            return ResponseMessage.newErrorInstance("上传图像失败");
        }
    }

    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public ResponseMessage logout(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            subject.logout();
            return ResponseMessage.newSuccessInstance("登出成功");
        }else{
            return ResponseMessage.newErrorInstance("登出失败");

        }
    }

    @ApiOperation("获取验证码")
    @GetMapping("/getImage")
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException, IOException {
        //生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //验证码放入session
        session.setAttribute("code",code);
        //验证码存入图片
        ServletOutputStream os = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(220,60,os,code);
    }

    @ApiOperation("修改密码")
    @PostMapping("/modifyPassword")
    public ResponseMessage modifyPassword(@RequestBody UserModifyPasswordReqDTO modifyPasswordReqDTO) throws ParseException {
        String originalPassword = modifyPasswordReqDTO.getOriginalPassword();
        String newPassword = modifyPasswordReqDTO.getNewPassword();
        String confirmPassword = modifyPasswordReqDTO.getConfirmPassword();
        System.out.println("旧密码:" + originalPassword);

        if(StrUtil.isEmpty(originalPassword) || StrUtil.isEmpty(newPassword) || StrUtil.isEmpty(confirmPassword)){
           return ResponseMessage.newErrorInstance("参数不能为空");
        }
        if(!newPassword.equals(confirmPassword)){
            return ResponseMessage.newErrorInstance("新密码和确认密码不一致");
        }
        //匹配8-16位，至少有一个大写字母和一个数字，不能有三个相同的字符，特殊字符包括
        if(!newPassword.matches("^(?=.*[A-Z])(?=.*[0-9])(?!.*([~!@&%$^\\(\\)#_]).*\\1.*\\1)[a-zA-Z0-9.~!@\";|:`&%$^\\(\\)#_]{8,16}$")){
            return ResponseMessage.newErrorInstance("新密码不符合格式要求");
        }
        Subject subject = SecurityUtils.getSubject();
        User curUser = (User) subject.getPrincipal();

        String encryptOldPwd = PasswordUtil.encryptPassword(curUser.getEmail(), originalPassword, curUser.getSalt());
        if(curUser.getPassword().equals(encryptOldPwd)){
            return ResponseMessage.newErrorInstance("旧密码不正确");
        }

        Integer update = userService.modifyPassword(curUser, newPassword);
        if(update >= 1){
            System.out.println("新密码:" + newPassword);
            //把新密码放入当用户信息里
            curUser.setPassword(encryptOldPwd);
           return ResponseMessage.newSuccessInstance("修改成功");
        }else{
           return ResponseMessage.newErrorInstance("修改失败");
        }

    }

    @ApiOperation(value = "获取用户信息")
    @PostMapping("/getUserInfo")
    public ResponseMessage getUserInfo(){
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        User userInfo = userService.getUserInfo(loginUser.getId());
        if(!ObjectUtil.isEmpty(userInfo)){
            return ResponseMessage.newSuccessInstance(userInfo,"获取用户信息成功");
        }else{
            return ResponseMessage.newErrorInstance("获取用户信息失败");
        }
    }
}
