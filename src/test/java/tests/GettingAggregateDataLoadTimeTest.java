package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;

public class GettingAggregateDataLoadTimeTest extends BaseTest {

    @Test(dataProvider = "timeInterval")
    @Epic("Получение времени загрузки данных агрегата на вкладке Схема")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShiftSchema(int dayFrom, int mouthFrom, int yearFrom, int hourFrom,
                                                int dayUp, int mouthUp, int yearUp, int hourUp) {

        new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(dayFrom, mouthFrom, yearFrom, hourFrom,
                        dayUp, mouthUp, yearUp, hourUp)
                .goToTDO()
                .getAggregateDataSchema();
    }

    @Test(dataProvider = "timeInterval")
    @Epic("Получение времени загрузки данных агрегата на вкладке Журнал")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShiftMagazine(int dayFrom, int mouthFrom, int yearFrom, int hourFrom,
                                                  int dayUp, int mouthUp, int yearUp, int hourUp) {

        new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(dayFrom, mouthFrom, yearFrom, hourFrom,
                        dayUp, mouthUp, yearUp, hourUp)
                .goMagazine()
                .goToTDO()
                .getAggregateDataMagazine();
    }

    @Test(dataProvider = "timeInterval")
    @Epic("Получение времени загрузки данных агрегата на вкладке События")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShiftEvents(int dayFrom, int mouthFrom, int yearFrom, int hourFrom,
                                                int dayUp, int mouthUp, int yearUp, int hourUp) {

        new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(dayFrom, mouthFrom, yearFrom, hourFrom,
                        dayUp, mouthUp, yearUp, hourUp)
                .goEvents()
                .goToTDO()
                .getAggregateDataMagazine();
    }

    @DataProvider(name = "timeInterval")
    public Object[][] providerTimeInterval() {
        return new Object[][]{
                {1, 1, 2020, 23, 7, 10, 2025, 00},    // выбранный диапазон
                {11, 6, 2020, 23, 11, 6, 2021, 23},   // за год
                {5, 5, 2020, 00, 5, 6, 2020, 00},     // за месяц
                {30, 5, 2024, 00, 31, 5, 2024, 00},   // за сутки
                {5, 5, 2021, 9, 5, 5, 2021, 10}       // за час
        };
    }
}
