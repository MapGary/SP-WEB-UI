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
import pages.LoginPage;
import utils.BaseTest;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;
import java.util.List;

public class GettingAggregateDataLoadTimeTest extends BaseTest {

    @FindBy(xpath = "//div[contains(@class, 'MuiTabPanel-root')]//p/..")
    private WebElement unitIcon;

    @Test
    @Epic("Получение времени загрузки данных агрегата За смену на вкладке Схема")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShiftSchema() {

        // авторизуюсь
        new LoginPage(getDriver()).loginToApp();
        // засекаю время загрузки дашборд
        long startTime1 = System.currentTimeMillis();
        // жду обновления дашборд на вкладке Схема
        getWait10().until(ExpectedConditions.urlContains("tab=1"));
        // останавливаю время загрузки дашборд
        long endTime1 = System.currentTimeMillis();
        // жду когда загрузится рабочая область
        getWait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/..")));
        // получаю ссылки на все агрегаты для первой станции
        List<WebElement> elements = getDriver().findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/.."));
        // перебираю все агрегаты для первой станции
        Allure.step("перебираю все агрегаты на первой странице");
        for (int i = 0; i < elements.size(); i++) {
            // засекаю время загрузки рабочего окна
            long startTime = System.currentTimeMillis();
            List<WebElement> eles = getDriver().findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/.."));
            eles.get(i).click();
            // ожидаю загрузку рабочей области
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiContainer-root')]")));
            // получаю 4 окна
            List<WebElement> windows = getDriver().findElements(By.xpath("//div[@id='panel1a-content']"));
            // проверяю, что каждое окно загрузилось
            for (int j = 0; j < windows.size(); j++) {
                getWait10().until(ExpectedConditions.visibilityOf(windows.get(j)));
            }
            // название агрегата
            String unit = getDriver().findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]//p[contains(@class, 'MuiTypography-root')]")).getText();
            // останавливаю время загрузки рабочего окна
            long endTime = System.currentTimeMillis();
            // вывожу время теста
            LoggerUtil.info(String.format("Время выполнения =  %s мс", (endTime1 - startTime1) + (endTime - startTime)));
            // делаю скрин рабочей области
            WebElement webElement = getDriver().findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]"));
            byte[] screen = null;
            try {
                screen = webElement.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(String.format("Агрегат - %s, Время выполнения -> %s", unit, String.valueOf((endTime1 - startTime1) + (endTime - startTime))), new ByteArrayInputStream(screen));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // перехожу к виду первой станции и всех ее агрегатов
            getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(@class, 'MuiTreeItem-root')]/div"))).click();
        }
    }

    @Test
    @Epic("Получение времени загрузки данных агрегата За последние 5 мин на вкладке Схема")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShiftSchema5min() {

        new LoginPage(getDriver())
                .loginToApp()
                .chooseInterval5Minutes()
                .work();
    }

    @Test
    @Epic("Получение времени загрузки данных агрегата За последний час на вкладке Схема")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShiftSchemaHour() {

        new LoginPage(getDriver())
                .loginToApp()
                .chooseIntervalHour()
                .work();
    }

    @Test
    @Epic("Получение времени загрузки данных агрегата За смену на вкладке Журнал")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShiftMagazine() {

        // авторизуюсь
        new LoginPage(getDriver()).loginToApp();
        // засекаю время загрузки дашборд
        long startTime1 = System.currentTimeMillis();
        // жду обновления дашборд на вкладке Схема
        getWait10().until(ExpectedConditions.urlContains("tab=1"));
        // перехожу на вкладку Журнал
        getDriver().findElement(By.xpath("//button[contains(@id, 'T-2')]")).click();
        // жду обновления дашборд на вкладке Журнал
        getWait10().until(ExpectedConditions.urlContains("tab=2"));
        // останавливаю время загрузки дашборд
        long endTime1 = System.currentTimeMillis();
        // жду когда загрузится рабочая область
        getWait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'P-2')]")));
        // получаю ссылки на все агрегаты по алфавиту
        List<WebElement> elements = getDriver().findElements(By.xpath("//div[@class='MuiDataGrid-row']/div[@aria-colspan='1']/.."));
        // перебираю все агрегаты для первой станции
        Allure.step("перебираю все агрегаты для первой станции");
        for (int i = 0; i < elements.size(); i++) {
            // засекаю время загрузки рабочего окна
            long startTime = System.currentTimeMillis();
            List<WebElement> eles = getDriver().findElements(By.xpath("//div[@class='MuiDataGrid-row']/div[@aria-colspan='1']"));
            eles.get(i).click();
            // ожидаю загрузку рабочей области
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id, 'P-2')]")));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // название агрегата
            String unit = getDriver().findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]//p[contains(@class, 'MuiTypography-root')]")).getText();
            // останавливаю время загрузки рабочего окна
            long endTime = System.currentTimeMillis();
            // вывожу время теста
            LoggerUtil.info(String.format("Время выполнения =  %s мс", (endTime1 - startTime1) + (endTime - startTime)));
            // делаю скрин рабочей области
            WebElement webElement = getDriver().findElement(By.xpath("//div[contains(@id, 'P-2')]"));
            byte[] screen = null;
            try {
                screen = webElement.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(String.format("Агрегат - %s, Время выполнения -> %s", unit, String.valueOf((endTime1 - startTime1) + (endTime - startTime))), new ByteArrayInputStream(screen));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // перехожу к виду первой станции и всех ее агрегатов
            getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(@class, 'MuiTreeItem-root')]/div"))).click();
        }
    }

    @Test
    @Epic("Получение времени загрузки данных агрегата За смену на вкладке События")
    @Severity(SeverityLevel.CRITICAL)
    public void testReceivingDataForShiftEvents() {

        // авторизуюсь
        new LoginPage(getDriver()).loginToApp();
        // засекаю время загрузки дашборд
        long startTime1 = System.currentTimeMillis();
        // жду обновления дашборд согласно дефолтного временного периода
        getWait10().until(ExpectedConditions.urlContains("precision="));
        // останавливаю время загрузки дашборд
        long endTime1 = System.currentTimeMillis();
        // жду когда загрузится рабочая область
        getWait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/..")));
        // получаю ссылки на все агрегаты для первой станции
        List<WebElement> elements = getDriver().findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/.."));
        // перебираю все агрегаты для первой станции
        Allure.step("перебираю все агрегаты для первой станции");
        for (int i = 0; i < elements.size(); i++) {
            // засекаю время загрузки рабочего окна
            long startTime = System.currentTimeMillis();
            List<WebElement> eles = getDriver().findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p/.."));
            eles.get(i).click();
            // ожидаю загрузку рабочей области
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiContainer-root')]")));
            // получаю 4 окна
            List<WebElement> windows = getDriver().findElements(By.xpath("//div[@id='panel1a-content']"));
            // проверяю, что каждое окно загрузилось
            for (int j = 0; j < windows.size(); j++) {
                getWait10().until(ExpectedConditions.visibilityOf(windows.get(j)));
            }
            // название агрегата
            String unit = getDriver().findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]//p[contains(@class, 'MuiTypography-root')]")).getText();
            // останавливаю время загрузки рабочего окна
            long endTime = System.currentTimeMillis();
            // вывожу время теста
            LoggerUtil.info(String.format("Время выполнения =  %s мс", (endTime1 - startTime1) + (endTime - startTime)));
            // делаю скрин рабочей области
            WebElement webElement = getDriver().findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]"));
            byte[] screen = null;
            try {
                screen = webElement.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(String.format("Агрегат - %s, Время выполнения -> %s", unit, String.valueOf((endTime1 - startTime1) + (endTime - startTime))), new ByteArrayInputStream(screen));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // перехожу к виду первой станции и всех ее агрегатов
            getWait5().until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(@class, 'MuiTreeItem-root')]/div"))).click();
        }
    }
}
