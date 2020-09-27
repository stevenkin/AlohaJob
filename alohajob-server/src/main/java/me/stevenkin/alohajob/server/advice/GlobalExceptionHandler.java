package me.stevenkin.alohajob.server.advice;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Response exceptionHandler(HttpServletRequest req, Exception e){
        return Response.failed(e.getMessage());
    }
}
