package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class DashboardPage extends BasePage {

    String refreshToken = null;
    String jwt_asu = null;
    String user = null;
    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

    public DashboardPage(WebDriver driver) {
        super(driver);
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

    @Step("Получаю jwt_asu")
    public String getJwtAsu() {
        jwt_asu = (String) jsExecutor.executeScript("return localStorage.getItem('jwt_asu');");
        Allure.addAttachment("В Local storage сохранился jwt_asu", jwt_asu);
        return jwt_asu;
    }

    @Step("Получаю user")
    public String getUser() {
        user = (String) jsExecutor.executeScript("return localStorage.getItem('user');");
        Allure.addAttachment("В Local storage сохранился user", user);
        return user;
    }
}
