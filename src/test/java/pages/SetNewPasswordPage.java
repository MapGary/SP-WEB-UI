package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SetNewPasswordPage extends BasePage {

    public SetNewPasswordPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//button[@data-testid='switchLanguage']")
    private WebElement buttonLanguage;

    // поле логин
    @FindBy(name = "login")
    private WebElement fieldLogin;

    @FindBy(xpath = "//div[@id='language-menu']//ul/li[@tabindex='0']")
    private WebElement activeLanguage;

    @FindBy(xpath = "//div[@id='language-menu']//ul/li[@tabindex='-1']")
    private WebElement inactiveLanguage;

    @FindBy(tagName = "h2")
    private WebElement nameForm;

    @FindBy(xpath = "//input[@name='login']/../../label")
    private WebElement labelFieldLogin;

    @FindBy(xpath = "//input[@name='currentPassword']/../../label")
    private WebElement labelFieldCurrentPassword;

    @FindBy(xpath = "//input[@name='newPassword']/../../label")
    private WebElement labelFieldNewPassword;

    @FindBy(xpath = "//button[@data-testid='SubmitSetNewPasswordButton']")
    private WebElement buttonSubmit;

    @FindBy(xpath = "//div[@id=':r0:']")
    private WebElement helperLanguage;

    @FindBy(xpath = "//p[@id=':r3:-helper-text']")
    private WebElement helperLogin;

    @FindBy(xpath = "//p[@id=':r4:-helper-text']")
    private WebElement helperCurrentPassword;

    @FindBy(xpath = "//form[@data-testid='SetNewPassword-form']/span")
    private WebElement helperCurrentPasswordDefault;

    @FindBy(xpath = "//p[@id=':r5:-helper-text']")
    private WebElement helperNewPassword;

    @FindBy(name = "currentPassword")
    private WebElement fieldCurrentPassword;

    @FindBy(name = "newPassword")
    private WebElement fieldNewPassword;

    @FindBy(xpath = "//input[@name='currentPassword']/..")
    private WebElement elementFieldCurrentPassword;

    @FindBy(xpath = "//input[@name='newPassword']/..")
    private WebElement elementFieldNewPassword;

    @FindBy(xpath = "//input[@name='currentPassword']/../div/button")
    private WebElement iconEyeCurrentPassword;

    @FindBy(xpath = "//input[@name='newPassword']/../div/button")
    private WebElement iconEyeNewPassword;

    @Step("Навожу мышку на иконку 'Сменить язык'")
    public SetNewPasswordPage getHelperSwitchLanguage() {
        Actions actions = new Actions(driver);

        actions.moveToElement(buttonLanguage).perform();
        Allure.addAttachment("Всплывающая подсказка ", new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOf(helperLanguage)).getText());

        return this;
    }

    @Step("Кликаю иконку 'Сменить язык'")
    public SetNewPasswordPage clickSwitchLanguage() {
        buttonLanguage.click();
        Allure.addAttachment("Появилось всплывающее меню с активным полем", activeLanguage.getText());

        return this;
    }

    @Step("Кликаю по неактивному языку")
    public SetNewPasswordPage clickInactiveLanguage() {
        Allure.addAttachment("Кликнул по неактивному языку", inactiveLanguage.getText());
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(inactiveLanguage)).click();

        return this;
    }

    @Step("Получаю переведенные данные со страницы")
    public Map<String, String> getTranslatedData() {
        Map<String, String> data = new HashMap<>();

        Actions actions = new Actions(driver);
        actions.moveToElement(buttonLanguage).perform();

        data.put("nameForm", nameForm.getText());
        data.put("login", labelFieldLogin.getText());
        data.put("currentPassword", labelFieldCurrentPassword.getText());
        data.put("newPassword", labelFieldNewPassword.getText());
        data.put("buttonSubmit", buttonSubmit.getText());
        data.put("helperCurrentPassword", helperCurrentPasswordDefault.getText());
        data.put("helperLanguage", new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOf(helperLanguage)).getText());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(data);
            Allure.addAttachment("Данные перевода", "application/json", json);
        } catch (Exception e) {
            Allure.addAttachment("Данные перевода", "text/plain", "Error converting map to JSON: " + e.getMessage());
        }

        return data;
    }

    @Step("Получаю подсказку для поля Логин")
    public String getHelperLogin() {
        String helper = helperLogin.getText();
        Allure.addAttachment("helper Login: ", helper);

        return helper;
    }

    @Step("Получаю подсказку для поля Текущий пароль")
    public String getHelperCurrentPasswordDefault() {
        String helper = helperCurrentPasswordDefault.getText();
        Allure.addAttachment("helper Current Password: ", helper);

        return helper;
    }

    @Step("Получаю подсказку для поля Текущий пароль")
    public String getHelperCurrentPassword() {
        String helper = helperCurrentPassword.getText();
        Allure.addAttachment("helper Current Password: ", helper);

        return helper;
    }

    @Step("Получаю подсказку для поля Новый пароль")
    public String getHelperNewPassword() {
        String helper = helperNewPassword.getText();
        Allure.addAttachment("helper New Password: ", helper);

        return helper;
    }

    @Step("Добавляю значение в поле Текущий пароль")
    public SetNewPasswordPage addValueToFieldCurrentPassword(String currentPassword) {
        fieldCurrentPassword.sendKeys(currentPassword);

        return this;
    }

    @Step("Добавляю значение в поле Новый пароль")
    public SetNewPasswordPage addValueToFieldNewPassword(String newPassword) {
        fieldNewPassword.sendKeys(newPassword);

        return this;
    }

    @Step("Получаю скриншот поля Текущий пароль")
    public File getScreenshotFieldCurrentPassword() {

        return getScreenshotWebElement(elementFieldCurrentPassword);
    }

    @Step("Получаю скриншот поля Новый пароль")
    public File getScreenshotFieldNewPassword() {

        return getScreenshotWebElement(elementFieldNewPassword);
    }

    @Step("Кликаю иконку 'глаз' для поля Текущий пароль")
    public SetNewPasswordPage clickEyeIconCurrentPassword() {
        iconEyeCurrentPassword.click();

        return this;
    }

    @Step("Кликаю иконку 'глаз' для поля Новый пароль")
    public SetNewPasswordPage clickEyeIconNewPassword() {
        iconEyeNewPassword.click();

        return this;
    }

    @Step("Получаю значение атрибута 'type' для поля Текущий пароль")
    public String getAttributeFieldCurrentPassword() {

        return fieldCurrentPassword.getDomAttribute("type");
    }

    @Step("Получаю значение атрибута 'type' для поля Новый пароль")
    public String getAttributeFieldNewPassword() {

        return fieldNewPassword.getDomAttribute("type");
    }

    @Step("Кликаю в поле Логин")
    public SetNewPasswordPage clickToFieldLogin() {
        fieldLogin.click();

        return this;
    }

    @Step("Кликаю в поле Текущий пароль")
    public SetNewPasswordPage clickToFieldCurrentPassword() {
        elementFieldCurrentPassword.click();

        return this;
    }

    @Step("Кликаю в поле Новый пароль")
    public SetNewPasswordPage clickToFieldNewPassword() {
        elementFieldNewPassword.click();

        return this;
    }

    // кликаю отправить без перехода
    @Step("Кликаю кнопку Отправить")
    public SetNewPasswordPage clickButtonSubmitWithHelper() {
        buttonSubmit.click();

        return this;
    }

    @Step("Добавляю значение в поле Логин")
    public SetNewPasswordPage addValueToFieldLogin(String login) {
        fieldLogin.sendKeys(login);

        return this;
    }

    @Step("Очистить поле Новый Пароль")
    public SetNewPasswordPage clearFieldNewPassword() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = ''", fieldNewPassword);

        return this;
    }
}
