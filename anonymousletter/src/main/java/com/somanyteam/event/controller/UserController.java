package com.somanyteam.event.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.somanyteam.event.dto.request.user.UserLoginReqDTO;
import com.somanyteam.event.dto.request.user.UserModifyPasswordReqDTO;
import com.somanyteam.event.dto.request.user.UserUpdateInfoReqDTO;
import com.somanyteam.event.dto.result.user.UserGetInfoResult;
import com.somanyteam.event.dto.result.user.UserLoginResult;
import com.somanyteam.event.entity.User;
import com.somanyteam.event.enums.UserType;
import com.somanyteam.event.exception.user.FileNotMatchException;
import com.somanyteam.event.exception.user.VerifyCodeNotMatchException;
import com.somanyteam.event.service.UserService;
import com.somanyteam.event.shiro.AccountProfile;
import com.somanyteam.event.util.JwtUtils;
import com.somanyteam.event.util.PasswordUtil;
import cn.hutool.core.util.ObjectUtil;
import com.somanyteam.event.dto.request.user.UserForgetPwdReqDTO;
import com.somanyteam.event.dto.request.user.UserRegisterReqDTO;
import com.somanyteam.event.exception.user.UserEnterEmptyException;
import com.somanyteam.event.util.ResponseMessage;
import com.somanyteam.event.util.Result;
import com.somanyteam.event.util.ShiroUtil;
import com.somanyteam.event.util.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
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

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    @ApiOperation("登录")
    public ResponseMessage login(@Validated @RequestBody UserLoginReqDTO dto, HttpServletResponse response, HttpSession session) {
        User user = userService.getByEmail(dto.getEmail());
        Assert.notNull(user, "用户不存在");//断言拦截
        //判断账号密码是否错误 因为是md5加密所以这里md5判断
        if (!user.getPassword().equals(PasswordUtil.encryptPassword(user.getId(), dto.getPassword(), user.getSalt()))) {
            //密码不同则抛出异常
            return ResponseMessage.newErrorInstance("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());

        //将token 放在我们的header里面
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        return ResponseMessage.newSuccessInstance(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("imgUrl", user.getImgUrl())
                .put("email", user.getEmail()).map()

        );
    }

    //需要认证权限才能退出登录
    @RequiresAuthentication
    @GetMapping("/logout")
    @ApiOperation("登出")
    public ResponseMessage logout() {
        System.out.println("=========");
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.isAuthenticated());
        //退出登录
        SecurityUtils.getSubject().logout();
        return ResponseMessage.newSuccessInstance("登出成功");
    }

//    @ApiOperation(value = "用户登录")
//    @PostMapping("/login")
//    public ResponseMessage<UserLoginResult> login(@RequestBody @Validated UserLoginReqDTO userLoginReqDTO, HttpSession session){
////        String rightCode = (String) session.getAttribute("code");
////
////        if(!rightCode.equals(userLoginReqDTO.getCode())){
////            throw new VerifyCodeNotMatchException();
////        }
//        Subject subject = SecurityUtils.getSubject();
//
//        UsernamePasswordToken token  = new UsernamePasswordToken(userLoginReqDTO.getEmail(), userLoginReqDTO.getPassword());
//        if(userLoginReqDTO.getRememberMe() != null && userLoginReqDTO.getRememberMe() == 1) {
//            token.setRememberMe(true);
//        }
//        try {
//            subject.login(token);
//
//            User loginUser = (User) subject.getPrincipal();
//            UserLoginResult result = new UserLoginResult();
//            BeanUtils.copyProperties(loginUser, result);
//            return ResponseMessage.newSuccessInstance(result, "登录成功");
//        } catch (AuthenticationException e){
//            String msg = "用户名或密码错误";
//            if (StrUtil.isNotEmpty(e.getMessage())) {
//                msg = e.getMessage();
//            }
//            return ResponseMessage.newErrorInstance("登陆异常: " + msg);
//        }
//
//
//    }

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
        user.setIdentity(UserType.NORMAL.getCode());
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

    @ApiOperation(value = "发送邮件")
    @PostMapping("/sendEmail")
    public ResponseMessage sendEmail(String email) {
        String content = "hello 欢迎访问匿名信网站，您的验证码为:";
        int i = userService.sendEmail(email,content);
        if (i==0) {
            return ResponseMessage.newErrorInstance("发送失败");
        } else {
            return ResponseMessage.newSuccessInstance("发送成功");
        }
    }

    //static/827ce27cbd5edaf1d3249552c3cb7911.png
    @RequiresAuthentication
    @ApiOperation("上传头像")
    @PostMapping("/uploadPhoto")
    public ResponseMessage uploadPhoto(@RequestPart("file") MultipartFile file) {

        User loginUser = ShiroUtil.getUser();

        try {
            //保存图片的名字为用户id+图片名称+上传图片时间
            String originalFileName = file.getOriginalFilename();
            String houzui = originalFileName.substring(originalFileName.lastIndexOf("."));
            if (!houzui.equals(".png") || !houzui.equals(".jpg")){
                //文件格式不对
                throw new FileNotMatchException();
            }
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

//    @ApiOperation(value = "登出")
//    @PostMapping("/logout")
//    public ResponseMessage logout(){
//        Subject subject = SecurityUtils.getSubject();
//        //正常登录或者使用cookie登录时都可以选择登出
//        if(subject.isAuthenticated() || subject.isRemembered()){
//            subject.logout();
//            return ResponseMessage.newSuccessInstance("登出成功");
//        }else{
//            return ResponseMessage.newErrorInstance("登出失败");
//
//        }
//    }

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

    @RequiresAuthentication
    @ApiOperation("修改密码")
    @PostMapping("/modifyPassword")
    public ResponseMessage modifyPassword(@RequestBody UserModifyPasswordReqDTO modifyPasswordReqDTO) throws ParseException {

        Subject subject = SecurityUtils.getSubject();
        AccountProfile profile = new AccountProfile();
        Object principal = subject.getPrincipal();
        BeanUtils.copyProperties(principal, profile);
        System.out.println(principal);
        Integer update = userService.modifyPassword(profile, modifyPasswordReqDTO);
        if(update >= 1){
           return ResponseMessage.newSuccessInstance("修改成功");
        }else{
           return ResponseMessage.newErrorInstance("修改失败");
        }

    }

    @RequiresAuthentication
    @ApiOperation(value = "获取用户信息")
    @PostMapping("/getUserInfo")
    public ResponseMessage getUserInfo() throws ParseException {

        User loginUser = ShiroUtil.getUser();

        User userInfo = userService.getUserInfo(loginUser.getId());
        UserGetInfoResult result = new UserGetInfoResult();
        BeanUtils.copyProperties(userInfo,result);
        if(!ObjectUtil.isEmpty(userInfo)){
            return ResponseMessage.newSuccessInstance(result,"获取用户信息成功");
        }else{
            return ResponseMessage.newErrorInstance("获取用户信息失败");
        }
    }

    @RequiresAuthentication
    @ApiOperation("注销账号")
    @GetMapping("/deleteAccount")
    public ResponseMessage deleteAccount(){
        return ResponseMessage.newSuccessInstance(userService.deleteAccount( ShiroUtil.getUser()));
    }

//    @ApiOperation("测试是用cookie登录还是正常登录")
//    @GetMapping("/test")
//    public ResponseMessage test(){
//        Subject subject = SecurityUtils.getSubject();
////        User user = (User) subject.getPrincipal();
//
//        if(subject.isAuthenticated()){
//            System.out.println("用户正常登录");
//        }else if(subject.isRemembered()){
//            System.out.println("cookie登录");
//        }else{
//            System.out.println("没登录");
//        }
//        return ResponseMessage.newSuccessInstance("测试");
//    }


    @RequiresAuthentication
    @ApiOperation("修改用户信息")
    @PostMapping("/updateInfo")
    public ResponseMessage updateInfo(@RequestBody UserUpdateInfoReqDTO dto) throws ParseException {

        return ResponseMessage.newSuccessInstance(userService.updateInfo(dto, ShiroUtil.getUser()));
    }

}
