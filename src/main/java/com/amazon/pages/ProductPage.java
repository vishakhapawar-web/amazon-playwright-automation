package com.amazon.pages;

import com.amazon.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProductPage extends BasePage {

    private static final String QUANTITY = "#quantity";
    private static final String ADD_TO_CART = "#add-to-cart-button";

    public ProductPage(Page page) {
        super(page);
    }

    /**
     * PDP loaded: URL looks like product page, or buy box / title visible after redirects (e.g. sspa).
     */
    public boolean isProductPageLoaded() {
        if (matchesPdpUrl(page.url())) {
            return true;
        }
        try {
            page.waitForURL(
                    ProductPage::matchesPdpUrl,
                    new Page.WaitForURLOptions().setTimeout(25_000));
            return true;
        } catch (TimeoutError e) {
            Locator marker = page.locator(
                    "#add-to-cart-button, #productTitle, #title, #quantity");
            try {
                marker.first().waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10_000));
                return marker.first().isVisible();
            } catch (TimeoutError e2) {
                return false;
            }
        }
    }

    private static boolean matchesPdpUrl(String url) {
        if (url == null) {
            return false;
        }
        String u = url.toLowerCase();
        return u.contains("/dp/")
                || u.contains("/gp/product/")
                || u.contains("/gp/aw/");
    }

    public void selectQuantity(String quantity) {
        Locator qty = page.locator(QUANTITY);
        qty.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        qty.selectOption(quantity);
    }

    public void addToCart() {
        page.locator(ADD_TO_CART).click();
    }

    public void clickGoToCart() {
        Locator goToCart = page.locator("#sw-gtc")
                .or(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Go to Cart")))
                .or(page.locator("a:has-text('Go to Cart')"));
        goToCart.first().waitFor();
        goToCart.first().click();
        page.waitForURL(url -> url.contains("/cart")
                || url.contains("viewcart")
                || url.contains("/gp/cart"));
    }
}