package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;

import java.util.List;

@Epic("Статичные элементы")
@Feature("Панель временного интервала")
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

    @Test(groups = "smoke")
    @Tag("smoke")
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
    @Description("Проверить, что при открытии выпадающего списка отображаются все ожидаемые варианты")
    @Severity(SeverityLevel.NORMAL)
    public void testDropdownContainsAllExpectedOptions() {

        List<String> actualOptions = new LoginPage(getDriver())
                .loginToApp()
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

    @Test(groups = "smoke")
    @Tag("smoke")
    @Description("Проверить, что можно выбрать интервал 'За смену (8 часов)'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectWorkDay() {
        String selectedText = new LoginPage(getDriver())
                .loginToApp()
                .selectIntervalByDataValue("WORK_DAY")
                .getSelectedIntervalText();

        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За смену (8 часов)",
                "После выбора WORK_DAY должен отображаться 'За смену (8 часов)'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Description("Проверить, что можно выбрать интервал 'За сутки'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectFullDay() {
        String selectedText = new LoginPage(getDriver())
                .loginToApp()
                .selectIntervalByDataValue("FULL_DAY")
                .getSelectedIntervalText();

        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За сутки",
                "После выбора FULL_DAY должен отображаться 'За сутки'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Description("Проверить, что можно выбрать интервал 'За неделю'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectWeek() {
        String selectedText = new LoginPage(getDriver())
                .loginToApp()
                .selectIntervalByDataValue("WEEK")
                .getSelectedIntervalText();

        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За неделю",
                "После выбора WEEK должен отображаться 'За неделю'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Description("Проверить, что можно выбрать интервал 'За месяц'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectMonth() {
        String selectedText = new LoginPage(getDriver())
                .loginToApp()
                .selectIntervalByDataValue("MONTH")
                .getSelectedIntervalText();

        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За месяц",
                "После выбора MONTH должен отображаться 'За месяц'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Description("Проверить, что можно выбрать интервал 'За год'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectYear() {
        String selectedText = new LoginPage(getDriver())
                .loginToApp()
                .selectIntervalByDataValue("YEAR")
                .getSelectedIntervalText();

        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За год",
                "После выбора YEAR должен отображаться 'За год'");
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Description("Проверить, что можно выбрать интервал 'За выбранный интервал'")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectCustomRange() {
        String selectedText = new LoginPage(getDriver())
                .loginToApp()
                .selectIntervalByDataValue("SELECTED_RANGE")
                .getSelectedIntervalText();

        Allure.step("Выбранный интервал: " + selectedText);

        Assert.assertEquals(selectedText, "За выбранный интервал",
                "После выбора SELECTED_RANGE должен отображаться 'За выбранный интервал'");
    }
}
