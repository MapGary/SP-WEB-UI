package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TestConfig;
import utils.TimeUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.*;

public class DateTimeAndEquipmentListPage {

    private final WebDriver driver;
    private final TestConfig config;

    public DateTimeAndEquipmentListPage(WebDriver driver, TestConfig config) {
        this.driver = driver;
        this.config = config;
    }

    private static final DateTimeFormatter READABLE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

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

    // получить Instant из параметра URL (или null)
    @Step("Получаю Instant из URL-параметра {name}")
    public Instant getInstantFromUrl(String url, String name) {
        String raw = getUrlParam(url, name);
        if (raw == null) return null;
        try {
            return Instant.parse(URLDecoder.decode(raw, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return null;
        }
    }

    //получить форматированную строку для Instant в заданной зоне (ZoneOffset.UTC или systemDefault)
    @Step("Форматирую Instant для отчёта")
    public String formatInstantForReport(Instant instant, ZoneId zone) {
        if (instant == null) return "null";
        return READABLE_FMT.withZone(zone).format(instant);
    }

    /**
     * Возвращает Map с start/end Instant и форматированными строками.
     * keys:
     *  - "startInstant" Instant or null
     *  - "endInstant" Instant or null
     *  - "startFormatted" String
     *  - "endFormatted" String
     */
    @Step("Получаю start и end из URL и форматирую для отчёта")
    public Map<String, Object> getStartEndFromUrlFormatted(String url, ZoneId displayZone) {
        Instant start = getInstantFromUrl(url, "startTimestamp");
        Instant end = getInstantFromUrl(url, "endTimestamp");

        Map<String, Object> result = new HashMap<>();
        result.put("startInstant", start);
        result.put("endInstant", end);
        result.put("startFormatted", formatInstantForReport(start, displayZone));
        result.put("endFormatted", formatInstantForReport(end, displayZone));
        return result;
    }

    // Преобразует секунды в человекочитаемый формат "Xh Ym Zs"
    @Step("Форматирую длительность в часы/минуты/секунды")
    public String formatSecondsHms(long totalSeconds) {
        long h = totalSeconds / 3600;
        long m = (totalSeconds % 3600) / 60;
        long s = totalSeconds % 60;
        return String.format("%dh %dm %ds", h, m, s);
    }

    @Step("Получаю start/end и ожидаемые значения + diff для интервала {interval}")
    public IntervalData computeStartEndAndDiffs(String url, TimeUtils.Interval interval, ZoneId displayZone) {
        IntervalData data = new IntervalData();

        data.startInstant = getInstantFromUrl(url, "startTimestamp");
        data.endInstant = getInstantFromUrl(url, "endTimestamp");
        data.startFormatted = formatInstantForReport(data.startInstant, displayZone);
        data.endFormatted = formatInstantForReport(data.endInstant, displayZone);

        ZoneId logicZone = ZoneOffset.UTC;
        data.expectedStart = TimeUtils.expectedStartInstantFor(interval, logicZone);
        data.expectedEnd = ZonedDateTime.now(logicZone).toInstant();

        data.diffStartSec = (data.startInstant == null || data.expectedStart == null)
                ? -1L : Math.abs(Duration.between(data.startInstant, data.expectedStart).getSeconds());
        data.diffEndSec = (data.endInstant == null || data.expectedEnd == null)
                ? -1L : Math.abs(Duration.between(data.endInstant, data.expectedEnd).getSeconds());

        data.diffStartHms = data.diffStartSec >= 0 ? formatSecondsHms(data.diffStartSec) : "n/a";
        data.diffEndHms = data.diffEndSec >= 0 ? formatSecondsHms(data.diffEndSec) : "n/a";

        return data;
    }

}
