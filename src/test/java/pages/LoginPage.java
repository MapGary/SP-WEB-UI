package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(name = "login")
    private WebElement fieldLogin;

    @FindBy(name = "password")
    private WebElement fieldPassword;

    @FindBy(xpath = "//button[@data-testid='LoginButton']")
    private WebElement buttonLogin;

    @Step("Добавляю значение в поле Логин")
    public LoginPage addValueToFieldLogin(String value) {
        fieldLogin.sendKeys(value);

        return this;
    }

    @Step("Добавляю значение в поле Пароль")
    public LoginPage addValueToFieldPassword(String value) {
        fieldPassword.sendKeys(value);

        return this;
    }

    @Step("Кликаю кнопку Войти")
    public DashboardPage clickButtonLogin() {
        buttonLogin.click();

        return new DashboardPage(driver);
    }
}
