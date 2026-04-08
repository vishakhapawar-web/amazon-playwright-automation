package pages;

import com.microsoft.playwright.Page;

public class AmazonHomePage {

    private Page page;

    public AmazonHomePage(Page page) {
        this.page = page;
    }

    private String searchBox = "#twotabsearchtextbox";
    private String searchButton = "#nav-search-submit-button";

    public void openAmazon(String url) {
        page.navigate(url);
    }

    public void searchProduct(String product) {
        page.fill(searchBox, product);
        page.click(searchButton);
    }
}