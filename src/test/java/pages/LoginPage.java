package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(tagName = "h2")
    private WebElement nameForm;

    @FindBy(name = "login")
    private WebElement fieldLogin;

    @FindBy(name = "password")
    private WebElement fieldPassword;

    @FindBy(xpath = "//input[@name='login']/../../label")
    private WebElement labelFieldLogin;

    @FindBy(xpath = "//input[@name='password']/../../label")
    private WebElement labelFieldPassword;

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

    @FindBy(xpath = "//a[@data-testid='SetNewPasswordButton']")
    private WebElement buttonNewPassword;

    @FindBy(xpath = "//button[@data-testid='switchLanguage']")
    private WebElement buttonLanguage;

    @FindBy(xpath = "//div[@id=':r0:']")
    private WebElement helperLanguage;

    @FindBy(xpath = "//div[@id='language-menu']//ul/li[@tabindex='0']")
    private WebElement activeLanguage;

    @FindBy(xpath = "//div[@id='language-menu']//ul/li[@tabindex='-1']")
    private WebElement inactiveLanguage;

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

    @Step("Кликаю кнопку 'Сменить пароль'")
    public SetNewPasswordPage clickButtonNewPassword() {
        buttonNewPassword.click();
        return new SetNewPasswordPage(driver);
    }

    @Step("Навожу мышку на иконку 'Сменить язык'")
    public LoginPage getHelperSwitchLanguage() {
        Actions actions = new Actions(driver);

        actions.moveToElement(buttonLanguage).perform();
        Allure.addAttachment("Всплывающая подсказка ", new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOf(helperLanguage)).getText());

        return this;
    }

    @Step("Кликаю иконку 'Сменить язык'")
    public LoginPage clickSwitchLanguage() {
        Actions actions = new Actions(driver);

        actions.moveToElement(buttonLanguage).click().perform();
        Allure.addAttachment("Появилось всплавающее меню с активным полем", activeLanguage.getText());

        return this;
    }

    @Step("Кликаю по неактивному языку")
    public LoginPage clickInactiveLanguage() {
        Actions actions = new Actions(driver);

        actions.moveToElement(inactiveLanguage).click().perform();
        Allure.addAttachment("Кликнул по неактивному языку", inactiveLanguage.getText());

        return this;
    }

    @Step("Получаю переведенные данные со страницы")
    public Map<String, String> getTranslatedData() {
        Map<String, String> data = new HashMap<>();

        Actions actions = new Actions(driver);
        actions.moveToElement(buttonLanguage).perform();

        data.put("nameForm", nameForm.getText());
        data.put("login", labelFieldLogin.getText());
        data.put("password", labelFieldPassword.getText());
        data.put("buttonNewPassword", buttonNewPassword.getText());
        data.put("buttonLogin", buttonLogin.getText());
        data.put("helperLanguage", new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOf(helperLanguage)).getText());

        return data;
    }
}
