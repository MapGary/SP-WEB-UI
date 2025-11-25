package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.BaseTest;

@Epic("Модуль События")
public class EventsTest extends BaseTest {

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Рабочее окно")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. Перешел к станции БКПРУ-4/СОФ/РВК "Б"/Конвейерный транспорт
            4. Модуль Схема
            5. Перешел в модуль События
            6. Данные в таблице заполнены
            7. Перешел к станции БКПРУ-4/СОФ/РВК "Б"/Насосное оборудование/4.2-2G28
            8. Модуль События
            9. Данные в таблице заполнены""")
    @Severity(SeverityLevel.CRITICAL)
    @Links(value = {@Link(name = "Тест-кейс 143", url = "https://team-b9fb.testit.software/projects/1/tests/143"),
            @Link(name = "Тест-кейс 144", url = "https://team-b9fb.testit.software/projects/1/tests/144")})
    public void testDashboardDownloads() {

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Конвейерный транспорт");

        DashboardPage station = dashboardPage
                .clickEventsModul();

        Assert.assertTrue(station.checkStationTableEvents());

        DashboardPage unit = dashboardPage
                .goToEvents("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28");

        Assert.assertTrue(unit.checkUnitTableEvents());
    }
}
