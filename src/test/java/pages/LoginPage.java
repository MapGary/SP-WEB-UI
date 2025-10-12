package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.ByteArrayInputStream;
import java.io.File;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(name = "login")
    private WebElement fieldLogin;

    @FindBy(name = "password")
    private WebElement fieldPassword;

    @FindBy(xpath = "//input[@name='password']/..")
    private WebElement elementFieldPassword;

    @FindBy(xpath = "//button[@data-testid='LoginButton']")
    private WebElement buttonLogin;

    @FindBy(id = ":r1:-helper-text")
    private WebElement helperLogin;

    @FindBy(id = ":r2:-helper-text")
    private WebElement helperPassword;

    @FindBy(xpath = "//input[@name='password']/../div/a")
    private WebElement iconEye;

    @Step("Добавляю значение в поле Логин")
    public LoginPage addValueToFieldLogin(String login) {
        fieldLogin.sendKeys(login);

        return this;
    }

    @Step("Добавляю значение в поле Пароль")
    public LoginPage addValueToFieldPassword(String password) {
        fieldPassword.sendKeys(password);

        return this;
    }

    @Step("Взять значение поля Логин")
    public String getValueToFieldLogin() {

        return fieldLogin.getDomAttribute("value");
    }

    @Step("Взять значение поля  Пароль")
    public String getValueToFieldPassword() {

        return fieldPassword.getDomAttribute("value");
    }

    @Step("Кликаю кнопку Войти")
    public DashboardPage clickButtonLogin() {
        buttonLogin.click();

        return new DashboardPage(driver);
    }

    @Step("Кликнуть в поле Логин")
    public LoginPage clickToFieldLogin() {
        fieldLogin.click();

        return this;
    }

    @Step("Кликнуть в поле Пароль")
    public LoginPage clickToFieldPassword() {
        fieldPassword.click();

        return this;
    }

    @Step("Кликаю кнопку Войти с подсказкой")
    public LoginPage clickButtonLoginWithHelper() {
        buttonLogin.click();

        return this;
    }

    @Step("Проверить текст подсказки для поля Логин")
    public String getHelperTextLogin() {
        return helperLogin.getText();
    }

    @Step("Проверить текст подсказки для поля Пароль")
    public String getHelperTextPassword() {
        return helperPassword.getText();
    }

    @Step("Получаю значение атрибута 'type' для поля password")
    public String getAttributeFieldPassword() {

        return fieldPassword.getDomAttribute("type");
    }

    @Step("Кликаю иконку 'глаз'")
    public LoginPage clickEyeIcon() {
        iconEye.click();

        return this;
    }

    public File getScreenshotWebElement() {
        File screenshot = null;
        try {
            Thread.sleep(1000);
            byte[] screen = elementFieldPassword.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Скрин поля пароль", new ByteArrayInputStream(screen));
            screenshot = elementFieldPassword.getScreenshotAs(OutputType.FILE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenshot;
    }
}
