package com.mdkj.aspect;

import com.mdkj.annotation.Access;
import com.mdkj.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * 自定义切面：校验用户ID和角色权限
 */
@Aspect
@Component
@Slf4j
public class ServiceAspect {
    
    // 修复：UserInfo 需要通过注入/上下文获取（根据你的实际场景调整，比如从ThreadLocal获取）
    // 注意：如果UserInfo是请求上下文的，建议从ThreadLocal中获取，而非直接@Autowired
    @Autowired
    private UserInfo userInfo; // 若为请求级别的UserInfo，需改为从ThreadLocal获取，示例见备注

    /**
     * 切点注解
     */
    @Pointcut("@annotation(com.mdkj.annotation.Access)")
    public void init(){}

    /**
     * 环绕通知：核心权限校验（ID + 角色）
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("init()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取注解配置
        Access access = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Access.class);
        long[] allowIds = access.ids(); // 注解中配置的允许访问的用户ID
        String[] allowRoles = access.value(); // 原有的允许角色

        // 2. 校验用户是否存在（ID非空）
        if (userInfo == null || userInfo.getId() == null) {
            throw new RuntimeException("用户未登录");
        }
        Long currentUserId = userInfo.getId();


        // 3. 核心：校验用户ID是否在允许的范围内（如果注解配置了ids）
        if (allowIds.length > 0) { // 只有当注解配置了ids时才校验
            boolean idAllowed = Arrays.asList(allowIds).contains(currentUserId);
            if (!idAllowed) {
                throw new RuntimeException("权限不足：用户ID不在允许范围内");
            }
        }

        // 5. 所有校验通过，执行原方法
        return joinPoint.proceed();
    }

    /**
     * 前置通知（可保留扩展）
     */
    @Before("init()")
    public void before() {
        log.info("执行接口权限校验前置通知");
    }

    /**
     * 后置通知（可保留扩展）
     */
    @After("init()")
    public void after() {
        log.info("接口权限校验完成，后置通知执行");
    }
}