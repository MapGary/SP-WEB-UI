package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.openqa.selenium.Dimension;
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
    @Description("Работа вкладки Данные измерений модуля Схема, вид измерения - Тренд. Выбран один параметр Оборотные. Вид Таблица")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/95")
    public void testSchemeDataMeasurementTrendTurnoverTable() {

        String columnTitle = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .clickButtonTable()
                .selectParameterTurnoverTable()
                .getColumnTitle();

        Allure.step("Проверяю, что название выбранного параметра соответствует название колонки в таблице");
        Assert.assertEquals(columnTitle, listParameters.get(listParameters.size() - 1));
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Данные измерений")
    @Description("Работа вкладки Данные измерений модуля Схема, вид измерения - Тренд. Выбран один параметр Параметры. Вид Таблица")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/95")
    public void testSchemeDataMeasurementTrendParametersTable() {

        String columnTitle = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .clickButtonTable()
                .selectParameterParametersTable()
                .getColumnTitle();

        Allure.step("Проверяю, что название выбранного параметра соответствует название колонки в таблице");
        Assert.assertEquals(columnTitle.replace(" в ", " "), listParameters.get(0));
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Данные измерений")
    @Description("Работа вкладки Данные измерений модуля Схема, вид измерения - Тренд. Выбран один параметр Замеры. Вид Таблица")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/95")
    public void testSchemeDataMeasurementTrendMeasurementsTable() {

        String columnTitle = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .clickButtonTable()
                .selectParameterMeasurementsTable()
                .getColumnTitle();

        Allure.step("Проверяю, что название выбранного параметра соответствует название колонки в таблице");
        Assert.assertEquals(columnTitle.replace(" в ", " "), listParameters.get(1));
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Данные измерений")
    @Description("Работа вкладки Данные измерений модуля Схема, вид измерения - Тренд. Выбран один параметр Оборотные")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/66")
    public void testSchemeDataMeasurementTrendTurnover() {

        List<String> nameGraph = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .selectParameterTurnover()
                .getNameGraph();

        Allure.step("Проверяю, что график один");
        Assert.assertEquals(nameGraph.size(), 1);
        Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
        Assert.assertEquals(nameGraph.get(0), listParameters.get(0));
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Данные измерений")
    @Description("Работа вкладки Данные измерений модуля Схема, вид измерения - Тренд. Выбрано по одному параметру Оборотные и Параметры")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/66")
    public void testSchemeDataMeasurementTrendParameters() {

        List<String> nameGraph = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .selectParameterTurnoverParameters()
                .getNameGraph();

        Allure.step("Проверяю, что график один");
        Assert.assertEquals(nameGraph.size(), 2);
        Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
        for (int i = 0; i < 2; i++) {
            Assert.assertEquals(nameGraph.get(i), (listParameters.get(i)));
        }
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Данные измерений")
    @Description("Работа вкладки Данные измерений модуля Схема, вид измерения - Тренд. Выбрано 5 однотипных параметров Замеры")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/66")
    public void testSchemeDataMeasurementTrendMeasurementsSameType() {

        int count = 5;

        List<String> nameGraph = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .selectParameterMeasurementsSameType(count)
                .getNameGraph();

        Assert.assertEquals(nameGraph.size(), count);
        Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
        for (int i = 0; i < count; i++) {
            // не точная проверка || заменить на &&
            Assert.assertTrue(nameGraph.get(i).contains(listParameters.get(i).split(" ")[0])
                    || nameGraph.get(i).contains(listParameters.get(i).split(" ")[1]));
        }
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Данные измерений")
    @Description("Работа вкладки Данные измерений модуля Схема, вид измерения - Тренд. Выбрано 3 не однотипных параметра Замеры")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/66")
    public void testSchemeDataMeasurementTrendMeasurementsNotSameType() {

        int count = 3;

        List<String> nameGraph = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .selectParameterMeasurementsNotSameType(count)
                .getNameGraph();

        Allure.step("Проверяю, что количество графиков больше чем выбрано параметров");
        Assert.assertTrue(nameGraph.size() > count);
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
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28");

        Assert.assertTrue(dashboardPage.checkUnitSchematicWindow());
        Assert.assertTrue(dashboardPage.checkStatusAndForecastWindow());
        Assert.assertTrue(dashboardPage.checkTableDataWindow());
        Assert.assertTrue(dashboardPage.checkMeasurementDataWindow());
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Схема агрегата")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Схема агрегата'")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/75")
    public void testUnitSchematicWindowCoversEntireWorkArea() {

        Dimension sizeImage = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(2, 3, 4)
                .getSizeImage();

        Assert.assertTrue(sizeImage.width >= 878 || sizeImage.height >= 537);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Состояние и прогнозирование")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Состояние и прогнозирование' вид Спидометр")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/76")
    public void testStatusForecastingSpeedometerWindowCoversEntireWorkArea() {

        Dimension sizeSpeedometer = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(1, 3, 4)
                .getSizeSpeedometer();

        Assert.assertTrue(sizeSpeedometer.width >= 68 || sizeSpeedometer.height >= 26);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Состояние и прогнозирование")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Состояние и прогнозирование' вид Спидометр")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/76")
    public void testStatusForecastingScaleWindowCoversEntireWorkArea() {

        Dimension sizeScale = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(1, 3, 4)
                .clickViewScale()
                .getSizeScale();

        Assert.assertTrue(sizeScale.width >= 80 || sizeScale.height >= 150);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Табличные данные")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Табличные данные'")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/77")
    public void testTableDataWindowCoversEntireWorkArea() {

        Dimension sizeTable = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(1, 2, 4)
                .getSizeTable();

        Assert.assertTrue(sizeTable.width >= 864);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Данные измерений")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Данные измерений'")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/71")
    public void testMeasurementDataWindowCoversEntireWorkArea() {

        Dimension sizeGraph = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(1, 2, 3)
                .getSizeGraph();

        Assert.assertTrue(sizeGraph.width >= 878 || sizeGraph.height >= 537);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Табличные данные")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Табличные данные' вкладка 'События'")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/83")
    public void testTableDataWindowCoversEntireWorkAreaTabEvents() {

        Dimension sizeTableEvents = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(1, 2, 4)
                .clickTabDefects()
                .clickTabEvents()
                .getSizeTable();

        Assert.assertTrue(sizeTableEvents.width >= 864);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Табличные данные")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Табличные данные' вкладка 'Мероприятия ТОиР'")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/80")
    public void testTableDataWindowCoversEntireWorkAreaTabMachineArrangements() {

        Dimension sizeTableMachineArrangements = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.1-2G02")
                .collapseWindows(1, 2, 4)
                .clickTabMachineArrangements()
                .getSizeTableMachineArrangements();

        Assert.assertTrue(sizeTableMachineArrangements.width >= 876);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Табличные данные")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Табличные данные' вкладка 'Дефекты'")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/81")
    public void testTableDataWindowCoversEntireWorkAreaTabDefects() {

        Dimension sizeTableDefects = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(1, 2, 4)
                .clickTabDefects()
                .getSizeTableDefects();

        Assert.assertTrue(sizeTableDefects.width >= 876);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Табличные данные")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Табличные данные' вкладка 'Рекомендации'")
    @Severity(SeverityLevel.BLOCKER)
    @Link("https://team-b9fb.testit.software/projects/1/tests/82")
    public void testTableDataWindowCoversEntireWorkAreaTabRecommendations() {

        Dimension sizeTableRecommendations = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(1, 2, 4)
                .clickTabRecommendations()
                .getSizeTableRecommendations();

        Assert.assertTrue(sizeTableRecommendations.width >= 876);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Табличные данные")
    @Description("Тип объекта 'Агрегат' модуль 'Схема' окно 'Табличные данные' вкладка 'Отчеты'")
    @Severity(SeverityLevel.BLOCKER)
    @Link("")
    public void testTableDataWindowCoversEntireWorkAreaTabReports() {

        Dimension sizeTableReports = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(1, 2, 4)
                .clickTabReports()
                .getSizeTableReports();

        Assert.assertTrue(sizeTableReports.width >= 876);
    }
}
