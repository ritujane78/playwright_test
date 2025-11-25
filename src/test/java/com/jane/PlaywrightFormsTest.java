package com.jane;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightFormsTest {
    private Page page;

    @BeforeEach
    void setup(Page page) {
        this.page = page;
    }


    @DisplayName("Interact with form")
    @Test
    void whenInteractingWithForm() throws URISyntaxException {
        page.navigate("https://practicesoftwaretesting.com/contact");

        var firstNameField = page.getByLabel("First Name");
        var lastNameField = page.getByLabel("Last Name");
        var emailField = page.getByLabel("Email");
        var messageField = page.getByLabel("Message");
        var subjectField = page.getByLabel("Subject");
        var uploadField = page.getByLabel("Attachment");

        firstNameField.fill("Ritu");
        lastNameField.fill("Bafna");
        emailField.fill("ritubafna@example.com");
        messageField.fill("Well, Hello there!");
        subjectField.selectOption("Warranty");

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        page.setInputFiles("#attachment", fileToUpload);

        assertThat(firstNameField).hasValue("Ritu");
        assertThat(lastNameField).hasValue("Bafna");
        assertThat(emailField).hasValue("ritubafna@example.com");
        assertThat(messageField).hasValue("Well, Hello there!");
        assertThat(subjectField).hasValue("warranty");

        String uploadedFile = uploadField.inputValue();
        org.assertj.core.api.Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");
    }

    @DisplayName("Mandatory fields")
    @ParameterizedTest
    @ValueSource(strings = {"First Name", "Last Name","Email", "Message"})
    void mandatoryFields(String fieldName){
        page.navigate("https://practicesoftwaretesting.com/contact");

        var firstNameField = page.getByLabel("First Name");
        var lastNameField = page.getByLabel("Last Name");
        var emailField = page.getByLabel("Email");
        var messageField = page.getByLabel("Message");

//        Fill in the field values
        firstNameField.fill("Ritu");
        lastNameField.fill("Bafna");
        emailField.fill("ritubafna@example.com");
        messageField.fill("Well, Hello there!");

//         Clear one of the field values
        page.getByLabel(fieldName).fill("");

        page.locator(".btnSubmit").click();

//        Check the error message of that field
        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

        assertThat(errorMessage).isVisible();



    }
}
