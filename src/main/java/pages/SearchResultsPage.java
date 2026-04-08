package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class SearchResultsPage {

    private Page page;

    public SearchResultsPage(Page page) {
        this.page = page;
    }

    private String searchResults = "[data-component-type='s-search-result']";

    public boolean isSearchResultDisplayed() {

        // Wait until results appear (IMPORTANT)
        page.waitForSelector(searchResults,
                new Page.WaitForSelectorOptions().setTimeout(15000));

        Locator results = page.locator(searchResults);

        return results.count() > 0;
    }

    public void selectProduct(String productName) {

        page.locator("h2 span")
                .filter(new Locator.FilterOptions()
                        .setHasText(productName))
                .first()
                .click();
    }
}