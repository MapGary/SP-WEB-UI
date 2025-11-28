package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.BaseTest;

@Epic("Модуль Аналитика")
public class AnalyticsTest extends BaseTest {

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Рабочее окно")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. Перешел к станции БКПРУ-4/СОФ/РВК "Б"/Насосное оборудование
            4. Модуль Схема
            5. Перешел в модуль Аналитика
            6. Выбрал шаблон Статистика агрегатов по заключениям
            7. Построены диаграммы""")
    @Severity(SeverityLevel.CRITICAL)
    @Links(value = {@Link(name = "Тест-кейс 135", url = "https://team-b9fb.testit.software/projects/1/tests/135")})
    public void testDashboardDownloads() {

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование")
                .clickAnalyticsModule();

        DashboardPage diagram = dashboardPage
                .selectSampleAnalyticsModule("Статистика агрегатов по заключениям")
                .clickButtonApply();

        Assert.assertTrue(diagram.checkDiagramAnalyticsModul());
    }
}
