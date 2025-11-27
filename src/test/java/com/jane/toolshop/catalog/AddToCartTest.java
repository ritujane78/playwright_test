package com.jane.toolshop.catalog;

import com.jane.HeadlessChromeOptions;
import com.jane.toolshop.fixtures.PlaywrightTestCase;
import com.jane.toolshop.fixtures.TakeFinalScreenshots;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.jane.toolshop.catalog.pageobjects.*;
import com.microsoft.playwright.junit.UsePlaywright;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.List;


@DisplayName("Shopping Cart")
@Feature("Shopping Cart")
@UsePlaywright(HeadlessChromeOptions.class)
public class AddToCartTest implements TakeFinalScreenshots {

    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckoutCart checkoutCart;

    @BeforeEach
    void openHomePage() {
        navBar.openHomePage();
    }

    @BeforeEach
    void setUp(Page page) {
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);
    }

    @BeforeEach
    void setupTrace(BrowserContext browserContext){
        browserContext.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @AfterEach
    void recordTrace(TestInfo testInfo, BrowserContext browserContext){
        String traceName = testInfo.getDisplayName().replace(" ", "-").toLowerCase();
        browserContext.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/traces/trace-" + traceName + ".zip"))
        );
    }
    @Test
    @Story("Checking out a product")
    void whenCheckingOutASingleItem() {
        searchComponent.searchBy("pliers");
        productList.viewProductDetails("Combination Pliers");

        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    Assertions.assertThat(item.title()).contains("Combination Pliers");
                    Assertions.assertThat(item.quantity()).isEqualTo(3);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });
    }

    @Test
    @Story("Checking out multiple products")
    void whenCheckingOutMultipleItems() {
        navBar.openHomePage();
        productList.viewProductDetails("Bolt Cutters");
        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();

        navBar.openHomePage();
        productList.viewProductDetails("Slip Joint Pliers");
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems).hasSize(2);
        List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
        Assertions.assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");

        Assertions.assertThat(lineItems)
                .allSatisfy(item -> {
                    Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                    Assertions.assertThat(item.price()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });

    }
}