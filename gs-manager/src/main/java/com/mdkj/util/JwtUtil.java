package com.mdkj.util;

import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET = "secret";

    /**
     * 创建令牌*/

    public static String createToken(String username) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", username);
        payloads.put("iat", new Date());
        payloads.put("exp", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 过期时间：24小时后

        return JWTUtil.createToken(payloads, JWTSignerUtil.hs256(SECRET.getBytes()));
    }

    /**
     * 获取登录人姓名*/
    public static String getUsername(String token) {
        JWTSigner signer = JWTSignerUtil.hs256(SECRET.getBytes());
        return JWTUtil.parseToken(token).setSigner(signer).getPayload().getClaim("username").toString();
    }

    /**
     * 验证令牌*/
    public static boolean parseToken(String token) {
            // 前置校验：token为空直接返回验证失败
            if (token == null || token.trim().isEmpty()) {
                throw new CustomException("令牌不能为空");
            }
            JWTSigner signer = JWTSignerUtil.hs256(SECRET.getBytes());
            try {
                // 核心：调用verify()方法触发签名校验，这是验证的关键
                boolean verify = JWTUtil.parseToken(token).setSigner(signer).verify();
                // 校验失败直接抛异常
                if (!verify) {
                    throw new CustomException("令牌签名无效");
                }
            } catch (Exception e) {
                // 其他异常（如格式错误、过期）统一封装为令牌验证失败
                throw new CustomException("令牌验证失败：" + e.getMessage());
            }
            System.out.println("令牌验证成功");
            return true;
        }
}
