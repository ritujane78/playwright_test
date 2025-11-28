package com.jane.cucumber.stepDefinitions;

import com.jane.domain.ProductSummary;
import com.jane.toolshop.catalog.pageobjects.NavBar;
import com.jane.toolshop.catalog.pageobjects.ProductDetails;
import com.jane.toolshop.catalog.pageobjects.ProductList;
import com.jane.toolshop.catalog.pageobjects.SearchComponent;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

public class ProductCatalogStepDefinitions {

    NavBar navBar;
    ProductList productList;
    SearchComponent searchComponent;

    @Before
    public void setup(){
        navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
        searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
    }

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {
        System.out.println("Opening home page...");
        navBar.openHomePage();
    }

    @When("she searches for {string}")
    public void she_searches_for(String productName) {
        System.out.println("Searching for: " + productName);
        searchComponent.searchBy(productName);
    }

    @Then("the {string} product should be displayed")
    public void product_should_be_displayed(String productName) {
        System.out.println("Verifying product: " + productName);
        var matchingProducts = productList.getProductNames();

        Assertions.assertThat(matchingProducts).contains(productName);

    }

    @DataTableType
    public ProductSummary productSummaryRow(Map<String, String> productData){
        return new ProductSummary(productData.get("Product"), productData.get("Price"));
    }
    @Then("the following products should be displayed:")
    public void the_following_products_should_be_displayed(List<ProductSummary> expectedProductSummaries) {

        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductSummaries);

    }
    @And("the message {string} should be displayed")
    public void theMessageShouldBeDisplayed(String messageText) {
        String completionMessage = productList.getSearchCompletedMessage();
        Assertions.assertThat(completionMessage).isEqualTo(messageText);
    }

    @And("she filters by {string}")
    public void sheFiltersBy(String filterName) {
        searchComponent.filterBy(filterName);
    }

    @When("she sorts by {string}")
    public void sheSortsBy(String sortFilter) {
        searchComponent.sortBy(sortFilter);
    }

    @Then("the first product displayed should be {string}")
    public void theFirstProductDisplayedShouldBe(String firstProductName) {
        List<String> productNames = productList.getProductNames();
        Assertions.assertThat(productNames).startsWith(firstProductName);
    }
}
