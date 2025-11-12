package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SetNewPasswordPage;
import utils.BaseTest;
import utils.Language;

import java.io.File;
import java.util.Map;

import static utils.Assert.compareExpectedLanguageSetNewPasswordPage;
import static utils.Assert.compareScreenshotsWithTolerance;

public class SetNewPasswordTest extends BaseTest {

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Переключение языка на форме смены пароля")
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
    @Epic("Авторизация и аутентификация")
    @Feature("Проверка видимости пароля (иконка 'глаз') для поля Текущий пароль на странице Cменить пароль")
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
    @Epic("Авторизация и аутентификация")
    @Feature("Проверка видимости пароля (иконка 'глаз') для поля Новый пароль на странице Cменить пароль")
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
    @Epic("Авторизация и аутентификация")
    @Feature("Отображение подсказки под полем 'Текущий пароль' в форме смены пароля")
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
    @Epic("Авторизация и аутентификация")
    @Feature("Попытка отправки формы с пустыми полями при смене пароля")
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
}
