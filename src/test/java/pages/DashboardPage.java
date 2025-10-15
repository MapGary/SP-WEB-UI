package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    String jwt_asu = null;
    String user = null;
    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получаю jwt_asu")
    public String getJwtAsu() {
        jwt_asu = (String) jsExecutor.executeScript("return localStorage.getItem('jwt_asu.accessToken');");
//        jwt_asu = (String) jsExecutor.executeScript("return localStorage.getItem('jwt_asu');");
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
