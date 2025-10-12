package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Set;

public class BasePage {

    WebDriver driver;
    String refreshToken = null;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Получаю url страницы")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Получаю refresh_token")
    public String getRefreshToken() {

        Set<Cookie> cookies = driver.manage().getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
                Allure.addAttachment("В Cookies сохранился refresh_token", refreshToken);
            }
        }
        return refreshToken;
    }
}
