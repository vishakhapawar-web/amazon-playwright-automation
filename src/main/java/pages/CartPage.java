package pages;

import com.microsoft.playwright.Page;

public class CartPage {

    private Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public boolean isSubtotalDisplayed() {
        return page.locator("#attach-accessory-cart-subtotal").isVisible()
                || page.locator("#sc-subtotal-label-activecart").isVisible();
    }

    public void goToCart() {
        page.click("a[href*='viewCart']");
    }

    public boolean verifyCartItem(String productName) {
        return page.locator("span:has-text('" + productName + "')").isVisible();
    }

    public boolean verifyQuantity(String quantity) {
        return page.locator("span:has-text('" + quantity + "')").isVisible();
    }
}