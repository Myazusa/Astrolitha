package com.github.myazusa.astrolithabackend.exception.handler;

import com.github.myazusa.astrolithabackend.exception.InvalidTokenException;
import com.github.myazusa.astrolithabackend.exception.JsonConversionException;
import com.github.myazusa.astrolithabackend.exception.VectorDatabaseAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("未知异常: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("非预期的系统异常，请稍后再试");
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(Exception e) {
        log.error("数据库访问异常: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("数据库操作失败");
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKeyException(Exception e) {
        log.error("数据库发生键冲突: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("数据已存在，不能重复插入");
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(Exception e){
        log.error("用户凭证无效: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("密码错误或手机号未注册");
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResourceFoundException(Exception e){

    }
    @ExceptionHandler(JsonConversionException.class)
    public ResponseEntity<String> handleJsonConversionException(Exception e){
        log.warn("Json文件实例化对象失败: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("服务器在转换json文件时出错");
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(Exception e){
        log.warn("无效令牌: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("令牌无效，禁止访问");
    }
    @ExceptionHandler(VectorDatabaseAccessException.class)
    public ResponseEntity<String> handleVectorDatabaseAccessException(Exception e){
        log.warn("向量数据库访问错误: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("向量数据库访问错误");
    }
}
