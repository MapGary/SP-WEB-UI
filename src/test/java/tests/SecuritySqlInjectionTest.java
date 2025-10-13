package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.SecurityUtils;
import utils.BaseTest;

/**
 * Простой тест на попытку обхода аутентификации через SQL-injection.
 * Тест делает одну попытку логина с тестовым payload'ом и проверяет:
 * 1) приложение осталось на странице логина (URL = {baseUrl}/login),
 * 2) в localStorage не появились jwt/user (т.е. не прошли аутентификацию).

 * Такие проверки детерминированны и не зависят от внешних сервисов.
 */
public class SecuritySqlInjectionTest extends BaseTest {

    @Test
    @Epic("Безопасность")
    @Feature("SQL-injection")
    @Description("Попытка SQL-injection в поля логина/пароля не должна приводить к успешной аутентификации")
    @Severity(SeverityLevel.CRITICAL)
    public void testSqlInjectionDoesNotBypassAuthentication() {
        String payload = SecurityUtils.sqlInjectionPayload();
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.clearFields();
        loginPage.addValueToFieldLogin(payload);
        loginPage.addValueToFieldPassword(payload);
        loginPage.clickButtonLoginWithHelper(); // остаёмся на LoginPage

        // assert 1: остались на странице логина (URL совпадает с baseUrl + "/login")
        String expectedLoginUrl = String.format("%s/login", getConfig().getBaseUrl());
        Allure.step("Проверяю, что остались на странице логина: " + expectedLoginUrl);
        Assert.assertEquals(loginPage.getCurrentUrl(), expectedLoginUrl, "После попытки SQL-injection не должно быть редиректа на защищённую страницу");

        // assert 2: в LocalStorage не появилось jwt/user (как в существующих тестах проекта)
        Allure.step("Проверяю, что в LocalStorage jwt_asu == null");
        Assert.assertEquals(loginPage.getJwtAsu(), "null", "После попытки SQL-injection не должно быть jwt в localStorage");
        Allure.step("Проверяю, что в LocalStorage user == null");
        Assert.assertEquals(loginPage.getUser(), "null", "После попытки SQL-injection не должно быть user в localStorage");
    }
}
