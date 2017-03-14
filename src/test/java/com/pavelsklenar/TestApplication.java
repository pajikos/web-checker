package com.pavelsklenar;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.pavelsklenar.service.WebDriverFactory;

@SpringBootApplication
public class TestApplication {

    @Value("${httpProxy.url:}")
    private String httpProxyUrl;

    @Value("${httpProxy.port:3128}")
    private Integer httpProxyPort;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("SMTP");
        javaMailSender.setHost("127.0.0.1");
        javaMailSender.setPort(25);
        return javaMailSender;
    }

    @Bean
    public WebDriverFactory createWebDriverFactory() {
        return new WebDriverFactory() {
            @Override
            public WebDriver getWebDriver() {
                HtmlUnitDriver driver = new HtmlUnitDriver(false);
                if (httpProxyUrl != null && !httpProxyUrl.isEmpty()) {
                    ArrayList<String> proxyIgnore = new ArrayList<String>();
                    proxyIgnore.add("localhost");
                    driver.setHTTPProxy(httpProxyUrl, httpProxyPort, proxyIgnore);
                }
                return driver;
            }
        };
    }
}
