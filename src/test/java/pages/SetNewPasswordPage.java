package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SetNewPasswordPage extends BasePage {

    public SetNewPasswordPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//button[@data-testid='switchLanguage']")
    private WebElement buttonLanguage;

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
}
