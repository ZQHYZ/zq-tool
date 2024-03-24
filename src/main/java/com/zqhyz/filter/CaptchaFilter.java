package com.zqhyz.filter;

import cn.hutool.json.JSONUtil;
import com.zqhyz.entity.R;
import com.zqhyz.exception.CaptchaException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {

    private final StringRedisTemplate template;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if ("/login".equals(uri) && "POST".equals(request.getMethod())) {
            String code = request.getParameter("code");
            String key = request.getParameter("token");

            if (ObjectUtils.isEmpty(code) || ObjectUtils.isEmpty(key)) {
                template.delete(key);
                AuthenticationException exception = new CaptchaException("验证码错误");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSONUtil.toJsonStr(R.result(10000, exception.getMessage(), null)));
                return;
            }

            if (!code.equals(template.opsForValue().get(key))) {
                template.delete(key);
                AuthenticationException exception = new CaptchaException("验证码错误");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSONUtil.toJsonStr(R.result(10000, exception.getMessage(), null)));
                return;
            }
            template.delete(key);
        }
        filterChain.doFilter(request, response);
    }
}
