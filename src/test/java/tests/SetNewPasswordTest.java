package tests;

import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SetNewPasswordPage;
import utils.BaseTest;
import utils.Language;

import java.io.File;
import java.util.Map;

import static utils.Assert.compareExpectedLanguageSetNewPasswordPage;
import static utils.Assert.compareScreenshotsWithTolerance;

@Epic("Авторизация и аутентификация")
@Feature("Страница Сменить пароль")
public class SetNewPasswordTest extends BaseTest {

    @Test
    @Description("Переключение языка на форме смены пароля")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/18")
    public void testSwitchLanguage() {

        Map<String, String> dataLanguageUS = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .getHelperSwitchLanguage()
                .clickSwitchLanguage()
                .clickInactiveLanguage()
                .getTranslatedData();

        Map<String, String> dataLanguageRU = new SetNewPasswordPage(getDriver())
                .getHelperSwitchLanguage()
                .clickSwitchLanguage()
                .clickInactiveLanguage()
                .getTranslatedData();

        Allure.step("Проверяю, что язык соответствует английскому");
        Assert.assertTrue(compareExpectedLanguageSetNewPasswordPage(Language.US, dataLanguageUS));

        Allure.step("Проверяю, что язык соответствует русскому");
        Assert.assertTrue(compareExpectedLanguageSetNewPasswordPage(Language.RU, dataLanguageRU));
    }

    @Test
    @Description("Проверка видимости пароля (иконка 'глаз') для поля Текущий пароль на странице Cменить пароль")
    @Severity(SeverityLevel.MINOR)
    @Link("https://team-b9fb.testit.software/projects/1/tests/20")
    public void testCheckingCurrentPasswordVisibility() {
        File screenshotCurrentPassword1 = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .addValueToFieldCurrentPassword("Test1234!")
                .getScreenshotFieldCurrentPassword();

        String valueCurrentPassword = new SetNewPasswordPage(getDriver())
                .clickEyeIconCurrentPassword()
                .getAttributeFieldCurrentPassword();

        String secretValueCurrentPassword = new SetNewPasswordPage(getDriver())
                .clickEyeIconCurrentPassword()
                .getAttributeFieldCurrentPassword();

        File screenshotCurrentPassword2 = new SetNewPasswordPage(getDriver())
                .clickToFieldCurrentPassword()
                .getScreenshotFieldCurrentPassword();

        Allure.step("Проверяю значение атрибута type для поля Текущий пароль, пароль видно");
        Assert.assertEquals(valueCurrentPassword, "text");
        Allure.step("Проверяю значение атрибута type для поля Текущий пароль, пароль скрыт");
        Assert.assertEquals(secretValueCurrentPassword, "password");
        Allure.step("Сравниваю скриншоты поля Текущий пароль");
        Assert.assertTrue(compareScreenshotsWithTolerance(screenshotCurrentPassword1, screenshotCurrentPassword2, 0.7));
    }

    @Test
    @Description("Проверка видимости пароля (иконка 'глаз') для поля Новый пароль на странице Cменить пароль")
    @Severity(SeverityLevel.MINOR)
    @Link("https://team-b9fb.testit.software/projects/1/tests/20")
    public void testCheckingNewPasswordVisibility() {

        File screenshotNewPassword1 = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .addValueToFieldNewPassword("Test4321!")
                .getScreenshotFieldNewPassword();

        String valueNewPassword = new SetNewPasswordPage(getDriver())
                .clickEyeIconNewPassword()
                .getAttributeFieldNewPassword();

        String secretValueNewPassword = new SetNewPasswordPage(getDriver())
                .clickEyeIconNewPassword()
                .getAttributeFieldNewPassword();

        File screenshotNewPassword2 = new SetNewPasswordPage(getDriver())
                .clickToFieldNewPassword()
                .getScreenshotFieldNewPassword();


        Allure.step("Проверяю значение атрибута type для поля Новый пароль, пароль видно");
        Assert.assertEquals(valueNewPassword, "text");
        Allure.step("Проверяю значение атрибута type для поля Новый пароль, пароль скрыт");
        Assert.assertEquals(secretValueNewPassword, "password");
        Allure.step("Сравниваю скриншоты поля Новый пароль");
        Assert.assertTrue(compareScreenshotsWithTolerance(screenshotNewPassword1, screenshotNewPassword2, 0.7));
    }

    @Test
    @Description("Отображение подсказки под полем 'Текущий пароль' в форме смены пароля")
    @Severity(SeverityLevel.TRIVIAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/21")
    public void testHelperCurrentPassword() {
        String helperCurrentPassword = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .getHelperCurrentPasswordDefault();

        Allure.step("Проверяю, что helper соответствует значению");
        Assert.assertEquals(helperCurrentPassword, Language.RU.getHelperCurrentPassword());
    }

    @Test
    @Description("Попытка отправки формы с пустыми полями при смене пароля")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/22")
    public void testSubmittingFormWithEmptyFields() {
        SetNewPasswordPage setNewPasswordPage = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .clickToFieldLogin()
                .clickToFieldCurrentPassword()
                .clickToFieldNewPassword()
                .clickButtonSubmitWithHelper();

        String helperLogin = setNewPasswordPage.getHelperLogin();
        String helperCurrentPassword = setNewPasswordPage.getHelperCurrentPassword();
        String helperNewPassword = setNewPasswordPage.getHelperNewPassword();

        Assert.assertEquals(helperLogin, "Поле Логин обязательно для заполнения");
        Assert.assertEquals(helperCurrentPassword, "Поле Текущий пароль обязательно для заполнения");
        Assert.assertEquals(helperNewPassword, "Пароль должен содержать только латинские буквы");
    }

    @Test
    @Description("Новый пароль не соответствует требованиям (например, без цифры или спецсимвола) при смене пароля")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/23")
    public void testNewPasswordNotMeetRequirements() {
        SetNewPasswordPage setNewPasswordPage = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .addValueToFieldLogin("Login")
                .addValueToFieldCurrentPassword("Password");

        String helper1 = setNewPasswordPage
                .addValueToFieldNewPassword("qazwsxe")
                .getHelperNewPassword();

        String helper2 = setNewPasswordPage
                .clearFieldNewPassword()
                .addValueToFieldNewPassword("qazwsxed")
                .getHelperNewPassword();

        String helper3 = setNewPasswordPage
                .clearFieldNewPassword()
                .addValueToFieldNewPassword("QAZWSXED")
                .getHelperNewPassword();

        String helper4 = setNewPasswordPage
                .clearFieldNewPassword()
                .addValueToFieldNewPassword("qazwsxeD")
                .getHelperNewPassword();

        String helper5 = setNewPasswordPage
                .clearFieldNewPassword()
                .addValueToFieldNewPassword("qazwsxE1")
                .getHelperNewPassword();

        String helper6 = setNewPasswordPage
                .clearFieldNewPassword()
                .addValueToFieldNewPassword("йфяцыУ1!")
                .getHelperNewPassword();

        Allure.step("Проверяю подсказки");
        Assert.assertEquals(helper1, "Пароль должен содержать не менее 8 символов");
        Assert.assertEquals(helper2, "Пароль должен содержать хотя бы одну заглавную букву");
        Assert.assertEquals(helper3, "Пароль должен содержать хотя бы одну строчную букву");
        Assert.assertEquals(helper4, "Пароль должен содержать хотя бы одну цифру");
        Assert.assertEquals(helper5, "Пароль должен содержать хотя бы один специальный символ");
        Assert.assertEquals(helper6, "Пароль должен содержать только латинские буквы");
    }

    @Test
    @Description("Новый пароль содержит запрещённое слово (например, Moscow)")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/24")
    @Ignore
    public void testNewPasswordNotContainForbiddenWord() {
        String helper = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .addValueToFieldLogin(getConfig().getUserName())
                .addValueToFieldCurrentPassword(getConfig().getPassword())
                .addValueToFieldNewPassword("Moscow123!")
                .clickButtonSubmitWithHelper()
                .getHelperNewPassword();

        Allure.step("Проверяю подсказку");
        Assert.assertEquals(helper, "Пароль не должен содержать словарные слова");
        Allure.step("Проверю, что остался на той же странице");
        Assert.assertEquals(getDriver().getCurrentUrl(), String.format("%s/set-new-password", getConfig().getBaseUrl()));
    }

    @Test
    @Description("Текущий пароль указан неверно при смене пароля")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/25")
    public void testCurrentPasswordIncorrect() {
        String helper = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .addValueToFieldLogin("Login")
                .addValueToFieldCurrentPassword("Rita")
                .addValueToFieldNewPassword("qazwsX1!")
                .clickButtonSubmitWithHelper()
                .getHelperCurrentPassword();

        Allure.step("Проверяю подсказку");
        Assert.assertEquals(helper, "Неверный пароль или логин пользователя");
    }

    @Test
    @Description("Логин указан неверно при смене пароля")
    @Severity(SeverityLevel.NORMAL)
    @Link("")
    public void testLoginIncorrect() {
        String helper = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .addValueToFieldLogin("Login")
                .addValueToFieldCurrentPassword(getConfig().getPassword())
                .addValueToFieldNewPassword("qazwsX1!")
                .clickButtonSubmitWithHelper()
                .getHelperCurrentPassword();

        Allure.step("Проверяю подсказку");
        Assert.assertEquals(helper, "Неверный пароль или логин пользователя");
    }

    @Test
    @Description("Новый пароль и старый одинаковые")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/30")
    public void testNewPasswordAndCurrentPasswordMatches() {
        String helper = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .addValueToFieldLogin(getConfig().getUserName())
                .addValueToFieldCurrentPassword(getConfig().getPassword())
                .addValueToFieldNewPassword(getConfig().getPassword())
                .getHelperNewPassword();

        Allure.step("Проверяю подсказку");
        Assert.assertEquals(helper, "Новый пароль не должен совпадать с текущим паролем");
    }

    @Test
    @Description("Повторное использование пароля при смене пароля")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/231")
    public void testPasswordReuse() {
        String helper = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .addValueToFieldLogin(getConfig().getUserName())
                .addValueToFieldCurrentPassword(getConfig().getPassword())
                .addValueToFieldNewPassword("Rita123$%^")
                .clickButtonSubmitWithHelper()
                .getHelperNewPassword();

        Allure.step("Проверяю подсказку");
        Assert.assertEquals(helper, "Пароль уже использовался ранее");
    }

    @Test
    @Description("Клик по лого на странице Сменить пароль ")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/191")
    public void testClickLogo() {
        LoginPage loginPage = new LoginPage(getDriver())
                .clickButtonSetNewPassword()
                .clickLogo();

        Allure.step("Проверяю, что url страницы Логин");
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@data-testid='SystemLogin-form']")));
        Assert.assertEquals(loginPage.getCurrentUrl(), String.format("%s/login", getConfig().getBaseUrl()));
    }
}
