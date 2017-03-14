package com.pavelsklenar;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.pavelsklenar.service.WebDriverFactory;

/**
 * The main class to run the WebChecker application
 *
 * @author pavel.sklenar
 *
 */
@SpringBootApplication
@EnableScheduling
public class WebCheckerApplication {

    @Value("${httpProxy.url:}")
    private String httpProxyUrl;

    @Value("${httpProxy.port:3128}")
    private Integer httpProxyPort;

    public static void main(String[] args) {
        SpringApplication.run(WebCheckerApplication.class);
    }

    @Bean
    public WebDriverFactory createWebDriverFactory() {
        return new WebDriverFactory() {
            @Override
            public WebDriver getWebDriver() {
                ArrayList<String> cliArgsCap = new ArrayList<String>();
                if (httpProxyUrl != null && !httpProxyUrl.isEmpty()) {
                    cliArgsCap.add("--proxy=" + httpProxyUrl + ":" + httpProxyPort);
                    cliArgsCap.add("--proxy-type=http");
                }
                DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
                capabilities.setJavascriptEnabled(true);
                capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
                return new PhantomJSDriver(capabilities);
            }
        };
    }



}
