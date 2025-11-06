package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DateTimeAndEquipmentListPage;
import pages.IntervalData;
import utils.BaseTest;
import utils.TimeUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

        Assert.assertEquals(selectedText, "За смену (8 часов)",
                "После выбора WORK_DAY должен отображаться 'За смену (8 часов)'");

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.MONTH, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 14400L;
        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusYears(1)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");
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

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

        Assert.assertEquals(selectedText, "За сутки",
                "После выбора FULL_DAY должен отображаться 'За сутки'");

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.MONTH, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 14400L;
        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusYears(1)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");
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

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

        Assert.assertEquals(selectedText, "За неделю",
                "После выбора WEEK должен отображаться 'За неделю'");

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.MONTH, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 14400L;
        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusYears(1)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");
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

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.MONTH, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 14400L;
        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusYears(1)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");
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

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.YEAR, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 14400L;
        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusYears(1)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");

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
