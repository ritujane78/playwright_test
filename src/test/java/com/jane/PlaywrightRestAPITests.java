package com.jane;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightRestAPITests {
    @DisplayName("Playwright allows us to mock out API responses")
    @Nested
    class MockingAPIResponses {
        @BeforeEach
        void openPage(Page page, Playwright playwright){
            page.navigate("https://practicesoftwaretesting.com");

            playwright.selectors().setTestIdAttribute("data-test");
        }

        @Test
        @DisplayName("When a search returns a single product")
        void whenASingleItemIsFound(Page page) {
            page.route("**/products/search?q=pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                            .setStatus(200))
            );

            var searchBox = page.getByPlaceholder("Search");
            searchBox.fill("pliers");
            searchBox.press("Enter");

            assertThat(page.getByTestId("product-name")).hasCount(1);
            assertThat(page.getByTestId("product-name")
                    .filter(new Locator.FilterOptions().setHasText("Super Pliers")))
                    .isVisible();
        }

        @Test
        @DisplayName("When a search returns no products")
        void whenNoItemsAreFound(Page page) {
            page.route("**/products/search?q=pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_NO_ENTRIES)
                            .setStatus(200))
            );
            var searchBox = page.getByPlaceholder("Search");
            searchBox.fill("pliers");
            searchBox.press("Enter");

            assertThat(page.getByTestId("product-name")).isHidden();
            assertThat(page.getByTestId("search_completed")).hasText("There are no products found.");
        }
    }
}
