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

import java.io.File;

import static utils.Assert.compareScreenshotsWithTolerance;

public class TestAuthLogin extends BaseTest {

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

        Allure.step("Загрузилась страница Дашборд");
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.id("equipment-content")));
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains(String.format("%s/dashboard", getConfig().getBaseUrl())));
        Assert.assertNotNull(dashboardPage.getRefreshToken());
        Assert.assertNotNull(dashboardPage.getJwtAsu());
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

        Assert.assertEquals(loginPage.getValueToFieldLogin(), login);
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

        Allure.step("Страница не обновилась, на полях появились подсказки");
        Assert.assertEquals(loginPage.getCurrentUrl(), String.format("%s/login", getConfig().getBaseUrl()));
        Assert.assertEquals(loginPage.getHelperTextLogin(), "Поле Логин обязательно для заполнения");
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

        Allure.step("Значение атрибута type для поля password меняется");
        Assert.assertEquals(value, "text");
        Assert.assertEquals(secretValue, "password");
        Allure.step("Сравниваю скрины поля password");
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

        Allure.step("Загрузилась страница Сменить пароль");
        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@data-testid='SetNewPassword-form']")));
        Assert.assertEquals(setNewPasswordPage.getCurrentUrl(), String.format("%s/set-new-password", getConfig().getBaseUrl()));
    }

}
