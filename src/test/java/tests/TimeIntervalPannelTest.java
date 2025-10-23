package tests;

import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.BaseTest;

import java.time.Duration;
import java.util.List;

public class TimeIntervalPannelTest extends BaseTest {

    // список ожидаемых названий интервалов в выпадающем списке
    private final List<String> expectedIntervals = List.of(
            "За смену (8 часов)",
            "За сутки",
            "За неделю",
            "За месяц",
            "За год",
            "За выбранный интервал"
    );

    //логин
    private DashboardPage loginToApp() {
        String login = getConfig().getUserName();
        String password = getConfig().getPassword();

        new LoginPage(getDriver())
                .addValueToFieldLogin(login)
                .addValueToFieldPassword(password)
                .clickButtonLoginWithHelper();

        DashboardPage dashboardPage = new DashboardPage(getDriver());
        // ждём, пока появится дашборд
//        dashboardPage.getWait10().until(ExpectedConditions.urlContains("/dashboard"));
//
//        return new DashboardPage(getDriver());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        // проверяем, что действительно перешли
        String currentUrl = getDriver().getCurrentUrl();
        if (!currentUrl.contains("/dashboard")) {
            Allure.addAttachment("After-login URL", currentUrl);
            Allure.addAttachment("Page HTML (after failed login)", "text/html", getDriver().getPageSource(), ".html");
            throw new AssertionError("Не удалось перейти на /dashboard. Текущий URL: " + currentUrl);
        }

        return dashboardPage;
    }

    //выбор интервала
    private void selectIntervalByDataValue(String dataValue) {
        DashboardPage dashboardPage = new DashboardPage(getDriver());

        dashboardPage.openTimeIntervalDropdown();

        By optionLocator = By.xpath("//ul[@role='listbox']/li[@data-value='" + dataValue + "']");
//        WebElement option = dashboardPage.getWait10().until(ExpectedConditions.elementToBeClickable(optionLocator));
        WebElement option = getWait5().until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
    }

    //выбранный текст
    private String getSelectedIntervalText() {
        DashboardPage staticPage = new DashboardPage(getDriver());
        return staticPage.timeIntervalSelected();
    }

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что по умолчанию указывается временной интервал 'За смену (8 часов)'")
    @Severity(SeverityLevel.MINOR)
    public void testDefaultTimeIntervalIsWorkDay() {

        String selected = loginToApp().timeIntervalSelected();

        Allure.step("Выбранный временной интервал: " + selected);
        Assert.assertEquals(selected, "За смену (8 часов)",
                "Ожидалось, что по умолчанию выбран интервал 'За смену (8 часов)'");
    }

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что при открытии выпадающего списка отображаются все ожидаемые варианты")
    @Severity(SeverityLevel.NORMAL)
    public void testDropdownContainsAllExpectedOptions() {

        List<String> actualOptions = loginToApp()
                .openTimeIntervalDropdown()
                .getAllOptions();

        Allure.step("Фактические значения выпадающего списка: " + actualOptions);

        for (String expected : expectedIntervals) {
            boolean found = false;

            for (String actual : actualOptions) {
                if (actual.trim().equals(expected)) {
                    found = true;
                    break;
                }
            }

            Assert.assertTrue(found, "Не найден ожидаемый интервал: " + expected);
        }
    }

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За смену (8 часов)'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectWorkDay() {
        loginToApp();
        getWait10().until(ExpectedConditions.urlContains("precision="));
        selectIntervalByDataValue("WORK_DAY");

//        waitForSeconds(5);

        String selectedText = getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За смену (8 часов)",
                "После выбора WORK_DAY должен отображаться 'За смену (8 часов)'");
    }

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За сутки'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectFullDay() {
        loginToApp();
        getWait10().until(ExpectedConditions.urlContains("precision="));
        selectIntervalByDataValue("FULL_DAY");


//        waitForSeconds(5);

        String selectedText = getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За сутки",
                "После выбора FULL_DAY должен отображаться 'За сутки'");
    }

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За неделю'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectWeek() {
        loginToApp();
        getWait10().until(ExpectedConditions.urlContains("precision="));
        selectIntervalByDataValue("WEEK");

//        waitForSeconds(5);

        String selectedText = getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За неделю",
                "После выбора WEEK должен отображаться 'За неделю'");
    }

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За месяц'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectMonth() {
        loginToApp();
        getWait10().until(ExpectedConditions.urlContains("precision="));
        selectIntervalByDataValue("MONTH");

//        waitForSeconds(5);

        String selectedText = getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За месяц",
                "После выбора MONTH должен отображаться 'За месяц'");
    }

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За год'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectYear() {
        loginToApp();
        getWait10().until(ExpectedConditions.urlContains("precision="));
        selectIntervalByDataValue("YEAR");

//        waitForSeconds(5);

        String selectedText = getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За год",
                "После выбора YEAR должен отображаться 'За год'");
    }

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За выбранный интервал'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectCustomRange() {
        loginToApp();
        getWait10().until(ExpectedConditions.urlContains("precision="));
        selectIntervalByDataValue("SELECTED_RANGE");

//        waitForSeconds(5);

        String selectedText = getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За выбранный интервал",
                "После выбора SELECTED_RANGE должен отображаться 'За выбранный интервал'");
    }

}
