package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TestConfig;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeAndEquipmentListPage {

    private final WebDriver driver;
    private final TestConfig config;

    public DateTimeAndEquipmentListPage(WebDriver driver, TestConfig config) {
        this.driver = driver;
        this.config = config;
    }

    @Step("Получаю параметр из URL")
    public String getUrlParam(String url, String name) {
        try {
            Pattern p = Pattern.compile("[\\?&]" + Pattern.quote(name) + "=([^&]*)");
            Matcher m = p.matcher(url);
            if (m.find()) {
                return URLDecoder.decode(m.group(1), StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) {}
        return null;
    }

    // список ожидаемых названий интервалов в выпадающем списке
    public static final List<String> EXPECTED_INTERVALS = List.of(
            "За смену (8 часов)",
            "За сутки",
            "За неделю",
            "За месяц",
            "За год",
            "За выбранный интервал"
    );

    @Step("Логинюсь в веб-приложении")
    public DashboardPage loginToApp() {
        String login = config.getUserName();
        String password = config.getPassword();

        new LoginPage(driver)
                .addValueToFieldLogin(login)
                .addValueToFieldPassword(password)
                .clickButtonLoginWithHelper();

        DashboardPage dashboardPage = new DashboardPage(driver);
        // ждём, пока появится дашборд
//        dashboardPage.getWait10().until(ExpectedConditions.urlContains("/dashboard"));
//
//        return new DashboardPage(getDriver());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        // проверяем, что действительно перешли
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("/dashboard")) {
            Allure.addAttachment("After-login URL", currentUrl);
            Allure.addAttachment("Page HTML (after failed login)", "text/html", driver.getPageSource(), ".html");
            throw new AssertionError("Не удалось перейти на /dashboard. Текущий URL: " + currentUrl);
        }

        return dashboardPage;
    }

    @Step("Выбираю интервал")
    public void selectIntervalByDataValue(String dataValue) {
        DashboardPage dashboardPage = new DashboardPage(driver);

        dashboardPage.openTimeIntervalDropdown();

        By optionLocator = By.xpath("//ul[@role='listbox']/li[@data-value='" + dataValue + "']");
        WebElement option = dashboardPage.getWait10().until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
    }

    @Step("Выбираю текст из поля временного интервала")
    public String getSelectedIntervalText() {
        DashboardPage staticPage = new DashboardPage(driver);
        return staticPage.timeIntervalSelected();
    }

}
