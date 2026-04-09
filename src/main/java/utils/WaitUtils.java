package utils;

import com.microsoft.playwright.Page;

public class WaitUtils {

    public static void waitForElement(Page page, String locator) {
        page.locator(locator).waitFor();
    }
}