package com.jane.toolshop.fixtures;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class ScreenshotsManager {
    public static void takeScreenshot(Page page, String name){
        var screenshot = page.screenshot(
                new Page.ScreenshotOptions()
                        .setFullPage(true)
        );

        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
    }
}
