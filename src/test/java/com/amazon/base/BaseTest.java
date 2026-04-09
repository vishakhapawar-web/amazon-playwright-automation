package com.amazon.base;

import com.amazon.factory.PlaywrightFactory;
import com.amazon.utils.ConfigReader;
import com.microsoft.playwright.Page;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected Page page;
    protected PlaywrightFactory factory;
    protected ConfigReader config;

    @BeforeMethod
    public void setup() {
        config = new ConfigReader();
        factory = new PlaywrightFactory();
        page = factory.initBrowser(config.getBrowser(), config.isHeadless());
        page.setDefaultTimeout(config.getDefaultTimeoutMs());
        page.navigate(config.getUrl());
    }

    @AfterMethod
    public void tearDown() {
        factory.closeBrowser();
    }
}