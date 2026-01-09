package com.itembay.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }

    /**
     * 캐치되지 못한 예외를 처리하기 위한 Handler
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(Exception e) {
        // 개발자가 예상하지 못한 에러는 log로 남기고 사용자에게는 오류 메세지를 보여준다.
        log.error("handleException", e);
        return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
    }
}
