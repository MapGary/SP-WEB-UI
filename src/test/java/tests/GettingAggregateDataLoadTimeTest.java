package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;
import java.util.List;

public class GettingAggregateDataLoadTimeTest extends BaseTest {

    @FindBy(xpath = "//div[contains(@class, 'MuiTabPanel-root')]//p/..")
    private WebElement unitIcon;

    @Test
    @Epic("Получение времени загрузки данных агрегата За смену")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShift() {

        // авторизуюсь
        loginToApp();
        // засекаю время загрузки дашборд
        long startTime1 = System.currentTimeMillis();
        // жду обновления дашборд согласно дефолтного временного периода
        getWait5().until(ExpectedConditions.urlContains("precision="));
        // останавливаю время загрузки дашборд
        long endTime1 = System.currentTimeMillis();
        // жду когда загрузится рабочая область
        getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/..")));
        // получаю ссылки на все агрегаты для первой станции
        List<WebElement> elements = getDriver().findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/.."));
        // перебираю все агрегаты для первой станции
        Allure.step("перебираю все агрегаты для первой станции");
        for (int i = 0; i < elements.size(); i++) {

            long startTime = System.currentTimeMillis();
            List<WebElement> eles = getDriver().findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/.."));
            eles.get(i).click();
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiContainer-root')]")));
            String unit = getDriver().findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]//p[contains(@class, 'MuiTypography-root')]")).getText();

            long endTime = System.currentTimeMillis();
            // вывожу время теста
            LoggerUtil.info(String.format("Время выполнения =  %s мс", (endTime1 - startTime1) + (endTime - startTime)));

            WebElement webElement = getDriver().findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]"));
            byte[] screen = null;
            try {
//                Thread.sleep(1000);
                screen = webElement.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(String.format("Агрегат - %s, Время выполнения -> %s", unit, String.valueOf((endTime1 - startTime1) + (endTime - startTime))), new ByteArrayInputStream(screen));
            } catch (Exception e) {
                e.printStackTrace();
            }


            getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(@class, 'MuiTreeItem-root')]/div"))).click();
        }
    }
}
