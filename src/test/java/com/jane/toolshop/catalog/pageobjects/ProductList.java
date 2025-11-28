package com.jane.toolshop.catalog.pageobjects;

import com.jane.domain.ProductSummary;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.List;

public class ProductList {
    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }


    public List<String> getProductNames() {
        return page.getByTestId("product-name").allInnerTexts();
    }

    @Step("View product details")
    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
    }

    public String getSearchCompletedMessage() {
        return page.getByTestId("search_completed").textContent();
    }

    public List<ProductSummary> getProductSummaries(){
          return page.locator(".card").all()
                .stream()
                .map(
                        product -> {
                            String productName = product.getByTestId("product-name").textContent().strip();
                            String productPrice = product.getByTestId("product-price").textContent();

                            return new ProductSummary(productName,productPrice);
                        }
                ).toList();
    }
}
