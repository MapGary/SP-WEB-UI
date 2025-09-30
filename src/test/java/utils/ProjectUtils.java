package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ProjectUtils {

    public static WebDriver createDriver() {

        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriver driver;

        chromeOptions.addArguments("--window-size=1440, 1080");

        driver = new ChromeDriver(chromeOptions);

        return driver;
    }
}
