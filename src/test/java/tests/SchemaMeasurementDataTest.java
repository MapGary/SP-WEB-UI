package tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;

import static utils.Data.Dashboard.listParameters;

@Epic("Модуль Схема")
@Feature("Данные измерений")
public class SchemaMeasurementDataTest extends BaseTest {

    @Test(groups = "smoke")
    @Tag("smoke")
    @Description("Работа вкладки Данные измерений модуля Схема, вид отображения - Тренд")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://team-b9fb.testit.software/projects/1/tests/66")
    public void testDisplayTypeTrend() {

        int count = 1;

        String nameGraph = new LoginPage(getDriver())
                .loginToApp()
                .selectTimeInterval(1, 1, 2020, 23, 7, 10, 2025, 00)
                .goTo()
                .getMeasurementDataGraph("4.2-2G28", count)
                .getNameGraph();

        for (String nameParameter : listParameters) {
            Allure.step("Проверяю, что название выбранного параметра соответствует показываемому");
            Assert.assertEquals(nameGraph, nameParameter);
        }
        Assert.assertEquals(listParameters.size(), count);
    }
}
