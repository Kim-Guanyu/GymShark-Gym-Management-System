package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mdkj.domain.Manager;
import com.mdkj.service.LoginService;
import com.mdkj.service.ManagerService;
import com.mdkj.util.JwtUtil;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(String name, String password) {
        // 1. 严格的参数非空校验（避免空指针和空字符串）
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }

        // 2. 根据用户名查询用户（仅查用户名，逻辑更清晰）
        Manager queryManager = new Manager();
        queryManager.setName(name.trim()); // 去除首尾空格，避免输入" 不做小团团 "导致查询失败
        List<Manager> managerList = managerService.selectList(queryManager);

        // 3. 校验用户是否存在
        if (managerList.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }

        // 4. 取第一个用户（建议数据库给name加唯一索引，确保只有一个结果）
        Manager dbManager = managerList.get(0);

        // 5. 明文比对密码（移除原代码中无意义的manager.setPassword(password)）
        if (!password.trim().equals(dbManager.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 6. 生成token并缓存到Redis（补充过期时间，避免Redis内存溢出）
        String token = JwtUtil.createToken(dbManager.getName());
        String uuid = IdWorker.get32UUID(); // 变量名小写，符合Java命名规范
        // 给Redis缓存加24小时过期时间（可选，根据业务调整）
        redisTemplate.opsForValue().set(uuid, token, 24, java.util.concurrent.TimeUnit.HOURS);

        return uuid;
    }
}
