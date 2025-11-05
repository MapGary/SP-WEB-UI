package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.BaseTest;

import java.util.List;

import static utils.Data.Dashboard.listParameters;

@Epic("Модуль Схема")
public class SchemaTest extends BaseTest {

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Данные измерений")
    @Description("Работа вкладки Данные измерений модуля Схема, вид отображения - Тренд. Выбран один параметр Оборотные")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/66")
    public void testDisplayTypeTrend1Parameter() {

        int count = 1;

        List<String> nameGraph = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo()
                .getMeasurementDataGraph("4.2-2G28", count)
                .getNameGraph();

        for (String nameParameter : listParameters) {
            Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
            Assert.assertEquals(nameGraph.get(0), nameParameter);
        }
        Assert.assertEquals(nameGraph.size(), count);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Данные измерений")
    @Description("Работа вкладки Данные измерений модуля Схема, вид отображения - Тренд. Выбран 5 параметров Замеры из конца списка")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/66")
    public void testDisplayTypeTrend5Parameter() {

        int count = 5;

        List<String> nameGraph = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo()
                .getMeasurementDataGraph("4.2-2G28", count)
                .getNameGraph();

        Assert.assertEquals(nameGraph.size(), count);
        for (int i = 0; i < count; i++) {
            Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
            Assert.assertTrue(nameGraph.get(i).contains(listParameters.get(i).split(" ")[0])
                    || nameGraph.get(i).contains(listParameters.get(i).split(" ")[1]));
        }
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Рабочая область")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' отображение рабочей области")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/74")
    public void testWorkAreaDisplay() {

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo()
                .getMeasurementDataGraph("4.2-2G28");

        Assert.assertTrue(dashboardPage.checkUnitSchematicWindow());
        Assert.assertTrue(dashboardPage.checkStatusAndForecastWindow());
        Assert.assertTrue(dashboardPage.checkTableDataWindow());
        Assert.assertTrue(dashboardPage.checkMeasurementDataWindow());
    }
}
