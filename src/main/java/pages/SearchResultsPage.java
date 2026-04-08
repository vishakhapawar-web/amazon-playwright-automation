// SearchResultsPage.java
package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class SearchResultsPage {
    private Page page;
    private Locator searchResults;

    public SearchResultsPage(Page page) {
        this.page = page;
        // All search result items on Amazon
        searchResults = page.locator("div.s-result-item");
    }

    /**
     * Selects the product by name. Uses contains/regex matching to handle variations.
     */
    public void selectProductByName(String productName) {
        // Wait for results to load
        searchResults.first().waitFor();

        boolean found = false;
        int count = searchResults.count();

        for (int i = 0; i < count; i++) {
            String itemText = searchResults.nth(i).innerText();
            if (itemText.contains(productName)) {
                searchResults.nth(i).locator("text=" + productName).click();
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Not enough products found! Could not locate: " + productName);
        }
    }

    /**
     * Optional helper: verify search results exist
     */
    public boolean verifyResultsExist() {
        searchResults.first().waitFor();
        return searchResults.count() > 0;
    }
}