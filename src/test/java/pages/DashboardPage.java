package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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

    @FindBy(id = "select-helper")
    private WebElement interval;

    @FindBy(xpath = "//li[@data-value='SELECTED_RANGE']")
    private WebElement selectedRange;

    @FindBy(xpath = "//input[contains(@class, 'MuiInputBase-input')]")
    private WebElement fieldFrom;

    @FindBy(xpath = "//div[@class='react-datepicker-wrapper'][2]")
    private WebElement fieldUp;

    @FindBy(xpath = "//select[@class='react-datepicker__year-select']/option[@value='2017']")
    private WebElement year;

    @FindBy(xpath = "//select[@class='react-datepicker__month-select']/option[@value='0']")
    private WebElement mouth;

    @FindBy(xpath = "//div[contains(@class, 'react-datepicker__day react-datepicker__day--001')]")
    private WebElement day;

    @FindBy(xpath = "//ul[@class='react-datepicker__time-list']/li")
    private WebElement timeFrom;

    @FindBy(xpath = "//div[@class='react-datepicker__time']//li[24]")
    private WebElement timeUp;

    @FindBy(xpath = "//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")
    private WebElement workspace;


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

    @Step("Выбираю интервал последние {minutes} минут")
    public DashboardPage chooseIntervalMinutes(int minutes) {

// засекаю время загрузки дашборд
        long startTime = System.currentTimeMillis();
        new DashboardPage(driver).selectIntervalByDataValue("SELECTED_RANGE");

        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(minutes);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'0Z'");
        String startTimestamp = fiveMinutesAgo.format(formatter).replace(":", "%3A");

        String endTimestamp = OffsetDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'0Z'")).replace(":", "%3A");
        String url = String.format("%s/dashboard?view=0&measureType=0&object=0&precision=hour&tab=1&stationId=136&startTimestamp=%s&endTimestamp=%s&rangeType=SELECTED_RANGE", getConfig().getBaseUrl(), startTimestamp, endTimestamp);
        driver.get(url);
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")));
// останавливаю время загрузки дашборд
        long endTime = System.currentTimeMillis();
        LoggerUtil.info(String.format("Выполнение выбора интервала =  %s мс", (endTime - startTime)));

// делаю скрин всей страницы
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiBox-root')]/header/../.."));
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

    @Step("Выбираю интервал За сутки")
    public DashboardPage chooseIntervalDay() {

// засекаю время загрузки дашборд
        long startTime = System.currentTimeMillis();
        new DashboardPage(driver).selectIntervalByDataValue("FULL_DAY");

        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")));
// останавливаю время загрузки дашборд
        long endTime = System.currentTimeMillis();
        LoggerUtil.info(String.format("Выполнение выбора интервала =  %s мс", (endTime - startTime)));

// делаю скрин всей страницы
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiBox-root')]/header/../.."));
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

    @Step("Выбираю интервал За месяц")
    public DashboardPage chooseIntervalMonth() {

// засекаю время загрузки дашборд
        long startTime = System.currentTimeMillis();
        new DashboardPage(driver).selectIntervalByDataValue("MONTH");

        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")));
// останавливаю время загрузки дашборд
        long endTime = System.currentTimeMillis();
        LoggerUtil.info(String.format("Выполнение выбора интервала =  %s мс", (endTime - startTime)));

// делаю скрин всей страницы
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiBox-root')]/header/../.."));
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

    @Step("Выбираю интервал За год")
    public DashboardPage chooseIntervalYear() {

// засекаю время загрузки дашборд
        long startTime = System.currentTimeMillis();
        new DashboardPage(driver).selectIntervalByDataValue("YEAR");

        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")));
// останавливаю время загрузки дашборд
        long endTime = System.currentTimeMillis();
        LoggerUtil.info(String.format("Выполнение выбора интервала =  %s мс", (endTime - startTime)));

// делаю скрин всей страницы
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiBox-root')]/header/../.."));
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

    @Step("Выбираю интервал За 2017_2025")
    public DashboardPage chooseInterval2017_2025() {

        // засекаю время загрузки дашборд
        long startTime = System.currentTimeMillis();

        new DashboardPage(driver).selectIntervalByDataValue2017_2025();

        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")));
        // останавливаю время загрузки дашборд
        long endTime = System.currentTimeMillis();
        LoggerUtil.info(String.format("Выполнение выбора интервала =  %s мс", (endTime - startTime)));

        // делаю скрин всей страницы
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiBox-root')]/header/../.."));
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
            long time3 = 0L;
            long time4 = 0L;
            List<WebElement> eles = driver.findElements(By.xpath("//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]"));
            eles.get(i).click();
            // ожидаю загрузку рабочей области
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'MuiContainer-root')]")));
            // получаю 4 окна
            List<WebElement> windows = workspace.findElements(By.xpath("//div[@id='panel1a-content']"));
            // проверяю, что окно табличные данные загрузилось
            try {
                getWait5().until(ExpectedConditions.elementToBeClickable(windows.get(2)));
            } catch (Exception e) {
                time4 = 5000;
            }
            // проверяю, что окно данные измерений загрузилось
            try {
                getWait10().until(ExpectedConditions.visibilityOf(windows.get(3).findElement(By.xpath("//div[@id='panel1a-content']//canvas"))));
            } catch (Exception e) {
                time3 = 10000;
            }
            // останавливаю время загрузки рабочего окна
            long endTime = System.currentTimeMillis();
            // вывожу время теста
            LoggerUtil.info(String.format("Время загрузки информации о агрегате =  %s мс", (endTime - time3 - time4 - startTime)));
            // название агрегата
            String unit = driver.findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]//p[contains(@class, 'MuiTypography-root')]")).getText();
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

    //выбор интервала 2017-2025
    @Step("Выбираю интервал 01-01-2017 00:00 -- сегодня 23:00")
    public DashboardPage selectIntervalByDataValue2017_2025() {

        getWait10().until(ExpectedConditions.elementToBeClickable(interval)).click();
        selectedRange.click();
        getWait10().until(ExpectedConditions.elementToBeClickable(fieldFrom)).click();
        year.click();
        mouth.click();
        getWait10().until(ExpectedConditions.elementToBeClickable(day)).click();
        timeFrom.click();

        fieldUp.click();
        getWait10().until(ExpectedConditions.elementToBeClickable(timeUp)).click();

        getWait10().until(ExpectedConditions.elementToBeClickable(workspace));

        return this;
    }
}
