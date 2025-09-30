package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.BaseTest;

public class TestAuthLogin extends BaseTest {

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Вход с валидными логином и паролем")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/8")
    public void testLoginWithValidUsernameAndPassword() throws InterruptedException {

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .addValueToFieldLogin(getConfig().getUserName())
                .addValueToFieldPassword(getConfig().getPassword())
                .clickButtonLogin();

        Allure.step("Загрузилась сатраница Дашборд");
        Thread.sleep(1000);
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains("http://10.0.0.238/dashboard"));
    }

    @Test
    @Epic("Авторизация и аутентификация")
    @Feature("Вход с пустыми полями логина и пароля")
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
}
