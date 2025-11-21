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
    @Feature("Окно Данные измерений")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. Перешел к агрегату БКПРУ-4/СОФ/РВК "Б"/Насосное оборудование/4.2-2G28
            4. Модуль Схема окно Данные измерений
            5. Выбран параметр Оборотные дефолтный (Частота вращения) перехожу с График на Таблица
            6. Выбран параметр Оборотные дефолтный (Частота вращения) и параметр Параметры перехожу с График на Таблица
            7. Выбран параметр по умолчанию (Частота вращения) и добавляю параметр Параметры, перехожу на Таблица, выбираю один из Замеры, перехожу на График
            8. Перехожу на Таблица, выбираю один из Замеры с несколькими точками, перехожу на График""")
    @Severity(SeverityLevel.CRITICAL)
    @Link(name = "Тест-кейс 96", url = "https://team-b9fb.testit.software/projects/1/tests/96")
    public void testMeasurementDataTableParameters() {

        DashboardPage dashboard = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(new String[]{"Схема агрегата", "Состояние и прогнозирование", "Табличные данные"});

        List<String> graphDefaultName = dashboard
                .getNameGraph();

        List<String> tableDefaultName = dashboard
                .clickButtonTable()
                .getColumnTitle();

        Allure.step("Проверяю,что название графика соответствует названию колонки в таблице");
        for (int i = 0; i < tableDefaultName.size(); i++) {
            Assert.assertEquals(graphDefaultName.get(i), tableDefaultName.get(i));
        }

        List<String> graphParameterName = dashboard
                .clickButtonGraph()
                .selectParameterTurnoverParameters()
                .getNameGraph();

        List<String> tableParameterName = dashboard
                .clickButtonTable()
                .getColumnTitle();

        Allure.step("Проверяю,что название графика соответствует названию колонки в таблице");
        for (int i = 0; i < tableParameterName.size(); i++) {
            Assert.assertEquals(graphParameterName.get(0), tableParameterName.get(0));
        }

        List<String> tableMeasurementsName = dashboard
                .selectParameterMeasurementsSameTypeTable(1)
                .getColumnTitle();

        List<String> graphMeasurementsName = dashboard
                .clickButtonGraph()
                .getNameGraph();

        Allure.step("Проверяю,что название графика соответствует названию колонки в таблице");
        for (int i = 0; i < tableMeasurementsName.size(); i++) {
            Assert.assertEquals(graphMeasurementsName.get(0), tableMeasurementsName.get(0));
        }

        List<String> tableMeasurementsWithPointsName = dashboard
                .clickButtonTable()
                .selectParameterMeasurementsWithPointsTable()
                .getColumnTitle();

        List<String> graphMeasurementsWithPointsName = dashboard
                .clickButtonGraph()
                .getNameGraph();

        Allure.step("Проверяю,что название графика соответствует названию колонки в таблице");
        for (int i = 0; i < tableMeasurementsWithPointsName.size(); i++) {
            Assert.assertEquals(graphMeasurementsWithPointsName.get(i), tableMeasurementsWithPointsName.get(i));
        }
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Данные измерений")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. Перешел к агрегату БКПРУ-4/СОФ/РВК "Б"/Насосное оборудование/4.2-2G28
            4. Модуль Схема окно Данные измерений вид График
            5. Выбран параметр Оборотные дефолтный (Частота вращения)
            6. Выбран параметр Оборотные и один Параметры
            7. Выбрано 5 однотипных параметров Замеры
            8. Выбрано 3 не однотипных параметра Замеры""")
    @Severity(SeverityLevel.CRITICAL)
    @Link(name = "Тест-кейс 66", url = "https://team-b9fb.testit.software/projects/1/tests/66")
    public void testMeasurementDataGraphParameters() {

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(new String[]{"Схема агрегата", "Состояние и прогнозирование", "Табличные данные"});

        List<String> nameDefault = dashboardPage
                .selectParameterTurnover()
                .getNameGraph();

        Allure.step("Проверяю, что график один");
        Assert.assertEquals(nameDefault.size(), 1);
        Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
        Assert.assertEquals(nameDefault.get(0), listParameters.get(0));

        List<String> nameTurnoverParameters = dashboardPage
                .selectParameterTurnoverParameters()
                .getNameGraph();

        Allure.step("Проверяю, что графиков два");
        Assert.assertEquals(nameTurnoverParameters.size(), 2);
        Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
        for (int i = 0; i < 2; i++) {
            Assert.assertEquals(nameTurnoverParameters.get(i), (listParameters.get(i)));
        }

        List<String> name5Parameters = dashboardPage
                .selectParameterMeasurementsSameTypeGraph(5)
                .getNameGraph();

        Assert.assertEquals(name5Parameters.size(), 5);
        Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
        for (int i = 0; i < 5; i++) {
            // не точная проверка || заменить на &&
            Assert.assertTrue(name5Parameters.get(i).contains(listParameters.get(i).split(" ")[0])
                    || name5Parameters.get(i).contains(listParameters.get(i).split(" ")[1]));
        }

        List<String> name3ParametersNotSameType = dashboardPage
                .selectParameterMeasurementsNotSameType(3)
                .getNameGraph();

        Allure.step("Проверяю, что количество графиков больше чем выбрано параметров");
        Assert.assertTrue(name3ParametersNotSameType.size() > 3);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Состояние и прогнозирование")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. Перешел к агрегату БКПРУ-4/СОФ/РВК "Б"/Насосное оборудование/4.2-2G28
            4. Модуль Схема окно Состояние и прогнозирование
            5. Меняю вид отображения на Шкала""")
    @Severity(SeverityLevel.BLOCKER)
    @Link(name = "Тест-кейс 76", url = "https://team-b9fb.testit.software/projects/1/tests/76")
    public void testStatusForecastingScaleWindowCoversEntireWorkArea() {

        Dimension sizeScale = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28")
                .collapseWindows(new String[]{"Табличные данные", "Схема агрегата", "Данные измерений"})
                .clickViewScale()
                .getSizeScale();

        Assert.assertTrue(sizeScale.width >= 80 || sizeScale.height >= 150);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Окно Табличные данные")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. Перешел к агрегату БКПРУ-4/СОФ/РВК "Б"/Насосное оборудование/4.2-2G02
            4. Модуль Схема окно Табличные данные
            5. Данные отображаются в виде События, Мероприятия ТОиР, Дефекты, Рекомендации, Отчеты""")
    @Severity(SeverityLevel.BLOCKER)
    @Links(value = {@Link(name = "Тест-кейс 83", url = "https://team-b9fb.testit.software/projects/1/tests/83"),
            @Link(name = "Тест-кейс 80", url = "https://team-b9fb.testit.software/projects/1/tests/80"),
            @Link(name = "Тест-кейс 81", url = "https://team-b9fb.testit.software/projects/1/tests/81"),
            @Link(name = "Тест-кейс 82", url = "https://team-b9fb.testit.software/projects/1/tests/82")})
    public void testTabularDataDisplayTypes() {

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.1-2G02")
                .collapseWindows(new String[]{"Состояние и прогнозирование", "Схема агрегата", "Данные измерений"});

        Dimension sizeTableEvents = dashboardPage
                .clickTabDefects()
                .clickTabEvents()
                .getSizeTable();

        Allure.step("Проверяю, что вкладка События открыта на весь дашборд");
        Assert.assertTrue(sizeTableEvents.width >= 864);

        Dimension sizeTableMachineArrangements = dashboardPage
                .clickTabMachineArrangements()
                .getSizeTableMachineArrangements();

        Allure.step("Проверяю, что вкладка Мероприятия ТОиР открыта на весь дашборд");
        Assert.assertTrue(sizeTableMachineArrangements.width >= 876);

        Dimension sizeTableDefects = dashboardPage
                .clickTabDefects()
                .getSizeTableDefects();

        Allure.step("Проверяю, что вкладка Дефекты открыта на весь дашборд");
        Assert.assertTrue(sizeTableDefects.width >= 876);

        Dimension sizeTableRecommendations = dashboardPage
                .clickTabRecommendations()
                .getSizeTableRecommendations();

        Allure.step("Проверяю, что вкладка Рекомендации открыта на весь дашборд");
        Assert.assertTrue(sizeTableRecommendations.width >= 876);

        Dimension sizeTableReports = dashboardPage
                .clickTabReports()
                .getSizeTableReports();

        Allure.step("Проверяю, что вкладка Отчеты открыта на весь дашборд");
        Assert.assertTrue(sizeTableReports.width >= 876);
    }

    @Test(groups = "smoke")
    @Tag("smoke")
    @Feature("Рабочая область")
    @Description("""
            1. Авторизовался в приложении
            2. Выбрал временной интервал от 01-01-2020 23:00 до 07-10-2025 00:00
            3. Перешел к агрегату БКПРУ-4/СОФ/РВК "Б"/Насосное оборудование/4.2-2G28
            4. Модуль Схема
            5. 4 окна в рабочей области загрузились
            6. Каждое из 4 окон растянулось на всю рабочую область""")
    @Severity(SeverityLevel.BLOCKER)
    @Links(value = {@Link(name = "Тест-кейс 74", url = "https://team-b9fb.testit.software/projects/1/tests/74"),
            @Link(name = "Тест-кейс 75", url = "https://team-b9fb.testit.software/projects/1/tests/75"),
            @Link(name = "Тест-кейс 76", url = "https://team-b9fb.testit.software/projects/1/tests/76"),
            @Link(name = "Тест-кейс 77", url = "https://team-b9fb.testit.software/projects/1/tests/77"),
            @Link(name = "Тест-кейс 71", url = "https://team-b9fb.testit.software/projects/1/tests/71")})
    public void testDashboardDownloads() {

        DashboardPage dashboardPage = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 0)
                .goTo("БКПРУ-4", "СОФ", "РВК \"Б\"", "Насосное оборудование", "4.2-2G28");

        Assert.assertTrue(dashboardPage.checkUnitSchematicWindow());
        Assert.assertTrue(dashboardPage.checkStatusAndForecastWindow());
        Assert.assertTrue(dashboardPage.checkTableDataWindow());
        Assert.assertTrue(dashboardPage.checkMeasurementDataWindow());

        Dimension sizeImageUnitSchema = dashboardPage
                .collapseWindows(new String[]{"Состояние и прогнозирование", "Табличные данные", "Данные измерений"})
                .getSizeImage();

        Allure.step("Проверяю, что окно Схема агрегата развернулось на весь дашборд");
        Assert.assertTrue(sizeImageUnitSchema.width >= 878 || sizeImageUnitSchema.height >= 537);

        Dimension sizeImageStatusForecasting = dashboardPage
                .collapseWindows(new String[]{"Состояние и прогнозирование"})
                .collapseWindows(new String[]{"Схема агрегата"})
                .getSizeSpeedometer();

        Allure.step("Проверяю, что окно Состояние и прогнозирование развернулось на весь дашборд");
        Assert.assertTrue(sizeImageStatusForecasting.width >= 68 || sizeImageStatusForecasting.height >= 26);


        Dimension sizeTableData = dashboardPage
                .collapseWindows(new String[]{"Табличные данные"})
                .collapseWindows(new String[]{"Состояние и прогнозирование"})
                .getSizeTable();

        Allure.step("Проверяю, что окно Табличные данные развернулось на весь дашборд");
        Assert.assertTrue(sizeTableData.width >= 864);

        Dimension sizeMeasurementData = dashboardPage
                .collapseWindows(new String[]{"Данные измерений"})
                .collapseWindows(new String[]{"Табличные данные"})
                .getSizeGraph();

        Allure.step("Проверяю, что окно Данные измерений развернулось на весь дашборд");
        Assert.assertTrue(sizeMeasurementData.width >= 878 || sizeMeasurementData.height >= 537);
    }
}
