package com.amazon.pages;

import com.amazon.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * SERP — opens PDP and returns that tab. PDP waits are in {@link ProductPage}.
 */
public class SearchResultsPage extends BasePage {

    private static final String MAIN_SLOT = "div.s-main-slot";
    private static final String RESULT_ROW =
            "div[data-component-type='s-search-result'], div.s-result-item";

    public SearchResultsPage(Page page) {
        super(page);
    }

    public boolean isResultsDisplayed() {
        Locator mainSlot = page.locator(MAIN_SLOT);
        mainSlot.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return mainSlot.isVisible();
    }

    public boolean hasSearchResults() {
        Locator items = page.locator(RESULT_ROW);
        items.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return items.count() > 0;
    }

    private static boolean looksLikeProductUrl(String url) {
        if (url == null) {
            return false;
        }
        String u = url.toLowerCase();
        return u.contains("/dp/")
                || u.contains("/gp/product/")
                || u.contains("/gp/aw/")
                || u.contains("sspa");
    }

    /**
     * First result card whose subtree matches {@code hasText(productName)}, then click a product link inside it.
     */
    public Page selectProduct(String productName) {
        page.locator(MAIN_SLOT).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        Locator card = page.locator(RESULT_ROW)
                .filter(new Locator.FilterOptions().setHasText(productName))
                .first();
        card.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        clickFirstProductLinkInCard(card, productName);
        return resolveProductPageAfterClick(page, 30_000);
    }

    private void clickFirstProductLinkInCard(Locator card, String productName) {
        String primary = "a[href*='/dp/'], a[href*='sspa'], a[href*='slredirect'], a[href*='/gp/product/'], a[href*='/gp/aw/']";
        Locator primaryLinks = card.locator(primary);
        int n = primaryLinks.count();
        for (int j = 0; j < n; j++) {
            Locator a = primaryLinks.nth(j);
            String t = a.innerText();
            if (t != null && t.contains(productName)) {
                clickWithForceFallback(a);
                return;
            }
        }
        if (n >= 2) {
            clickWithForceFallback(primaryLinks.nth(1));
            return;
        }
        if (n == 1) {
            clickWithForceFallback(primaryLinks.first());
            return;
        }

        Locator h2a = card.locator("h2 a[href], h2 a").first();
        if (h2a.count() > 0) {
            clickWithForceFallback(h2a);
            return;
        }

        Locator any = card.locator("a[href]");
        int m = any.count();
        for (int j = 0; j < m; j++) {
            Locator a = any.nth(j);
            String href = a.getAttribute("href");
            if (href != null && href.length() > 1 && !href.startsWith("#")) {
                clickWithForceFallback(a);
                return;
            }
        }

        throw new RuntimeException("No clickable product link in result card for: " + productName);
    }

    private static void clickWithForceFallback(Locator loc) {
        try {
            loc.click();
        } catch (com.microsoft.playwright.PlaywrightException e) {
            loc.click(new Locator.ClickOptions().setForce(true));
        }
    }

    private static Page resolveProductPageAfterClick(Page searchPage, long maxWaitMs) {
        long deadline = System.currentTimeMillis() + maxWaitMs;
        while (System.currentTimeMillis() < deadline) {
            for (Page p : searchPage.context().pages()) {
                if (looksLikeProductUrl(p.url())) {
                    return p;
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        if (looksLikeProductUrl(searchPage.url())) {
            return searchPage;
        }
        java.util.List<Page> pages = searchPage.context().pages();
        for (int i = pages.size() - 1; i >= 0; i--) {
            Page p = pages.get(i);
            String u = p.url();
            if (u != null && !u.isEmpty() && !"about:blank".equals(u)) {
                return p;
            }
        }
        return searchPage;
    }
}