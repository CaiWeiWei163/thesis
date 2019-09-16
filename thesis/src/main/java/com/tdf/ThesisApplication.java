package com.tdf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.tdf", "com.tdf.service", "com.tdf.service.sys", "com.tdf.service.thesis"}, exclude = SecurityAutoConfiguration.class)
@MapperScan(basePackages = {"com.tdf.dao", "com.tdf.dao.*"})
@ServletComponentScan
/*war打包方式*/
public class ThesisApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ThesisApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ThesisApplication.class);
    }
}

/*jar 打包方式*/
//public class ThesisApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(ThesisApplication.class, args);
//	}
//}