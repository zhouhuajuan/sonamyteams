//package com.somanyteam.event.handler;
//
//import com.somanyteam.event.util.ResponseMessage;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
///**
// * 全局异常处理
// * @author zbr
// */
//@RestControllerAdvice
//public class ControllerExceptionHandler {
//
//    private static final Logger logger = LoggerFactory.getLogger(com.somanyteam.event.handler.ControllerExceptionHandler.class);
//
//    @ExceptionHandler(value = RuntimeException.class)
//    public ResponseMessage<Object> runTimeExceptionHandler(RuntimeException e){
////        ResponseMessage<Object> responseMessage;
//        e.printStackTrace();
//        logger.info("异常信息:" +e.getMessage());
//        return ResponseMessage.newErrorInstance(e.getMessage());
//
//    }
//}
