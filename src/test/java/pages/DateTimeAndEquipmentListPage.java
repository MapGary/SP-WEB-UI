package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import utils.TestConfig;
import utils.TimeUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeAndEquipmentListPage extends DashboardPage{

    private final WebDriver driver;
    private final TestConfig config;

    public DateTimeAndEquipmentListPage(WebDriver driver, TestConfig config) {
        super(driver);
        this.driver = driver;
        this.config = config;
    }

    private static final DateTimeFormatter READABLE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final By equipmentFilterBtn = By.cssSelector("button svg[data-testid='TuneIcon']");
    private final By dialogContainer = By.cssSelector("div[role='dialog']");
    private final By pathInput = By.cssSelector("input[name='path']");
    private final By pathOpenButton = By.xpath("//input[@name='path']/following::button[@title='Open'][1]");
    private final By loadingSpinner = By.cssSelector("svg.MuiCircularProgress-svg");
    private final By okButton = By.xpath("//button[normalize-space() = 'Ок']");
    private final By autocompleteListItems = By.xpath("//ul[@role='listbox']/li");
    private final By pathSelector = By.xpath("//ul[@role='listbox']/li[not(contains(@class,'MuiListSubheader-root'))][1]");

    private By inputByName(String name) {
        return By.cssSelector("input[name='" + name + "']");
    }

    private void openAutocompleteForInput(String inputName) {
        By openBtn = By.xpath("//input[@name='" + inputName + "']/following::button[@title='Open'][1]");
        getWait5().until(ExpectedConditions.elementToBeClickable(openBtn)).click();
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
    public DashboardPage selectIntervalByDataValue(String dataValue) {
        DashboardPage dashboardPage = new DashboardPage(driver);

        dashboardPage.openTimeIntervalDropdown();

        By optionLocator = By.xpath("//ul[@role='listbox']/li[@data-value='" + dataValue + "']");
        WebElement option = dashboardPage.getWait10().until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
        return dashboardPage;
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
        // время окончания из URL как базовое для расчетов
        if (data.endInstant != null) {
            // время из URL и вычитаем интервал для начала
            ZonedDateTime baseTime = ZonedDateTime.ofInstant(data.endInstant, logicZone);
            data.expectedStart = baseTime.minus(interval.amount, interval.unit).toInstant();
            data.expectedEnd = data.endInstant;
        } else {
            // на старую логику
            data.expectedStart = TimeUtils.expectedStartInstantFor(interval, logicZone);
            data.expectedEnd = ZonedDateTime.now(logicZone).toInstant();
        }

        data.diffStartSec = (data.startInstant == null || data.expectedStart == null)
                ? -1L : Math.abs(Duration.between(data.startInstant, data.expectedStart).getSeconds());
        data.diffEndSec = (data.endInstant == null || data.expectedEnd == null)
                ? -1L : Math.abs(Duration.between(data.endInstant, data.expectedEnd).getSeconds());

        data.diffStartHms = data.diffStartSec >= 0 ? formatSecondsHms(data.diffStartSec) : "n/a";
        data.diffEndHms = data.diffEndSec >= 0 ? formatSecondsHms(data.diffEndSec) : "n/a";

        return data;
    }

    @Step("Выбираю пользовательский временной интервал от {dayFrom}-{mouthFrom}-{yearFrom} {hourFrom}:00" +
            " до {dayUp}-{mouthUp}-{yearUp} {hourUp}:00")
    public DashboardPage selectTimeInterval(int dayFrom, int mouthFrom, int yearFrom, int hourFrom,
                                            int dayUp, int mouthUp, int yearUp, int hourUp) {

        DashboardPage dashboardPage = new DashboardPage(driver);
        By intervalField = By.xpath("//div[contains(@class, 'MuiBox-root')]/div[contains(@class, 'MuiFormControl-root')]");
        WebElement customInterval = dashboardPage.getWait10().until(ExpectedConditions.elementToBeClickable(intervalField));
        customInterval.click();

        // устанавливаю дату от
        By fieldFrom = By.xpath("//div[@class='react-datepicker-wrapper'][1]//input");
        getWait5().until(ExpectedConditions.elementToBeClickable(fieldFrom)).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][1]/../div[@class='react-datepicker__tab-loop']//select[@class='react-datepicker__year-select']/option[@value='%s']", String.valueOf(yearFrom)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][1]/../div[@class='react-datepicker__tab-loop']//select[@class='react-datepicker__month-select']/option[@value='%s']", String.valueOf(mouthFrom - 1)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][1]/../div[@class='react-datepicker__tab-loop']//div[contains(@class, 'react-datepicker__day react-datepicker__day--%s')]", String.format("%03d", dayFrom)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][1]/../div[@class='react-datepicker__tab-loop']//ul[@class='react-datepicker__time-list']/li[%s]", String.valueOf(hourFrom + 1)))).click();

        // устанавливаю дату до
        By fieldUp = By.xpath("//div[@class='react-datepicker-wrapper'][2]//input");
        getWait5().until(ExpectedConditions.elementToBeClickable(fieldUp)).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][2]/../div[@class='react-datepicker__tab-loop']//select[@class='react-datepicker__year-select']/option[@value='%s']", String.valueOf(yearUp)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][2]/../div[@class='react-datepicker__tab-loop']//select[@class='react-datepicker__month-select']/option[@value='%s']", String.valueOf(mouthUp - 1)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][2]/../div[@class='react-datepicker__tab-loop']//div[contains(@class, 'react-datepicker__day react-datepicker__day--%s')]", String.format("%03d", dayUp)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][2]/../div[@class='react-datepicker__tab-loop']//ul[@class='react-datepicker__time-list']/li[%s]", String.valueOf(hourUp + 1)))).click();

        return this;
    }

    @Step("Проверяю, что выбранные даты соответствуют датам в URL с учетом временной зоны {zoneId}")
    public void verifySelectedDatesMatchUrl(String url, int dayFrom, int monthFrom, int yearFrom, int hourFrom,
                                            int dayTo, int monthTo, int yearTo, int hourTo, ZoneId zoneId) {

        Instant urlStart = getInstantFromUrl(url, "startTimestamp");
        Instant urlEnd = getInstantFromUrl(url, "endTimestamp");

        Assert.assertNotNull(urlStart, "startTimestamp не найден в URL");
        Assert.assertNotNull(urlEnd, "endTimestamp не найден в URL");

        // ожидаемые даты в указанной временной зоне
        ZonedDateTime expectedStart = ZonedDateTime.of(yearFrom, monthFrom, dayFrom, hourFrom, 0, 0, 0, zoneId).withSecond(0);
        ZonedDateTime expectedEnd = ZonedDateTime.of(yearTo, monthTo, dayTo, hourTo, 0, 0, 0, zoneId).withSecond(0);

        // конвертирование Instant из URL в ту же временную зону
        ZonedDateTime actualStart = urlStart.atZone(zoneId).withSecond(0);
        ZonedDateTime actualEnd = urlEnd.atZone(zoneId).withSecond(0);


        Allure.step(String.format("Ожидаемая дата начала: %s (%s)", expectedStart, zoneId));
        Allure.step(String.format("Фактическая дата начала из URL: %s (%s)", actualStart, zoneId));
        Allure.step(String.format("Ожидаемая дата окончания: %s (%s)", expectedEnd, zoneId));
        Allure.step(String.format("Фактическая дата окончания из URL: %s (%s)", actualEnd, zoneId));

        Assert.assertEquals(actualStart, expectedStart,
                "Дата начала в URL не совпадает с выбранной датой в UI");
        Assert.assertEquals(actualEnd, expectedEnd,
                "Дата окончания в URL не совпадает с выбранной датой в UI");

        Allure.step("Проверка дат пройдена: даты в URL соответствуют выбранным в UI");
    }

    @Step("Открываю фильтр оборудования и ожидаю загрузку поля 'Путь'")
    public DateTimeAndEquipmentListPage openEquipmentFilterAndWait() {
        getWait5().until(ExpectedConditions.elementToBeClickable(equipmentFilterBtn)).click();
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(dialogContainer));
        // жду исчезновение спиннера
        getWait10().until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        // жду доступности поля Путь
        getWait10().until(ExpectedConditions.elementToBeClickable(pathInput));
        Allure.step("Диалог открыт, поле 'Путь' доступно");
        return this;
    }

    @Step("Ввожу в поле 'Путь' (частично) и нажимаю Enter: {part}")
    public DateTimeAndEquipmentListPage typePathAndPressEnter(String part) {
        WebElement el = getWait5().until(ExpectedConditions.elementToBeClickable(pathInput));
        el.clear();
        el.sendKeys(part);
//        el.sendKeys(Keys.ENTER);
        WebElement option = getWait5().until(ExpectedConditions.elementToBeClickable(pathSelector));
        option.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        //value появились (или равны введённому)
        getWait5().until(d -> {
            try {
                String v = el.getAttribute("value");
                return v != null && (v.equals(part) || v.contains(part));
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
        Allure.step("В поле 'Путь' введено и нажато Enter: " + part);
        return this;
    }

    @Step("Ввожу в поле 'Путь' (полностью) и нажимаю Enter: {full}")
    public DateTimeAndEquipmentListPage typeFullPathAndPressEnter(String full) {
        return typePathAndPressEnter(full);
    }

    @Step("Открываю выпадающий список 'Путь' и выбираю элемент, содержащий: {visibleText}")
    public DateTimeAndEquipmentListPage selectPathFromDropdownByVisibleText(String visibleText) {
        getWait5().until(ExpectedConditions.elementToBeClickable(pathOpenButton)).click();

        List<WebElement> items = getWait10().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(autocompleteListItems));

        for (WebElement item : items) {
            String text = item.getText();
            if (text != null && text.toLowerCase().contains(visibleText.toLowerCase())) {
                try {
                    getWait5().until(ExpectedConditions.elementToBeClickable(item));
                    item.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
                }
                Allure.step("Выбран элемент в 'Путь': " + text);
                return this;
            }
        }

        Allure.addAttachment("Dropdown items (path)", String.join("\n", items.stream().map(WebElement::getText).toList()));
        throw new NoSuchElementException("Не найден вариант в Path dropdown содержащий: " + visibleText);
    }

    @Step("Заполняю поле {name} значением '{value}'")
    public DateTimeAndEquipmentListPage setTextField(String name, String value) {
        By locator = inputByName(name);
        WebElement el = getWait5().until(ExpectedConditions.elementToBeClickable(locator));
        el.clear();
        el.sendKeys(value);

        getWait5().until(d -> {
            try {
                String v = el.getAttribute("value");
                return v != null && (v.equals(value) || v.contains(value));
            } catch (StaleElementReferenceException ex) {
                return false;
            }
        });
        Allure.step(String.format("Поле %s заполнено: %s", name, value));
        return this;
    }

    @Step("Выбираю вариант '{optionText}' в автокомплете поля {inputName}")
    public DateTimeAndEquipmentListPage selectOptionFromAutocomplete(String inputName, String optionText) {
        openAutocompleteForInput(inputName);

        List<WebElement> items = getWait10().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(autocompleteListItems));

        for (WebElement item : items) {
            String text = item.getText();
            if (text != null && text.toLowerCase().contains(optionText.toLowerCase())) {
                try {
                    getWait5().until(ExpectedConditions.elementToBeClickable(item));
                    item.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
                }
                Allure.step(String.format("Выбран '%s' для поля %s", text, inputName));
                return this;
            }
        }

        Allure.addAttachment("Dropdown items for " + inputName, String.join("\n", items.stream().map(WebElement::getText).toList()));
        throw new NoSuchElementException("Не найден вариант '" + optionText + "' в автокомплете " + inputName);
    }

    @Step("Получаю значение поля {name}")
    public String getTextFieldValue(String name) {
        return getWait5().until(ExpectedConditions.visibilityOfElementLocated(inputByName(name))).getAttribute("value");
    }

    @Step("Нажимаю кнопку 'Ок' и жду закрытия диалога")
    public DateTimeAndEquipmentListPage clickOkAndWait() {
        getWait5().until(ExpectedConditions.elementToBeClickable(okButton)).click();
        getWait10().until(ExpectedConditions.invisibilityOfElementLocated(dialogContainer));
        Allure.step("Нажата кнопка Ок, диалог закрылся");
        return this;
    }

}
