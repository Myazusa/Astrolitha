package com.github.myazusa.astrolithabackend.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class SecurityUtils {
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 使用spring的bcrypt计算加密
     * @param plainText 需要加密的明文
     * @return 返回一个bcrypt加密的密文
     */
    public static String calculateBCryptHash(String plainText){
        return bCryptPasswordEncoder.encode(plainText);
    }

    /**
     * 比对明文是否和密文一致
     * @param plainText 要比对的明文
     * @param cipherText 要比对的密文
     * @return 一致为true，否则false
     */
    public static boolean verifyBCryptHash(String plainText, String cipherText){
        return bCryptPasswordEncoder.matches(plainText,cipherText);
    }

    /**
     * 校验是否空指针且空字符
     * @param s 字符串
     * @return 是则true
     */
    public static boolean verifyObjectNullBlankEmpty(String s){
        if (Optional.ofNullable(s).filter(String::isEmpty).filter(String::isBlank).isEmpty()){
            return false;
        }
        return true;
    }
}
