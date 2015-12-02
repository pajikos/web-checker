package com.pavelsklenar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main class to run the WebChecker application
 * @author pajik
 *
 */
@SpringBootApplication
@EnableScheduling
public class WebCheckerApplication {

    public static void main(String[] args) {
    	SpringApplication.run(WebCheckerApplication.class);
    }
}
