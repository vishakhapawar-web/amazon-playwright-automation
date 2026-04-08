package pages;

import com.microsoft.playwright.Page;

public class ProductPage {

    private Page page;

    public ProductPage(Page page) {
        this.page = page;
    }

    public boolean isProductPageOpened(String productName) {
        return page.title().contains(productName);
    }

    public void selectQuantity(String quantity) {
        page.selectOption("#quantity", quantity);
    }

    public void addToCart() {
        page.click("#add-to-cart-button");
    }
}