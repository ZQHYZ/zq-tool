package com.zqhyz.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    //用于给Jwt令牌签名校验的秘钥
    @Value("${jwt.key}")
    private String key;
    //令牌的过期时间，以小时为单位
    @Value("${jwt.expire}")
    private int expire;

    private final StringRedisTemplate template;


    /**
     * 生成令牌
     */
    public String generateJwt(String username) {
        String jwt = JWT.create()
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(key));
        template.opsForValue().set(Const.JWT_ACCOUNT + username, jwt, expire * 60 * 60L, TimeUnit.SECONDS);
        return jwt;
    }
}
