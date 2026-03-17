package com.mdkj.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行登录接口
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/manager/login") ) {
            return true;
        }

        // 👇 改回原来的逻辑：直接把 Authorization 当作 UUID
        String uuid = request.getHeader("Authorization");
        if (uuid == null || uuid.trim().isEmpty()) {
            returnJson(response, "请求头为空，请登录");
            return false;
        }

        // 用 UUID 去 Redis 取 token
        String token = redisTemplate.opsForValue().get(uuid);
        if (token == null) {
            returnJson(response, "令牌失效，请登录");
            return false;
        }

        // 校验 JWT（如果需要）
        try {
            jwtUtil.parseToken(token);
        } catch (Exception e) {
            returnJson(response, "令牌失效，请登录");
            return false;
        }

        return true;
    }

    private void returnJson(HttpServletResponse response, String msg) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("msg", msg);
        result.put("data", null);

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}