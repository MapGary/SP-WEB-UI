package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.testng.Tag;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;

public class GettingAggregateDataLoadTimeTest extends BaseTest {

    @Test(dataProvider = "timeInterval", groups = "smoke")
    @Tag("smoke")
    @Feature("Время загрузки данных")
    @Epic("Получение времени загрузки данных агрегата 4.2-2G28 на вкладке Схема")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetDataUnitSchema(int dayFrom, int mouthFrom, int yearFrom, int hourFrom,
                                      int dayUp, int mouthUp, int yearUp, int hourUp) {

        new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(dayFrom, mouthFrom, yearFrom, hourFrom, dayUp, mouthUp, yearUp, hourUp)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28");
    }

    @DataProvider(name = "timeInterval")
    public Object[][] providerTimeInterval() {
        return new Object[][]{
                {1, 1, 2020, 23, 7, 10, 2025, 0},    // выбранный диапазон
                {16, 12, 2020, 12, 16, 12, 2021, 12},   // за год
                {1, 3, 2021, 0, 1, 4, 2021, 0},     // за месяц
                {14, 5, 2021, 9, 15, 5, 2021, 9},   // за сутки
                {14, 5, 2021, 10, 14, 5, 2021, 11}       // за час
        };
    }
}
