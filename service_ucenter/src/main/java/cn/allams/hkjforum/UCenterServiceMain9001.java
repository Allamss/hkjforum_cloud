package cn.allams.hkjforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户中心启动类
 *
 * @author Allams
 */
@ComponentScan({"cn.allams"})
@SpringBootApplication
@MapperScan("cn.allams.hkjforum.mapper")
public class UCenterServiceMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(UCenterServiceMain9001.class, args);
    }
}
