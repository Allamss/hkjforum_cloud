package cn.allams.hkjforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 论坛C端消费者启动类
 *
 * @author Allams
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class HkjForumCMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(HkjForumCMain8001.class, args);
    }
}
