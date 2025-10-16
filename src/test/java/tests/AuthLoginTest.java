package tests;

import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.SetNewPasswordPage;
import utils.BaseTest;
import utils.Language;

import java.io.File;
import java.util.Map;

import static utils.Assert.compareExpectedLanguage;
import static utils.Assert.compareScreenshotsWithTolerance;

public class AuthLoginTest extends BaseTest {

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Вход с валидными логином и паролем")
    @Description("Вход с валидными логином и паролем")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/8")
    public void testLoginWithValidUsernameAndPassword() {

        String login = getConfig().getUserName();
        String password = getConfig().getPassword();

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .addValueToFieldLogin(login)
                .addValueToFieldPassword(password)
                .clickButtonLogin();

        Allure.step("Проверяю, что загрузилась страница Дашборд");
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.id("equipment-content")));
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains(String.format("%s/dashboard", getConfig().getBaseUrl())));
        Allure.step("Проверяю, что в Cookies записалось значение refresh_token");
        Assert.assertNotNull(dashboardPage.getRefreshToken());
        Allure.step("Проверяю, что в refresh_token отмечен HttpOnly");
        Assert.assertTrue(dashboardPage.isHttpOnlyRefreshToken());
        Allure.step("Проверяю поле jwt_asu в Local storage");
        Assert.assertNotNull(dashboardPage.getJwtAsu());
        Allure.step("Проверяю поле user в Local storage");
        Assert.assertNotNull(dashboardPage.getUser());
    }

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Введенные данные сохраняются в значение поля")
    @Description("Введенные данные сохраняются в значение поля")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/8")
    public void testEnteredDataSavedFieldValue() {

        String login = "test login";
        String password = "test password";

        LoginPage loginPage = new LoginPage(getDriver())
                .addValueToFieldLogin(login)
                .addValueToFieldPassword(password);

        Allure.step("Проверяю значение сохраненное в поле Логин");
        Assert.assertEquals(loginPage.getValueToFieldLogin(), login);
        Allure.step("Проверяю значение сохраненное в поле Пароль");
        Assert.assertEquals(loginPage.getValueToFieldPassword(), password);
    }

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Вход с пустыми полями логина и пароля")
    @Description("Вход с пустыми полями логина и пароля")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/9")
    public void testLoginWithEmptyUsernameAndPassword() {

        LoginPage loginPage = new LoginPage(getDriver())
                .clickToFieldLogin()
                .clickToFieldPassword()
                .clickButtonLoginWithHelper();

        Allure.step("Проверяю, что загрузилась страница Логин");
        Assert.assertEquals(loginPage.getCurrentUrl(), String.format("%s/login", getConfig().getBaseUrl()));
        Allure.step("Проверяю, что в поле Логин появилась подсказка");
        Assert.assertEquals(loginPage.getHelperTextLogin(), "Поле Логин обязательно для заполнения");
        Allure.step("Проверяю, что в поле Пароль появилась подсказка");
        Assert.assertEquals(loginPage.getHelperTextPassword(), "Поле Пароль обязательно для заполнения");
    }

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Проверка видимости пароля (иконка 'глаз') на странице Login")
    @Description("Проверка видимости пароля (иконка 'глаз') на странице Login")
    @Severity(SeverityLevel.MINOR)
    @Link("https://team-b9fb.testit.software/projects/1/tests/10")
    public void testCheckingPasswordVisibility() {

        File screenshot1 = new LoginPage(getDriver())
                .addValueToFieldPassword("Test1234!")
                .getScreenshotWebElement();

        String value = new LoginPage(getDriver())
                .clickEyeIcon()
                .getAttributeFieldPassword();

        String secretValue = new LoginPage(getDriver())
                .clickEyeIcon()
                .getAttributeFieldPassword();

        File screenshot2 = new LoginPage(getDriver())
                .clickToFieldPassword()
                .getScreenshotWebElement();

        Allure.step("Проверяю значение атрибута type для поля Пароль, пароль видно");
        Assert.assertEquals(value, "text");
        Allure.step("Проверяю значение атрибута type для поля Пароль, пароль скрыт");
        Assert.assertEquals(secretValue, "password");
        Allure.step("Сравниваю скриншоты поля Пароль");
        Assert.assertTrue(compareScreenshotsWithTolerance(screenshot1, screenshot2, 0.7));
    }

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Переход по кнопке 'Сменить пароль'")
    @Description("Переход по кнопке 'Сменить пароль'")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/11")
    public void testClickChangePasswordButton() {

        SetNewPasswordPage setNewPasswordPage = new LoginPage(getDriver())
                .clickButtonNewPassword();

        Allure.step("Проверяю, что загрузилась страница Сменить пароль");
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@data-testid='SetNewPassword-form']")));
        Assert.assertEquals(setNewPasswordPage.getCurrentUrl(), String.format("%s/set-new-password", getConfig().getBaseUrl()));
    }

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Переключения языка (RU/EN)")
    @Description("Переключения языка (RU/EN)")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/12")
    public void testSwitchLanguage() {

        Map<String, String> dataLanguageUS = new LoginPage(getDriver())
                .getHelperSwitchLanguage()
                .clickSwitchLanguage()
                .clickInactiveLanguage()
                .getTranslatedData();

        Map<String, String> dataLanguageRU = new LoginPage(getDriver())
                .getHelperSwitchLanguage()
                .clickSwitchLanguage()
                .clickInactiveLanguage()
                .getTranslatedData();

        Allure.step("Проверяю, что язык соответствует английскому");
        Assert.assertTrue(compareExpectedLanguage(Language.US, dataLanguageUS));

        Allure.step("Проверяю, что язык соответствует русскому");
        Assert.assertTrue(compareExpectedLanguage(Language.RU, dataLanguageRU));
    }

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Ввод неверного логина или пароля")
    @Description("Ввод неверного логина или пароля")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/13")
    public void testEnterIncorrectLoginOrPassword() {

        String login = "login";
        String password = "098";

        LoginPage loginPage = new LoginPage(getDriver())
                .addValueToFieldLogin(login)
                .addValueToFieldPassword(password)
                .clickButtonLoginWithHelper();

        Allure.step("Проверяю, что url страницы Логин");
        Assert.assertEquals(loginPage.getCurrentUrl(), String.format("%s/login", getConfig().getBaseUrl()));
        Allure.step("Проверяю подсказку в поле Пароль");
        Assert.assertEquals(loginPage.getHelperTextPassword(), "Неверный пароль или логин пользователя");
        Allure.step("Проверяю поле jwt_asu в Local storage");
        Assert.assertEquals(loginPage.getJwtAsu(), "null");
        Allure.step("Проверяю поле user в Local storage");
        Assert.assertEquals(loginPage.getUser(), "null");
    }

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Клик по лого на странице Login")
    @Description("Клик по лого на странице Login")
    @Severity(SeverityLevel.MINOR)
    @Link("https://team-b9fb.testit.software/projects/1/tests/224")
    public void testClickLogo() {

        LoginPage loginPage = new LoginPage(getDriver())
                .clickLogo();

        Allure.step("Проверяю, что url страницы Логин");
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@data-testid='SystemLogin-form']")));
        Assert.assertEquals(loginPage.getCurrentUrl(), String.format("%s/login", getConfig().getBaseUrl()));
    }
}
