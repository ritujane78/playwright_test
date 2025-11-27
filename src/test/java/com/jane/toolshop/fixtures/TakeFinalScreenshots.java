package com.jane.toolshop.fixtures;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;

public interface TakeFinalScreenshots {
    @AfterEach
    default void takeScreenshot(Page page){
        System.out.println("Taking Screenshot");
        ScreenshotsManager.takeScreenshot(page, "Final screenshot");
    }
}
