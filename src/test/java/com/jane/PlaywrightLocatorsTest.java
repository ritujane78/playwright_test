package com.jane;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;


@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightLocatorsTest {

    @DisplayName("Locating Elements by CSS")
    @Nested
    class LocatingElementsUsingCSS{

        @BeforeEach
        void navigateTo(Page page){
            page.navigate("https://practicesoftwaretesting.com/contact");

        }
        @DisplayName("By id")
        @Test
        void byId(Page page){
            page.locator("#first_name").fill("Ritu Bafna");

            PlaywrightAssertions.assertThat(page.locator("#first_name")).hasValue("Ritu Bafna");
        }

        @DisplayName("by CSS class")
        @Test
        void byClass(Page page){
            page.locator("#first_name").fill("Ritu Bafna");
            page.locator(".btnSubmit").click();
            List<String> alertMessages = page.locator(".alert").allTextContents();

            Assertions.assertTrue(!alertMessages.isEmpty());

        }
        @DisplayName("By Attribute")
        @Test
        void locateSendButtonByAttribute(Page page){
            page.locator("input[placeholder='Your last name *']").fill("Bafna");

            PlaywrightAssertions.assertThat(page.locator("#last_name")).hasValue("Bafna");
        }


    }

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
