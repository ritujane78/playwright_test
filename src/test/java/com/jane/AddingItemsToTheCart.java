package com.jane;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;


import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Test")
@UsePlaywright(HeadlessChromeOptions.class)
public class AddingItemsToTheCart {

    @DisplayName("Check if searching")
    @Test
    void shouldSearch(Page page, Playwright playwright){
        page.navigate("https://practicesoftwaretesting.com");
        playwright.selectors().setTestIdAttribute("data-test");

        page.getByPlaceholder("Search").fill("pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        assertThat(page.locator(".card")).hasCount(4);

        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

        Locator outOfStockItems = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");

        assertThat(outOfStockItems).hasCount(1);
        assertThat(outOfStockItems).hasText("Long Nose Pliers");
    }

}
