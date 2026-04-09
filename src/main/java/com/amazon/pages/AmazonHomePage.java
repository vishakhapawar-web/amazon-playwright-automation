package com.amazon.pages;

import com.amazon.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class AmazonHomePage extends BasePage {

    public AmazonHomePage(Page page) {
        super(page);
    }

    public void searchProduct(String product) {
        page.locator("#twotabsearchtextbox").fill(product);
        page.locator("#nav-search-submit-button").click();
        Locator mainSlot = page.locator("div.s-main-slot");
        mainSlot.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }
}