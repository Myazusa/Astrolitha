package com.github.myazusa.astrolithabackend.common.exception.handler;

import com.github.myazusa.astrolithabackend.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("未知异常: {}", e.getMessage(), e);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("非预期的系统异常，请稍后再试");
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(Exception e) {
        log.error("数据库访问异常: {}", e.getMessage(), e);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("数据库操作失败");
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKeyException(Exception e) {
        log.error("数据库发生键冲突: {}", e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("数据已存在，不能重复插入");
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(Exception e){
        log.error("用户凭证无效: {}", e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("密码错误或手机号未注册");
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResourceFoundException(Exception e){

    }
    @ExceptionHandler(JsonConversionException.class)
    public ResponseEntity<String> handleJsonConversionException(Exception e){
        log.warn("Json文件实例化对象失败: {}",e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("服务器在转换json文件时出错");
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(Exception e){
        log.warn("无效令牌: {}",e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("令牌无效，禁止访问");
    }
    @ExceptionHandler(VectorDatabaseAccessException.class)
    public ResponseEntity<String> handleVectorDatabaseAccessException(Exception e){
        log.warn("向量数据库访问错误: {}",e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("向量数据库访问错误");
    }
    @ExceptionHandler(FileOperationException.class)
    public ResponseEntity<String> handleFileOperationException(Exception e){
        log.warn("文件操作错误: {}",e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("文件操作错误，"+e.getMessage());
    }
    @ExceptionHandler(RemoteServiceException.class)
    public ResponseEntity<String> handleRemoteServiceException(Exception e){
        log.error("远端服务错误: {}",e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("远端服务错误，"+e.getMessage());
    }
    @ExceptionHandler(InvalidAgentException.class)
    public ResponseEntity<String> handlerInvalidAgentException(Exception e){
        log.error("无效Agent: {}",e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("无效Agent，"+e.getMessage());
    }
    @ExceptionHandler(InjectException.class)
    public ResponseEntity<String> handlerInjectException(Exception e){
        log.error("方法注入失败: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("方法注入失败，"+e.getMessage());
    }

    @ExceptionHandler(UnknownException.class)
    public ResponseEntity<String> handleUnknownException(Exception e){
        log.error("未知错误: {}",e.getMessage());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误，"+e.getMessage());
    }
}
