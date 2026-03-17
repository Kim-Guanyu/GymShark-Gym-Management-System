package com.mdkj;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAdminServer
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringBootAdminApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminApp.class, args);
        System.out.println("监控启动成功!!!");
    }
}
