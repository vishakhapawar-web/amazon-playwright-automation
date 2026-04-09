package com.amazon.factory;

import com.microsoft.playwright.*;

public class PlaywrightFactory {

    private Playwright playwright;
    private Browser browser;

    public Page initBrowser(String browserName, boolean headless) {
        playwright = Playwright.create();
        BrowserType.LaunchOptions opts = new BrowserType.LaunchOptions().setHeadless(headless);

        String name = browserName == null ? "chromium" : browserName.trim().toLowerCase();
        switch (name) {
            case "firefox":
                browser = playwright.firefox().launch(opts);
                break;
            case "webkit":
                browser = playwright.webkit().launch(opts);
                break;
            default:
                browser = playwright.chromium().launch(opts);
                break;
        }

        BrowserContext context = browser.newContext();
        return context.newPage();
    }

    public void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}