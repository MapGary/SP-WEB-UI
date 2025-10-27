package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
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

    long startTimeInterval = 0L;
    long endTimeInterval = 0L;
    long time3 = 0L;
    long time4 = 0L;

    //кнопка выбора временного интервала
    private final By selectedInterval = By.id("select-helper");

    // селектор для контейнера списка (любой ul с role='listbox')
    private final By listBox = By.xpath("//ul[@role='listbox']");

    // селектор для элементов внутри listbox: все li с role='option'
    private final By listOptions = By.xpath("//ul[@role='listbox']/li[@role='option']");

    // поле время от
    @FindBy(xpath = "//div[@class='react-datepicker-wrapper'][1]//input")
    private WebElement fieldFrom;

    // поле время до
    @FindBy(xpath = "//div[@class='react-datepicker-wrapper'][2]//input")
    private WebElement fieldUp;

    @FindBy(xpath = "//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]/../..")
    private WebElement workspace;

    // поле выбора временного интервала
    @FindBy(xpath = "//div[contains(@class, 'MuiBox-root')]/div[contains(@class, 'MuiFormControl-root')]")
    private WebElement timeIntervalField;

    // выпадающее меню временного интервала
    @FindBy(xpath = "//ul[contains(@class, 'MuiMenu-list')]")
    private WebElement menuIntervalField;

    // За выбранный интервал
    @FindBy(xpath = "//li[contains(@data-value, 'SELECTED_RANGE')]")
    private WebElement getSelectedRange;

    // Иконка агрегата на рабочей области
    @FindBy(xpath = "//div[contains(@class, 'MuiTabPanel-root')]//p[contains(@class, 'MuiTypography-root')]")
    private List<WebElement> listIconAggregate;

    // Рабочая область
    @FindBy(xpath = "//div[contains(@class, 'MuiContainer-root')]")
    private WebElement workspaceWithTab;

    // Вся страница
    @FindBy(xpath = "//div[contains(@class, 'MuiBox-root')]/header/../..")
    private WebElement page;

    // Список оборудования ссылки 1 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li[contains(@class, 'MuiTreeItem')]/div"))
    private List<WebElement> level1s;

    // Название агрегата
    @FindBy(xpath = "//div[contains(@class, 'MuiContainer-root')]//p[contains(@class, 'MuiTypography-root')]")
    private WebElement nameUnit;

    // 4 окна на рабочей области
    @FindBy(xpath = "//div[@id='panel1a-content']")
    private List<WebElement> workspaceWindows;


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

    @Step("Выбираю временной интервал от {dayFrom}-{mouthFrom}-{yearFrom} {hourFrom}:00" +
            " до {dayUp}-{mouthUp}-{yearUp} {hourUp}:00")
    public DashboardPage selectTimeInterval(int dayFrom, int mouthFrom, int yearFrom, int hourFrom,
                                            int dayUp, int mouthUp, int yearUp, int hourUp) {

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        timeIntervalField.click();
        getWait5().until(ExpectedConditions.elementToBeClickable(menuIntervalField));
        Allure.step("За выбранный интервал");
        getSelectedRange.click();

        // устанавливаю дату от
        getWait5().until(ExpectedConditions.elementToBeClickable(fieldFrom)).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][1]/../div[@class='react-datepicker__tab-loop']//select[@class='react-datepicker__year-select']/option[@value='%s']", String.valueOf(yearFrom)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][1]/../div[@class='react-datepicker__tab-loop']//select[@class='react-datepicker__month-select']/option[@value='%s']", String.valueOf(mouthFrom - 1)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][1]/../div[@class='react-datepicker__tab-loop']//div[contains(@class, 'react-datepicker__day react-datepicker__day--%s')]", String.format("%03d", dayFrom)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][1]/../div[@class='react-datepicker__tab-loop']//ul[@class='react-datepicker__time-list']/li[%s]", String.valueOf(hourFrom + 1)))).click();

        // устанавливаю дату до
        getWait5().until(ExpectedConditions.elementToBeClickable(fieldUp)).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][2]/../div[@class='react-datepicker__tab-loop']//select[@class='react-datepicker__year-select']/option[@value='%s']", String.valueOf(yearUp)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][2]/../div[@class='react-datepicker__tab-loop']//select[@class='react-datepicker__month-select']/option[@value='%s']", String.valueOf(mouthUp - 1)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][2]/../div[@class='react-datepicker__tab-loop']//div[contains(@class, 'react-datepicker__day react-datepicker__day--%s')]", String.format("%03d", dayUp)))).click();
        driver.findElement(By.xpath(String.format("//div[@class='react-datepicker-wrapper'][2]/../div[@class='react-datepicker__tab-loop']//ul[@class='react-datepicker__time-list']/li[%s]", String.valueOf(hourUp + 1)))).click();

        getWait5().until(ExpectedConditions.elementToBeClickable(workspace));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage();

        return this;
    }

    @Step("Делаю скрин всей страницы")
    public DashboardPage takeScreenshotPage() {

        byte[] screen = null;
        try {
            screen = page.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(String.format("Выполнение выбора интервала -> %s", String.valueOf((endTimeInterval - startTimeInterval))), new ByteArrayInputStream(screen));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    @Step("Делаю скрин рабочей области для {unit}")
    public void takeScreenshotWorkspace(String unit, long endTime, long startTime) {

        byte[] screen = null;
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'MuiContainer-root')]"));
        try {
            screen = webElement.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(String.format("Агрегат - %s, Время загрузки информации -> %s", unit, String.valueOf((endTime - startTime))), new ByteArrayInputStream(screen));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkWindowsLoad() {

        // жду пока окно табличные данные загрузится
        try {
            getWait5().until(ExpectedConditions.elementToBeClickable(workspaceWindows.get(2).findElement(By.xpath("//div[@id='panel1a-content']//div[@class='MuiDataGrid-main css-opb0c2']"))));
        } catch (Exception e) {
            time3 = 5000;
        }
        // жду пока окно данные измерений загрузится
        try {
            getWait10().until(ExpectedConditions.visibilityOf(workspaceWindows.get(3).findElement(By.xpath("//div[@id='panel1a-content']//canvas"))));
        } catch (Exception e) {
            time4 = 10000;
        }
    }

    @Step("Получаю данные для агрегатов")
    public void getAggregateData() {

        getWait5().until(ExpectedConditions.elementToBeClickable(workspaceWithTab));

        Allure.step("Перебираю все агрегаты на первой странице");
//        for (int i = 0; i < listIconAggregate.size(); i++) {
        for (int i = 0; i < 10; i++) {
            // засекаю начало времени загрузки данных о агрегате
            long startTime = System.currentTimeMillis();

            listIconAggregate.get(i).click();
            getWait5().until(ExpectedConditions.elementToBeClickable(workspaceWithTab));

            checkWindowsLoad();

            // останавливаю время загрузки данных о агрегате
            long endTime = System.currentTimeMillis();
            LoggerUtil.info(String.format("Время загрузки информации о агрегате =  %s мс", (endTime - time3 - time4 - startTime)));

            takeScreenshotWorkspace(nameUnit.getText(), endTime, startTime);

            getWait5().until(ExpectedConditions.elementToBeClickable(level1s.get(0))).click();
            getWait5().until(ExpectedConditions.visibilityOf(workspace));
        }
    }


    @FindBy(xpath = "//button[contains(@id, 'T-2')]")
    private WebElement magazine;

    @FindBy(xpath = "//ul[@role='tree']")
    private WebElement list1;

    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level2s;
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-label']"))
    private WebElement level2click;

    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level3s;

    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/ul/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level4s;

    @Step("Перехожу на Журнал")
    public DashboardPage goMagazine() {
        magazine.click();

        return this;
    }

//    @Step("Прохожусь по оборудованию в списке оборудования")
//    public DashboardPage goThroughEquipmentInEquipmentList() {
//
//        getWait5().until(ExpectedConditions.elementToBeClickable(list1));
//        for (WebElement level1 : level1s) {
//            getWait5().until(ExpectedConditions.elementToBeClickable(level1)).click();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            for (WebElement level2 : level2s) {
//                getWait5().until(ExpectedConditions.elementToBeClickable(level2)).click();
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                for (WebElement level3 : level3s) {
//                    getWait5().until(ExpectedConditions.elementToBeClickable(level3)).click();
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    for (WebElement level4 : level4s) {
//                        getWait5().until(ExpectedConditions.elementToBeClickable(level4)).click();
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//        }
//    }
//
//        return this;
//}
}
