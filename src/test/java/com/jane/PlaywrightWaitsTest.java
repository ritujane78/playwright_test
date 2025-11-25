package com.jane;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;


import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightWaitsTest {

    @Nested
    class WaitingForState{

        @BeforeEach
        void openPage(Page page, Playwright playwright){
            page.navigate("https://practicesoftwaretesting.com");
            page.waitForSelector(".card-img-top");

            playwright.selectors().setTestIdAttribute("data-test");
        }

        @Test
        void shouldWaitForAllProductNames(Page page){
            List<String> productNames = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
        }

        @Test
        void shouldWaitForAllImages(Page page){
            List<String> productImageTitles = page.locator(".card-img-top").all()
                            .stream()
                                    .map(img -> img.getAttribute("alt"))
                                            .toList();

            Assertions.assertThat(productImageTitles).contains("Pliers", "Bolt Cutters", "Hammer");
        }
    }

    @Nested
    class AutomateTests{

        @BeforeEach
        void openPage(Page page, Playwright playwright){
            page.navigate("https://practicesoftwaretesting.com");

            playwright.selectors().setTestIdAttribute("data-test");
        }

        @Test
        void shouldWaitForTheFileCheckboxes(Page page){
            var screwDriverCheckBox = page.getByLabel("ScrewDriver");

            screwDriverCheckBox.click();

            assertThat(screwDriverCheckBox).isChecked();


        }
        @Test
        void shouldFilterProductsByCategory(Page page, Playwright playwright){
            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();
            page.waitForSelector(".card");

            List<String> filteredProducts = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(filteredProducts).contains("Sheet Sander");
        }

    }
    @Nested
    class WaitingForElementsToAppearAndDisappear{
        @BeforeEach
        void openPage(Page page, Playwright playwright){
            page.navigate("https://practicesoftwaretesting.com");
        }

        @Test
        void shouldAppearAndDisappear(Page page){
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");
            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

        @Test
        void shouldUpdateCartCount(Page page){
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));
        }
    }

//    @Nested
//    class WaitingForAPICalls{
//        @BeforeEach
//        void openPage(Page page, Playwright playwright){
//            page.navigate("https://practicesoftwaretesting.com");
//        }
//
//        @Test
//        void shouldCheckForSorting(Page page){

//            page.waitForResponse("**/products?sort**",
//                    () -> {
//                            page.getByLabel("Sort").selectOption("Price (High - Low)");
//                    }
//            );


//            page.getByLabel("Sort").selectOption("Price (High - Low)");
//
//            page.waitForSelector("[data-test='product-price']");
//
//
//            List<Double> productPrices = page.getByTestId("product-price")
//                    .allInnerTexts()
//                    .stream()
//                    .map(WaitingForAPICalls::extractPrice)
//                    .toList();
//
//            System.out.println("Product Prices = " + productPrices);
//
//            Assertions.assertThat(productPrices).isSortedAccordingTo(Comparator.reverseOrder());
//        }
//
//        private static double extractPrice(String price){
//            return Double.parseDouble(price.replace("$",  ""));
//        }
//    }
}
