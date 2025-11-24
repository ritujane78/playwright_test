package com.jane;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.util.Arrays;


@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightLocatorsTest {

    @DisplayName("Locating Elements by Texts")
    @Nested
    class LocatingElementsByText{
        @BeforeEach
        public void openTheCatalogPage(Page page){
            openPage(page);
        }

        @DisplayName("using alt text")
        @Test
        void byAltText(Page page){
            page.getByAltText("Combination Pliers").click();

            PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools")).isVisible();
        }

        @DisplayName("Locating an element by text contents")
        @Test
        void byText(Page page){
            page.getByText("Bolt Cutters").click();

            PlaywrightAssertions.assertThat(page.getByText("MightyCraft Hardware")).isVisible();
        }

        @DisplayName("Using title")
        @Test
        void byTitle(Page page) {
            page.getByAltText("Combination Pliers").click();

            page.getByTitle("Practice Software Testing - Toolshop").click();
        }
    }
    private void openPage(Page page) {
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
}
