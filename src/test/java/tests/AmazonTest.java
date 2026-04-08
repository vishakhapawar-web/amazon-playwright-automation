package tests;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AmazonTest {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    public void setUp() {

        // Start Playwright
        playwright = Playwright.create();

        // Launch browser
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false) // visible browser
        );

        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    public void amazonShoppingTest() {

        // Open Amazon
        page.navigate("https://www.amazon.in");

        // Search for product
        page.locator("#twotabsearchtextbox").fill("laptop");
        page.locator("#nav-search-submit-button").click();

        // Wait for results to load
        page.waitForSelector(".s-result-item");

        // Select products using Playwright locator (SAFE WAY)
        Locator products = page.locator(".s-result-item[data-component-type='s-search-result']");

        int count = products.count();
        System.out.println("Products found: " + count);

        // Safety check
        if (count > 1) {
            // Click second product (index starts at 0)
            products.nth(1).click();
        } else {
            throw new RuntimeException("Not enough products found!");
        }

        // Wait few seconds to observe
        page.waitForTimeout(5000);
    }

    @AfterClass
    public void tearDown() {

        context.close();
        browser.close();
        playwright.close();
    }
}