package com.github.myazusa.astrolithabackend.common.util;

import com.github.myazusa.astrolithabackend.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final byte[] secretKey = "ssdfddfevevefsggegjyjykghdhfhfhrverxciojweoijakdhfdoswe".getBytes();
    private final static String AUTH_HEADER = "Authorization";
    private final static String AUTH_HEADER_TYPE = "Bearer";

    /**
     * 生成一个token
     * @param userUUID 需要用户登录知道用户的uuid
     * @return token
     */
    public static String generateToken(String userUUID) {
        Map<String, Object> claims = new HashMap<>();
        long expirationTime = 1000L * 60 * 60 * 24 * 10;
        return Jwts.builder()
                .claims(claims)
                .subject(userUUID)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(Keys.hmacShaKeyFor(secretKey))
                .compact();
    }

    private static Claims parseToken(String token) {
        if (isValid(token)){
            if (token.contains("Bearer")){
                String s = token.replace("Bearer ", "");

                return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey))
                        .build().parseSignedClaims(s).getPayload();

            }
            return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }else {
            throw new InvalidTokenException();
        }
    }

    /**
     * 解包http的header
     * @param request http请求
     * @return 字符串形式的token
     */
    public static String extractTokenFromRequest(HttpServletRequest request){
        String header = request.getHeader(AUTH_HEADER);
        if (header != null && header.startsWith(AUTH_HEADER_TYPE)){
            return header.substring(7);
        }
        return null;
    }

    /**
     * 获得subject里存的信息
     * @param token
     * @return 里面存的字符串形式的信息
     */
    public static String extractSubject(String token) {
        return parseToken(token).getSubject();
    }

    private static boolean isValid(String token){
        if (token.contains("Bearer")){
            String s = token.replace("Bearer ", "");
            try{
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey))
                        .build().parseSignedClaims(s).getPayload();
                return true;
            }catch (Exception e){
                return false;
            }
        }else {
            try{
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                return true;
            }catch (Exception e){
                return false;
            }
        }
    }

    /**
     * 验证token是否过期
     * @param token
     * @return 过期就是true
     */
    public static boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}
