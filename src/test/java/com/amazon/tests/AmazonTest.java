package com.amazon.tests;

import com.amazon.base.BaseTest;
import com.amazon.listeners.TestListener;
import com.amazon.pages.AmazonHomePage;
import com.amazon.pages.CartPage;
import com.amazon.pages.ProductPage;
import com.amazon.pages.SearchResultsPage;
import com.amazon.utils.TestDataReader;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
public class AmazonTest extends BaseTest {

    @Test(description = "HP Smart Tank search, PDP, qty 2, add to cart, verify cart")
    public void amazonShoppingTest() {

        TestDataReader data = new TestDataReader();

        String searchProduct = data.getData("amazonTestData", "searchProduct");
        String productName = data.getData("amazonTestData", "productName");
        String quantity = data.getData("amazonTestData", "quantity");
        String expectedSubtotalContains = data.getData("amazonTestData", "expectedSubtotalContains");

        AmazonHomePage homePage = new AmazonHomePage(page);
        homePage.searchProduct(searchProduct);

        SearchResultsPage resultsPage = new SearchResultsPage(page);
        Assert.assertTrue(resultsPage.isResultsDisplayed(), "Search results container not visible");
        Assert.assertTrue(resultsPage.hasSearchResults(), "No search results returned");

        Page productPageTab = resultsPage.selectProduct(productName);

        ProductPage productPage = new ProductPage(productPageTab);
        Assert.assertTrue(productPage.isProductPageLoaded(), "Product page did not open");

        productPage.selectQuantity(quantity);
        productPage.addToCart();
        productPage.clickGoToCart();

        CartPage cartPage = new CartPage(productPageTab);
        Assert.assertTrue(cartPage.isCartDisplayed(), "Shopping cart page did not open");

        String subtotal = cartPage.getSubtotal();
        Assert.assertFalse(subtotal.isEmpty(), "Cart subtotal is empty");
        Assert.assertTrue(subtotal.contains(expectedSubtotalContains),
                "Subtotal does not contain expected text: " + expectedSubtotalContains + " | actual: " + subtotal);

        String cartTitle = cartPage.getFirstItemTitle();
        Assert.assertTrue(
                cartTitle.contains(productName) || cartTitle.toLowerCase().contains(productName.toLowerCase()),
                "Cart item title mismatch. Expected to contain: " + productName + " | actual: " + cartTitle);

        String qtyLabel = cartPage.getFirstItemQuantityLabel();
        Assert.assertTrue(qtyLabel.contains(quantity),
                "Cart quantity mismatch. Expected: " + quantity + " | actual: " + qtyLabel);
    }
}