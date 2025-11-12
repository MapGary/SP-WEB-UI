package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;
import utils.SecurityUtils;

public class SecuritySqlInjectionTest extends BaseTest {

    @Test(groups = "smoke")
    @Tag("smoke")
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
        getWait5().until(ExpectedConditions.textToBePresentInElementValue(By.name("login"), payload));

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
