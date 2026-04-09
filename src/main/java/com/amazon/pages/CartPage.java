package com.amazon.pages;

import com.amazon.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CartPage extends BasePage {

    /** Line that actually contains a product title (skips headers / promos). */
    private static final String PRODUCT_LINE =
            ".sc-list-item:has(.sc-product-title), .sc-list-item:has([data-action='delete-active'])";

    public CartPage(Page page) {
        super(page);
    }

    public boolean isCartDisplayed() {
        String url = page.url();
        return url.contains("/cart") || url.contains("viewcart") || url.contains("/gp/cart");
    }

    public String getSubtotal() {
        Locator sub = page.locator(
                "#sc-subtotal-amount-activecart, #sc-subtotal-amount-buybox, span[data-testid='cart-subtotal']");
        sub.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return sub.first().innerText().trim();
    }

    public String getFirstItemTitle() {
        Locator title = page.locator(".sc-product-title, span.sc-product-title, .sc-list-item .a-truncate-cut")
                .first();
        title.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return title.innerText().trim();
    }

    /**
     * Quantity on cart lines: dropdown prompt, {@code select}, or {@code input} (layout varies by locale/A-B).
     */
    public String getFirstItemQuantityLabel() {
        Locator row = page.locator(PRODUCT_LINE).first();
        row.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15_000));

        Locator qty = row.locator(
                "select[name='quantity'], "
                        + "select.sc-quantity-select, "
                        + "select[data-a-selector='quantity'], "
                        + "span.a-dropdown-prompt, "
                        + "input.sc-quantity-textfield, "
                        + "[data-a-selector='value']");

        qty.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(20_000));

        Locator first = qty.first();
        String tag = first.evaluate("el => el.tagName").toString();
        if ("SELECT".equalsIgnoreCase(tag) || "INPUT".equalsIgnoreCase(tag)) {
            return first.inputValue();
        }
        return first.innerText().trim();
    }
}