package com.pavelsklenar.service;

import org.openqa.selenium.WebDriver;

/**
 * Interface for Factory for {@link WebDriver} instances
 * @author pavel.sklenar
 *
 */
public interface WebDriverFactory {

    /**
     * Get {@link WebDriver} instance, ready to use
     * @return existing and working {@link WebDriver} instance
     */
    WebDriver getWebDriver();

}
