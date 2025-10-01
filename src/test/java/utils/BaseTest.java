package utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.Objects;

public abstract class BaseTest {

    private WebDriver driver;
    private TestConfig config = new TestConfig();

    protected WebDriver getDriver() {
        return driver;
    }

    protected TestConfig getConfig() {
        return config;
    }

    private void startDriver() {

    }

    private void closeDriver() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Parameters("browser")
    @BeforeMethod
    protected void beforeMethod(Method method, String browser) {

        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().setSize(new Dimension(1440, 1080));
        System.out.println(String.format("Открылса браузер: %s", browser));

        driver.get(config.getBaseUrl());
    }

    @AfterMethod
    protected void afterMethod(Method method, ITestResult testResult) {
        if (!testResult.isSuccess()) {
            Allure.addAttachment(
                    "screenshot.png",
                    "image/png",
                    new ByteArrayInputStream(((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES)),
                    "png");

            Allure.addAttachment(
                    "Page HTML",
                    "text/html",
                    Objects.requireNonNull(getDriver().getPageSource()),
                    ".html");
        }

        closeDriver();
    }
}
