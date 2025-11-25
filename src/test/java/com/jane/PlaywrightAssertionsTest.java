package com.jane;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.LoadState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightAssertionsTest {

    @BeforeEach
    public  void fixTestId(Page page, Playwright playwright){
        playwright.selectors().setTestIdAttribute("data-test");
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);
    }

    @DisplayName("Check price of all the products")
    @Test
    public void allProductPricesShouldBeCorrectValues(Page page){
        List<Double> productPrices = page.getByTestId("product-price")
                .allInnerTexts()
                .stream()
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .toList();

        Assertions.assertThat(productPrices)
                .isNotEmpty()
                .allMatch(price -> price > 0)
                .allSatisfy(price ->
                        Assertions.assertThat(price)
                                .isGreaterThan(0.0)
                                .isLessThan(1000.0));
    }
    @Test
    void shouldSortInAlphabeticalOrder(Page page){
        page.getByLabel("Sort").selectOption("Name (A - Z)");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        List<String> productNames = page.getByTestId("product-name").allTextContents();

        Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.naturalOrder());
//        Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);

    }
}
