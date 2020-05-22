package cn.allams.hkjforum;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 论坛C端消费者启动类
 *
 * @author Allams
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HkjForumCMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(HkjForumCMain8001.class, args);
    }
}
