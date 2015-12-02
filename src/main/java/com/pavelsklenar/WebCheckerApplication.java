package com.pavelsklenar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebCheckerApplication {

    public static void main(String[] args) {
        //SpringApplication.run(DemoApplication.class, args);
    	
    	SpringApplication.run(TestApplication.class);
    }
}
