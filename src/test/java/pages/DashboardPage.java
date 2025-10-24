package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DashboardPage extends BasePage {

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    //кнопка выбора временного интервала
    private final By selectedInterval = By.id("select-helper");

    //выпадающий список у временного интервала
    private final By timeIntervalDropdown = By.id(":r3:");

    // селектор для контейнера списка (любой ul с role='listbox')
    private final By listBox = By.xpath("//ul[@role='listbox']");

    // селектор для элементов внутри listbox: все li с role='option'
    private final By listOptions = By.xpath("//ul[@role='listbox']/li[@role='option']");

    private By selectedOption = By.xpath("//ul[@role='listbox']/li[@role='option' and @aria-selected='true']");

    @Step("Открываю выпадающий список временных интервалов")
    public DashboardPage openTimeIntervalDropdown() {
        WebElement dropdown = getWait10().until(ExpectedConditions.elementToBeClickable(selectedInterval));
        dropdown.click();
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(listBox));

        return this;

    }

    @Step("Получаю выбранный временной интервал (текст)")
    public String timeIntervalSelected() {
        WebElement element = getWait10().until(ExpectedConditions.visibilityOfElementLocated(selectedInterval));
        String text = element.getText();
        if (text == null) text = "";
        return text.trim();
    }

    @Step("Получаю все значения выпадающего списка")
    public List<String> getAllOptions() {
        List<WebElement> elements = getWait10().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(listOptions));

        List<String> result = new ArrayList<>();
        for (WebElement e : elements) {
            String text = e.getText();
            if (text == null) text = "";
            result.add(text.trim());
        }
        return result;
    }

    @Step("Выбираю интервал последние 5 минут")
    public DashboardPage chooseInterval5Minutes() {

// засекаю время загрузки дашборд
        long startTime = System.currentTimeMillis();
        new DashboardPage(driver).selectIntervalByDataValue("SELECTED_RANGE");

        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'0Z'");
        String startTimestamp = fiveMinutesAgo.format(formatter).replace(":", "%3A");

        String endTimestamp = OffsetDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'0Z'")).replace(":", "%3A");
        String url = String.format("http://10.0.0.238/dashboard?view=0&measureType=0&object=0&precision=hour&tab=1&stationId=136&startTimestamp=%s&endTimestamp=%s&rangeType=SELECTED_RANGE", startTimestamp, endTimestamp);
        driver.get(url);
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")));
// останавливаю время загрузки дашборд
        long endTime = System.currentTimeMillis();
        LoggerUtil.info(String.format("Выполнение выбора интервала =  %s мс", (endTime - startTime)));

// делаю скрин рабочей области
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]"));
        byte[] screen = null;
        try {
            screen = webElement.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(String.format("Выполнение выбора интервала -> %s", String.valueOf((endTime - startTime))), new ByteArrayInputStream(screen));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    @Step("Выбираю интервал последний час")
    public DashboardPage chooseIntervalHour() {

// засекаю время загрузки дашборд
        long startTime = System.currentTimeMillis();
        new DashboardPage(driver).selectIntervalByDataValue("SELECTED_RANGE");

        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(60);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'0Z'");
        String startTimestamp = fiveMinutesAgo.format(formatter).replace(":", "%3A");

        String endTimestamp = OffsetDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'0Z'")).replace(":", "%3A");
        String url = String.format("http://10.0.0.238/dashboard?view=0&measureType=0&object=0&precision=hour&tab=1&stationId=136&startTimestamp=%s&endTimestamp=%s&rangeType=SELECTED_RANGE", startTimestamp, endTimestamp);
        driver.get(url);
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")));
// останавливаю время загрузки дашборд
        long endTime = System.currentTimeMillis();
        LoggerUtil.info(String.format("Выполнение выбора интервала =  %s мс", (endTime - startTime)));

// делаю скрин рабочей области
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]"));
        byte[] screen = null;
        try {
            screen = webElement.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(String.format("Выполнение выбора интервала -> %s", String.valueOf((endTime - startTime))), new ByteArrayInputStream(screen));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    @Step("Работа с агрегатами")
    public void work() {
        // получаю ссылки на все агрегаты для первой станции
        List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]"));
        // перебираю все агрегаты для первой станции
        Allure.step("перебираю все агрегаты на первой странице");
        for (int i = 0; i < elements.size(); i++) {
            // засекаю время загрузки рабочего окна
            long startTime = System.currentTimeMillis();
            List<WebElement> eles = driver.findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]"));
            eles.get(i).click();
            // ожидаю загрузку рабочей области
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiContainer-root')]")));
            // получаю 4 окна
            List<WebElement> windows = driver.findElements(By.xpath("//div[@id='panel1a-content']"));
            // проверяю, что каждое окно загрузилось
            for (int j = 0; j < windows.size(); j++) {
                getWait10().until(ExpectedConditions.visibilityOf(windows.get(j)));
            }
            // название агрегата
            String unit = driver.findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]//p[contains(@class, 'MuiTypography-root')]")).getText();
            // останавливаю время загрузки рабочего окна
            long endTime = System.currentTimeMillis();
            // вывожу время теста
            LoggerUtil.info(String.format("Время загрузки информации о агрегате =  %s мс", (endTime - startTime)));
            // делаю скрин рабочей области
            WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]"));
            byte[] screen = null;
            try {
                screen = webElement.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(String.format("Агрегат - %s, Время загрузки информации -> %s", unit, String.valueOf((endTime - startTime))), new ByteArrayInputStream(screen));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // перехожу к виду первой станции и всех ее агрегатов
            getWait10().until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(@class, 'MuiTreeItem-root')]/div"))).click();
            // ожидаю загрузку рабочей области
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiContainer-root')]")));
        }
    }
}
