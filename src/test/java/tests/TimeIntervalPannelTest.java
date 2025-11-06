package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DateTimeAndEquipmentListPage;
import utils.BaseTest;
import utils.TimeUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static pages.DateTimeAndEquipmentListPage.EXPECTED_INTERVALS;
import static utils.TimeUtils.assertTimestampClose;
import pages.DateTimeAndEquipmentListPage;

public class TimeIntervalPannelTest extends BaseTest {

    @BeforeMethod
    protected void initPageObject() {
        page = new DateTimeAndEquipmentListPage(getDriver(), getConfig());
    }


    private DateTimeAndEquipmentListPage page;

    @Test
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что по умолчанию указывается временной интервал 'За смену (8 часов)'")
    @Severity(SeverityLevel.MINOR)
    public void testDefaultTimeIntervalIsWorkDay() {

        String selected = page.loginToApp().timeIntervalSelected();

        Allure.step("Выбранный временной интервал: " + selected);
        Assert.assertEquals(selected, "За смену (8 часов)",
                "Ожидалось, что по умолчанию выбран интервал 'За смену (8 часов)'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что при открытии выпадающего списка отображаются все ожидаемые варианты")
    @Severity(SeverityLevel.NORMAL)
    public void testDropdownContainsAllExpectedOptions() {

        List<String> actualOptions = page.loginToApp()
                .openTimeIntervalDropdown()
                .getAllOptions();

        Allure.step("Фактические значения выпадающего списка: " + actualOptions);

        for (String expected : EXPECTED_INTERVALS) {
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

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За смену (8 часов)'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectWorkDay() {
        page.loginToApp();
        page.selectIntervalByDataValue("WORK_DAY");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За смену (8 часов)",
                "После выбора WORK_DAY должен отображаться 'За смену (8 часов)'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За сутки'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectFullDay() {
        page.loginToApp();
        page.selectIntervalByDataValue("FULL_DAY");


        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За сутки",
                "После выбора FULL_DAY должен отображаться 'За сутки'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За неделю'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectWeek() {
        page.loginToApp();
        page.selectIntervalByDataValue("WEEK");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За неделю",
                "После выбора WEEK должен отображаться 'За неделю'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За месяц'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectMonth() {
        page.loginToApp();
        page.selectIntervalByDataValue("MONTH");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

        Assert.assertEquals(selectedText, "За месяц",
                "После выбора MONTH должен отображаться 'За месяц'");

        String startRaw = page.getUrlParam(url, "startTimestamp");
        String endRaw = page.getUrlParam(url, "endTimestamp");

        Assert.assertNotNull(startRaw, "startTimestamp не найден в URL");
        Assert.assertNotNull(endRaw, "endTimestamp не найден в URL");

        Instant startInstant = Instant.parse(URLDecoder.decode(startRaw, StandardCharsets.UTF_8));
        Instant endInstant = Instant.parse(URLDecoder.decode(endRaw, StandardCharsets.UTF_8));

        // используем UTC, потому что URL имеет Z (UTC)
        ZoneId zone = ZoneOffset.UTC;

        Instant expectedStart = TimeUtils.expectedStartInstantFor(TimeUtils.Interval.MONTH, zone);
        Instant expectedEnd = ZonedDateTime.now(zone).toInstant();

        // tolerance 4 часа??
        long toleranceSeconds = 14400L;

        assertTimestampClose(startInstant, expectedStart, toleranceSeconds, "startTimestamp ~ now.minusYears(1)");
        assertTimestampClose(endInstant, expectedEnd, toleranceSeconds, "endTimestamp ~ now");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За год'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectYear() {
        page.loginToApp();
        page.selectIntervalByDataValue("YEAR");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

        Assert.assertEquals(selectedText, "За год",
                "После выбора YEAR должен отображаться 'За год'");

        String startRaw = page.getUrlParam(url, "startTimestamp");
        String endRaw = page.getUrlParam(url, "endTimestamp");

        Assert.assertNotNull(startRaw, "startTimestamp не найден в URL");
        Assert.assertNotNull(endRaw, "endTimestamp не найден в URL");

        Instant startInstant = Instant.parse(URLDecoder.decode(startRaw, StandardCharsets.UTF_8));
        Instant endInstant = Instant.parse(URLDecoder.decode(endRaw, StandardCharsets.UTF_8));

        // задаём таймзону (РФ = Africa/Addis_Ababa??) и ожидаемое начало
//        ZoneId zone = ZoneId.of("Africa/Addis_Ababa");
//        Instant expectedStart = expectedStartInstantFor(Interval.YEAR, zone);
//        Instant expectedEnd = ZonedDateTime.now(zone).toInstant();

        // используем UTC, потому что URL имеет Z (UTC)
        ZoneId zone = ZoneOffset.UTC;

        Instant expectedStart = TimeUtils.expectedStartInstantFor(TimeUtils.Interval.YEAR, zone);
        Instant expectedEnd = ZonedDateTime.now(zone).toInstant();

        // tolerance 4 часа??
        long toleranceSeconds = 14400L;

        assertTimestampClose(startInstant, expectedStart, toleranceSeconds, "startTimestamp ~ now.minusYears(1)");
        assertTimestampClose(endInstant, expectedEnd, toleranceSeconds, "endTimestamp ~ now");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За выбранный интервал'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectCustomRange() {
        page.loginToApp();
        page.selectIntervalByDataValue("SELECTED_RANGE");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За выбранный интервал",
                "После выбора SELECTED_RANGE должен отображаться 'За выбранный интервал'");
    }

}
