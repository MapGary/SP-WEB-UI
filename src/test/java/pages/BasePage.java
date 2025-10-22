package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

import static java.sql.DriverManager.getDriver;

public class BasePage {

    WebDriver driver;
    String refreshToken = null;
    boolean httpOnlyRefreshToken = false;
    String jwt_asu = null;
    String user = null;
    String settings = null;
    private WebDriverWait wait10;

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

        try {
            Set<Cookie> cookies = driver.manage().getCookies();

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    refreshToken = cookie.getValue();
                    Allure.addAttachment("В Cookies сохранился refresh_token", refreshToken);
                }
            }
        } catch (NullPointerException e) {
            Allure.addAttachment("RefreshToken", "text/plain", "Поле refresh token не найдено: " + e.getMessage());
        }
        return refreshToken;
    }

    @Step("Получаю занчение HttpOnly у refresh_token")
    public boolean isHttpOnlyRefreshToken() {

        try {
            Set<Cookie> cookies = driver.manage().getCookies();

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    httpOnlyRefreshToken = cookie.isHttpOnly();
                    Allure.addAttachment("В Cookies сохранился refresh_token со значением в HttpOnly", String.valueOf(httpOnlyRefreshToken));
                }
            }
        } catch (NullPointerException e) {
            Allure.addAttachment("RefreshToken", "text/plain", "Поле refresh token не найдено: " + e.getMessage());
        }
        return httpOnlyRefreshToken;
    }

    @Step("Получаю jwt_asu")
    public String getJwtAsu() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        try {
            jwt_asu = (String) jsExecutor.executeScript("return localStorage.getItem('jwt_asu');");
            Allure.addAttachment("В Local storage сохранился jwt_asu", jwt_asu);
        } catch (NullPointerException e) {
            Allure.addAttachment("JwtAsu", "text/plain", "Поле jwt asu не найдено: " + e.getMessage());
        }
        return jwt_asu;
    }

    @Step("Получаю user")
    public String getUser() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        try {
            user = (String) jsExecutor.executeScript("return localStorage.getItem('user');");
            Allure.addAttachment("В Local storage сохранился user", user);
        } catch (NullPointerException e) {
            Allure.addAttachment("User", "text/plain", "Поле user не найдено: " + e.getMessage());
        }
        return user;
    }

    @Step("Получаю settings")
    public String getSettings() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        try {
            settings = (String) jsExecutor.executeScript("return localStorage.getItem('settings');");
            Allure.addAttachment("В Local storage сохранился settings", settings);
        } catch (NullPointerException e) {
            Allure.addAttachment("Settings", "text/plain", "Поле settings не найдено: " + e.getMessage());
        }
        return settings;
    }

    public WebDriverWait getWait10() {
        if (wait10 == null) {
            wait10 = new WebDriverWait(driver, Duration.ofSeconds(10));
        }

        return wait10;
    }

}
