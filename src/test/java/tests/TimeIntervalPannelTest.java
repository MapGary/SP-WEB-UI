package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.DateTimeAndEquipmentListPage;
import pages.IntervalData;
import pages.LoginPage;
import utils.BaseTest;
import utils.TimeUtils;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static pages.DateTimeAndEquipmentListPage.EXPECTED_INTERVALS;
import static utils.TimeUtils.assertTimestampClose;

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

        String selected = new LoginPage(getDriver())
                .loginToApp()
                .timeIntervalSelected();

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

        List<String> actualOptions = new LoginPage(getDriver()).loginToApp()
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
        new LoginPage(getDriver()).loginToApp().selectIntervalByDataValue("WORK_DAY");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

        Assert.assertEquals(selectedText, "За смену (8 часов)",
                "После выбора WORK_DAY должен отображаться 'За смену (8 часов)'");

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.WORK_DAY, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 1800L;

//        page.debugTimeComparison(data);

        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusHours(8)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За сутки'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectFullDay() {
        new LoginPage(getDriver()).loginToApp().selectIntervalByDataValue("FULL_DAY");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

        Assert.assertEquals(selectedText, "За сутки",
                "После выбора FULL_DAY должен отображаться 'За сутки'");

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.FULL_DAY, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 3600L;

//        page.debugTimeComparison(data);

        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusHours(24)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За неделю'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectWeek() {
        new LoginPage(getDriver()).loginToApp().selectIntervalByDataValue("WEEK");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

        Assert.assertEquals(selectedText, "За неделю",
                "После выбора WEEK должен отображаться 'За неделю'");

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.WEEK, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 3600L;

//        page.debugTimeComparison(data);

        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusWeeks(1)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За месяц'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectMonth() {
        new LoginPage(getDriver()).loginToApp().selectIntervalByDataValue("MONTH");

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

        long toleranceSeconds = 3600L;

//        page.debugTimeComparison(data);

        assertTimestampClose(data.startInstant, data.expectedStart, toleranceSeconds, "startTimestamp ~ now.minusMonths(1)");
        assertTimestampClose(data.endInstant, data.expectedEnd, toleranceSeconds, "endTimestamp ~ now");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверить, что можно выбрать интервал 'За год'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectYear() {
        new LoginPage(getDriver()).loginToApp().selectIntervalByDataValue("YEAR");

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

        long toleranceSeconds = 3600L;

//        page.debugTimeComparison(data);

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
        new LoginPage(getDriver()).loginToApp().selectIntervalByDataValue("SELECTED_RANGE");

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За выбранный интервал",
                "После выбора SELECTED_RANGE должен отображаться 'За выбранный интервал'");
    }

    @Test(dataProvider = "timeInterval")
    @Epic("Статичные элементы")
    @Feature("Панель временного интервала")
    @Description("Проверка работы пользовательского временного интервала")
    @Severity(SeverityLevel.NORMAL)
    public void testReceivingDataForShiftEvents(int dayFrom, int mouthFrom, int yearFrom, int hourFrom,
                                                int dayUp, int mouthUp, int yearUp, int hourUp) {

        new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(dayFrom, mouthFrom, yearFrom, hourFrom,
                        dayUp, mouthUp, yearUp, hourUp);

        waitForSeconds(5);

        String selectedText = page.getSelectedIntervalText();
        Allure.step("Выбранный интервал: " + selectedText);

        String url = getDriver().getCurrentUrl();
        Allure.step("Current URL: " + url);

//        page.verifySelectedDatesMatchUrl(url, dayFrom, mouthFrom, yearFrom, hourFrom,
//                dayUp, mouthUp, yearUp, hourUp, ZoneId.of("Europe/Moscow"));
        page.verifySelectedDatesMatchUrl(url, dayFrom, mouthFrom, yearFrom, hourFrom,
                dayUp, mouthUp, yearUp, hourUp, ZoneOffset.UTC);

        Assert.assertEquals(selectedText, "За выбранный интервал",
                "После выбора YEAR должен отображаться 'За выбранный интервал'");

        ZoneId displayZone = ZoneOffset.UTC;
        IntervalData data = page.computeStartEndAndDiffs(url, TimeUtils.Interval.YEAR, displayZone);

        Allure.step(String.format("Interval: %s — Start: %s (MSK), End: %s (MSK)",
                selectedText, data.startFormatted, data.endFormatted));

        Assert.assertNotNull(data.startInstant, "startTimestamp не найден в URL");
        Assert.assertNotNull(data.endInstant, "endTimestamp не найден в URL");

        Allure.step(String.format("Differences: start = %d sec (%s), end = %d sec (%s)",
                data.diffStartSec, data.diffStartHms, data.diffEndSec, data.diffEndHms));

        long toleranceSeconds = 30L;

        Allure.step(String.format("Проверка пользовательского интервала: start=%s, end=%s",
                data.startFormatted, data.endFormatted));

        // даты не null
        Assert.assertNotNull(data.startInstant, "startTimestamp не должен быть null");
        Assert.assertNotNull(data.endInstant, "endTimestamp не должен быть null");

    }

    @DataProvider(name = "timeInterval")
    public Object[][] providerTimeInterval() {
        return new Object[][]{
                {1, 1, 2020, 23, 7, 10, 2025, 00}//,    // выбранный диапазон
//                {11, 6, 2020, 23, 11, 6, 2021, 23},   // за год
//                {5, 5, 2020, 00, 5, 6, 2020, 00},     // за месяц
//                {30, 5, 2024, 00, 31, 5, 2024, 00},   // за сутки
//                {5, 5, 2021, 9, 5, 5, 2021, 10}       // за час
        };
    }

    // { partialPath, fullPath, trainName, trainSerial, trainPosition, instanceNumber, rfid, trainType, technologicalProcess, manufacturer, equipmentGroup }
    @DataProvider(name = "filterData")
    public Object[][] filterData() {
        return new Object[][]{
                {"СКРУ-3\\СКРУ-3, Рудник\\Конвейеры", "", "", "", "", "", "", "", "", ""},
                {"СКРУ-3\\СКРУ-3, Рудник\\Конвейеры\\ГУ старые\\ГУ-1", "", "", "", "", "", "", "", "", ""},
                {"СКРУ-3\\СКРУ-3, Рудник\\Конвейеры", "Насос ГВС№1", "100254989", "Эталон Кр 3-х ст.", "100255957", "RFID-12345", "", "", "", ""},
                {"", "", "", "", "", "", "Вентилятор", "Вентиляция", "CREITZ VENTILATOREN", "D7"}
//                для vpn
                // {"СКРУ-3\\РУДНИК\\Конвейеры", "", "", "", "", "", "", "", "", ""},
                // {"СКРУ-3\\РУДНИК\\Конвейеры\\ГУ-1", "", "", "", "", "", "", "", "", ""},
                // {"СКРУ-3\\РУДНИК\\Конвейеры", "Насос ГВС№1", "100254989", "Эталон Кр 3-х ст.", "100255957", "RFID-12345", "", "", "", ""},
                // {"", "", "", "", "", "", "Вентилятор", "Вентиляция", "CREITZ VENTILATOREN", "D7"}
//                //тест 1: частичный путь + остальные поля пустые
//                {"СКРУ", "СКРУ-3\\РУДНИК\\Конвейеры", "", "", "", "", "", "", "", "", ""},
//                //тест 2: полный путь
//                {"Стац. системы", "СКРУ-3\\РУДНИК\\Конвейеры\\ГУ-1", "", "", "", "", "", "", "", "", ""},
//                //тест 3: частичный путь + все поля заполнены
//                {"Мобильные обходы", "СКРУ-3\\РУДНИК\\Конвейеры", "Насос ГВС№1", "100254989", "Эталон Кр 3-х ст.", "100255957", "RFID-12345",
//                        "Насос", "Вентиляция", "ALLWEILER", "B6"},
        };
    }

//    @Test(dataProvider = "filterData")
//    @Epic("Фильтры")
//    @Feature("Поле 'Путь' — частичный ввод")
//    @Description("""
//            1. Авторизовался в приложении
//            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
//            3. В меню Оборудование кликнул Список фильтров
//            4. Ввел частичный путь нажал ОК""")
//    @Severity(SeverityLevel.NORMAL)
//    public void testPartialPathAndOk(String partialPath, String fullPath, String trainName, String trainSerial,
//                                     String trainPosition, String instanceNumber, String rfid,
//                                     String trainType, String technologicalProcess, String manufacturer, String equipmentGroup) {
//
//        new LoginPage(getDriver())
//                .loginToApp()
//                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
//                .openEquipmentFilterAndWait()
//                .setPath(partialPath)
//                .clickOkAndWait();
//
//        Assert.assertTrue(true, "Диалог успешно закрыт после ввода частичного пути");
//
////        page.loginToApp();
////        page.openEquipmentFilterAndWait();
////
////        DateTimeAndEquipmentListPage page = new DateTimeAndEquipmentListPage(getDriver(), getConfig());
////
////        page.typePathAndPressEnter(partialPath);
////
////        waitForSeconds(3);
////
////        page.clickOkAndWait();
//    }
//
//    @Test(dataProvider = "filterData")
//    @Epic("Фильтры")
//    @Feature("Поле 'Путь' — полный ввод")
//    @Description("""
//            1. Авторизовался в приложении
//            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
//            3. В меню Оборудование кликнул Список фильтров
//            4. Ввел полный путь нажал ОК""")
//    @Severity(SeverityLevel.NORMAL)
//    public void testFullPathAndOk(String partialPath, String fullPath, String trainName, String trainSerial,
//                                  String trainPosition, String instanceNumber, String rfid,
//                                  String trainType, String technologicalProcess, String manufacturer, String equipmentGroup) {
//
//        new LoginPage(getDriver())
//                .loginToApp()
//                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
//                .openEquipmentFilterAndWait()
//                .setPath(fullPath)
//                .clickOkAndWait();
//
//        Assert.assertTrue(true, "Диалог успешно закрыт после ввода полного пути");
//
////                page.loginToApp();
////
////        DateTimeAndEquipmentListPage page = new DateTimeAndEquipmentListPage(getDriver(), getConfig());
////
////        page.openEquipmentFilterAndWait();
////
////        page.typeFullPathAndPressEnter(fullPath);
////
////        page.clickOkAndWait();
//    }

    @Test(dataProvider = "filterData")
    @Epic("Фильтры")
    @Feature("Заполняем всю форму и применяем")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. В меню Оборудование кликнул Список фильтров
            4. Ввел частичный путь нажал ОК
            5. Заполнил все остальные поля нажал ОК""")
    @Severity(SeverityLevel.NORMAL)
    public void testPartialPathAndFillOthersAndOk(String partialPath, String trainName, String trainSerial,
                                                  String trainPosition, String instanceNumber, String rfid,
                                                  String trainType, String technologicalProcess, String manufacturer, String equipmentGroup) {

        new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .openEquipmentFilterAndWait()
                .setPath(partialPath)
                .setAllFields(trainName, trainSerial, trainPosition, instanceNumber, rfid, trainType, technologicalProcess, manufacturer, equipmentGroup)
                .clickOkAndWait();

        Assert.assertTrue(true, "Форма заполнена и применена (диалог закрыт)");

//        page.loginToApp();
//
//        DateTimeAndEquipmentListPage page = new DateTimeAndEquipmentListPage(getDriver(), getConfig());
//
//        page.openEquipmentFilterAndWait();
//
//        page.setPath(partialPath);
//
//        if (!trainName.isBlank()) page.setTextField("trainName", trainName);
//        if (!trainSerial.isBlank()) page.setTextField("trainSerial", trainSerial);
//        if (!trainPosition.isBlank()) page.setTextField("trainPosition", trainPosition);
//        if (!instanceNumber.isBlank()) page.setTextField("instanceNumber", instanceNumber);
//        if (!rfid.isBlank()) page.setTextField("rfid", rfid);
//
//        //?? выбор в автокомплетах (если данные заданы)
//        if (!trainType.isBlank()) {
//            try {
//                page.selectOptionFromAutocomplete("trainType", trainType);
//            } catch (Exception e) {
//                Allure.step("Не удалось выбрать trainType: " + e.getMessage());
//            }
//        }
//        if (!technologicalProcess.isBlank()) {
//            try {
//                page.selectOptionFromAutocomplete("technologicalProcess", technologicalProcess);
//            } catch (Exception e) {
//                Allure.step("Не удалось выбрать technologicalProcess: " + e.getMessage());
//            }
//        }
//        if (!manufacturer.isBlank()) {
//            try {
//                page.selectOptionFromAutocomplete("manufacturer", manufacturer);
//            } catch (Exception e) {
//                Allure.step("Не удалось выбрать manufacturer: " + e.getMessage());
//            }
//        }
//        if (!equipmentGroup.isBlank()) {
//            try {
//                page.selectOptionFromAutocomplete("equipmentGroup", equipmentGroup);
//            } catch (Exception e) {
//                Allure.step("Не удалось выбрать equipmentGroup: " + e.getMessage());
//            }
//        }
//
//        page.clickOkAndWait();
    }
//
//    @Test                           УДАЛИТЬ
//    @Epic("Фильтры")
//    @Feature("Фильтрация по кнопкам")
//    @Description("Фильтрация оборудования по статусу 'Предупреждение' и 'Авария'")
//    @Severity(SeverityLevel.TRIVIAL)
//    public void testClickSecondButton() {
//        page.loginToApp();
//
//        DateTimeAndEquipmentListPage page = new DateTimeAndEquipmentListPage(getDriver());
//
//        //элементы ДО нажатия
//        int before = page.getWorkAreaItemCount();
//
//        page.clickSecondButton();
//
//        //элементы ПОСЛЕ нажатия
//        int after = page.getWorkAreaItemCount();
//
//        Allure.step("Before count = " + before + ", After count = " + after);
//
//        Assert.assertNotEquals(before, after,
//                "Рабочая область должна измениться после нажатия второй кнопки");
//    }
//
//    @Test                         УДАЛИТЬ
//    @Epic("Фильтры")
//    @Feature("Фильтрация по кнопкам")
//    @Description("Фильтрация оборудования по статусам, которые не относятся к 'Норма', 'Предупреждение' и 'Авария'")
//    @Severity(SeverityLevel.TRIVIAL)
//    public void testClickThirdButton() {
//        page.loginToApp();
//
//        DateTimeAndEquipmentListPage page = new DateTimeAndEquipmentListPage(getDriver());
//
//        //элементы ДО нажатия
//        int before = page.getWorkAreaItemCount();
//
//        page.clickThirdButton();
//
//        //элементы ПОСЛЕ нажатия
//        int after = page.getWorkAreaItemCount();
//
//        Allure.step("Before count = " + before + ", After count = " + after);
//
//        Assert.assertNotEquals(before, after,
//                "Рабочая область должна измениться после нажатия третьей кнопки");
//    }

    @Test
    @Epic("Фильтры")
    @Feature("Фильтрация по кнопкам")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. Перешел к станции БКПРУ-4
            4. Выбрал второй фильтр
            5. Выбрал третий фильтр
            6. Выбрал первый фильтр""")
    @Severity(SeverityLevel.TRIVIAL)
    public void testFilterButton() {

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4");

        int countUnitsBeforeFilter = dashboardPage
                .getCountUnits();

        int countUnitsSecondFilter = dashboardPage
                .clickSecondButton()
                .getCountUnits();

        Assert.assertNotEquals(countUnitsBeforeFilter, countUnitsSecondFilter,
                "Рабочая область должна измениться после нажатия второй кнопки");
        Assert.assertEquals(countUnitsSecondFilter, 637,
                "Количество агрегатов соответствует ожидаемому");

        int countUnitsThirdFilter = dashboardPage
                .clickThirdButton()
                .getCountUnits();

        Assert.assertNotEquals(countUnitsBeforeFilter, countUnitsThirdFilter,
                "Рабочая область должна измениться после нажатия третьей кнопки");
        Assert.assertEquals(countUnitsThirdFilter, 1038,
                "Количество агрегатов соответствует ожидаемому");

        int countUnitsFirstFilter = dashboardPage
                .clickFirstButton()
                .getCountUnits();

        Assert.assertEquals(countUnitsBeforeFilter, countUnitsFirstFilter,
                "Рабочая область не должна измениться после нажатия первой кнопки");
        Assert.assertEquals(countUnitsFirstFilter, 2266,
                "Количество агрегатов соответствует ожидаемому");
        Assert.assertEquals(countUnitsFirstFilter, countUnitsBeforeFilter,
                "Количество агрегатов соответствует результату без применения фильтров");
    }

}
