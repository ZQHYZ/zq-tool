package com.zqhyz.config;

import cn.hutool.json.JSONUtil;
import com.zqhyz.annotation.IgnoreAuth;
import com.zqhyz.entity.R;
import com.zqhyz.filter.CaptchaFilter;
import com.zqhyz.filter.JwtAuthenticationFilter;
import com.zqhyz.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CaptchaFilter captchaFilter;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        handlerMethodMap.forEach((info, method) -> {
            if (method.getMethodAnnotation(IgnoreAuth.class) != null) {
                Objects.requireNonNull(info.getPathPatternsCondition()).getPatterns().forEach(pattern -> {
                    try {
                        http.authorizeHttpRequests(auth -> {
                            auth.requestMatchers(pattern.getPatternString()).permitAll();
                        }).httpBasic(Customizer.withDefaults());
                    } catch (Exception e) {
                        log.error("开放匿名接口失败");
                    }
                });
            }
        });

        http.authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api-docs", "swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/login")
                        .successHandler(this::handleProcess)
                        .failureHandler(this::handleProcess)
                )
                .logout(conf -> conf
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        .accessDeniedHandler(this::handleProcess)
                        .authenticationEntryPoint(this::handleProcess)
                )
//                .authenticationProvider(authenticationProvider())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(captchaFilter, JwtAuthenticationFilter.class)
//                .addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationFilter, RequestLogFilter.class)
                .build();

    }

    private void handleProcess(HttpServletRequest request,
                               HttpServletResponse response,
                               Object exceptionOrAuthentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(JSONUtil.toJsonStr(R.forbidden(exception.getMessage())));
        } else if (exceptionOrAuthentication instanceof Exception exception) {
            writer.write(JSONUtil.toJsonStr(R.unauthorized(exception.getMessage())));
        } else if (exceptionOrAuthentication instanceof Authentication authentication) {
            Account user = (Account) authentication.getPrincipal();
//            SysUser account = userService.lambdaQuery().eq(SysUser::getUsername,user.getUsername()).one();
            String jwt = jwtUtil.createJwt(user, user.getUsername(), user.getUserId());
            if (jwt == null) {
                writer.write(R.forbidden("登录验证频繁，请稍后再试").asJsonString());
            } else {
                AuthorizeVO vo = ConvertUtil.copyProperties(user, AuthorizeVO.class, o -> {
                    o.setToken(jwt);
                    o.setExpire(jwtUtil.expireTime());
                    o.setRole(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
                });
//                vo.setExpire(jwtUtil.expireTime());
//                vo.setRole(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
                // TODO 这里可以增加菜单、功能权限，也可以单独请求
                writer.write(R.ok(vo).asJsonString());
            }
        }
    }

    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (jwtUtil.invalidateJwt(authorization)) {
            writer.write(R.ok("退出登录成功").asJsonString());
            return;
        }
        writer.write(R.fail(400, "退出登录失败").asJsonString());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }}