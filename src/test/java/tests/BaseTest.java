package base;

import com.microsoft.playwright.*;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    public void setUp() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );

        BrowserContext context = browser.newContext();
        page = context.newPage();
    }

    public void tearDown() {
        browser.close();
        playwright.close();
    }
}