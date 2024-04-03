package com.zqhyz.handler;

import cn.hutool.json.JSONUtil;
import com.zqhyz.entity.User;
import com.zqhyz.util.Const;
import com.zqhyz.util.ConvertUtil;
import com.zqhyz.util.JwtUtil;
import com.zqhyz.vo.LoginVo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${jwt.expire}")
    private int expire;

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate template;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = JSONUtil.parseObj(authentication.getPrincipal()).toBean(User.class);
        String jwt = jwtUtil.generateJwt(user.getUsername());
        template.opsForValue().set(Const.JWT_ACCOUNT, jwt, expire * 60 * 60L, TimeUnit.SECONDS);
        LoginVo loginVo = ConvertUtil.dynamicConstructure(LoginVo.class, vo -> {
            vo.setUser(user);
            vo.setToken(jwt);
        });
        response.getWriter().write(ObjectUtils.getDisplayString(loginVo));
    }
}
