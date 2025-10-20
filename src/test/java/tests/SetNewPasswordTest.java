package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SetNewPasswordPage;
import utils.BaseTest;
import utils.Language;

import java.util.Map;

import static utils.Assert.compareExpectedLanguageLoginPage;
import static utils.Assert.compareExpectedLanguageSetNewPasswordPage;

public class SetNewPasswordTest extends BaseTest {

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Переключение языка на форме смены пароля")
    @Description("Переключение языка на форме смены пароля")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/18")
    public void testSwitchLanguage() {

        Map<String, String> dataLanguageUS = new LoginPage(getDriver())
                .clickButtonNewPassword()
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
    @Feature("Отображение подсказки под полем 'Текущий пароль' в форме смены пароля")
    @Description("Отображение подсказки под полем 'Текущий пароль' в форме смены пароля")
    @Severity(SeverityLevel.MINOR)
    @Link("https://team-b9fb.testit.software/projects/1/tests/21")
    public void testHelperCurrentPassword() {
        String helperCurrentPassword = new LoginPage(getDriver())
                .clickButtonNewPassword()
                .getHelperCurrentPassword();

        Allure.step("Проверяю, что helper соответствует значению");
        Assert.assertEquals(helperCurrentPassword, Language.RU.getHelperCurrentPassword());
    }
}
