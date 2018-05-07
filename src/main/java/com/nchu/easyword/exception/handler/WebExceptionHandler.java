package com.nchu.easyword.exception.handler;

import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2018-4-19 14:10:47
 * web层统一异常处理器
 */
@ControllerAdvice
public class WebExceptionHandler {
    /*系统异常处理*/
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseDTO exceptionHandler(Exception exception, HttpServletResponse response, HttpServletRequest request) {
        /*这里不能通过@RequestAttribute注解获取统一返回对象因为当发生请求本身异常或系统异常时request对象会被丢弃,会出现空指针异常*/
        ResponseDTO responseDTO = new ResponseDTO();
        /*跨域配置*/
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("P3P", "CP=CAO PSA OUR");
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
            response.addHeader("Access-Control-Max-Age", "120");
        }
        exception.printStackTrace();
        return responseDTO.setMessage(exception.getMessage()).setStatusCode(StatusCode.SYSTEM_ERROR);
    }

    /*业务异常处理*/
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    ResponseDTO ServiceExceptionHandler(ServiceException serviceException, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        System.out.println("业务异常：" + serviceException.getMsg() + serviceException.getStatusCode());
        return responseDTO.setMessage(serviceException.getMsg()).setStatusCode(serviceException.getStatusCode());
    }
}
