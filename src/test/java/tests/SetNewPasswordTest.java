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
        Assert.assertTrue(compareExpectedLanguageLoginPage(Language.US, dataLanguageUS));

        Allure.step("Проверяю, что язык соответствует русскому");
        Assert.assertTrue(compareExpectedLanguageLoginPage(Language.RU, dataLanguageRU));
    }
}
