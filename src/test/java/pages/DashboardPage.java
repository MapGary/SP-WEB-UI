package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;
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

    // рабочая область схема
    @FindBy(xpath = "//div[contains(@class, 'MuiTabPanel-root')]/div/div/div/div[contains(@class, 'MuiBox-root')]")
    private WebElement workspaceSchema;

    // рабочая область события
    @FindBy(xpath = "//div[contains(@class, 'MuiTabPanel-root')]//div[contains(@class, 'MuiDataGrid-main')]")
    private WebElement workspaceEvents;

    // рабочая область журнал
    @FindBy(xpath = "//div[contains(@class, 'MuiDataGrid-root--densityStandard')]")
    private WebElement workspaceMagazine;

    // поле выбора временного интервала
    @FindBy(xpath = "//div[contains(@class, 'MuiBox-root')]/div[contains(@class, 'MuiFormControl-root')]")
    private WebElement timeIntervalField;

    // выпадающее меню временного интервала
    @FindBy(xpath = "//ul[contains(@class, 'MuiMenu-list')]")
    private WebElement menuIntervalField;

    // За выбранный интервал
    @FindBy(xpath = "//li[contains(@data-value, 'SELECTED_RANGE')]")
    private WebElement getSelectedRange;

    // Лист агрегатов для конкретной станции в списке оборудования
    @FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/ul/div/div/li/ul//li")
    private List<WebElement> listAggregate;

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

    // список оборудования
    @FindBy(xpath = "//ul[contains(@class, 'MuiTreeView-root')]")
    private WebElement equipmentList;

    // кнопка журнал
    @FindBy(xpath = "//button[contains(@id, 'T-2')]")
    private WebElement magazine;

    // кнопка события
    @FindBy(xpath = "//button[contains(@id, 'T-3')]")
    private WebElement events;

    @FindBy(xpath = "//ul[@role='tree']")
    private WebElement list1;

    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level2s;

    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level3s;

    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/ul/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level4s;

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

    @Step("Выбираю временной интервал от {dayFrom}-{mouthFrom}-{yearFrom} {hourFrom}:00" +
            " до {dayUp}-{mouthUp}-{yearUp} {hourUp}:00")
    public DashboardPage selectTimeInterval(int dayFrom, int mouthFrom, int yearFrom, int hourFrom,
                                            int dayUp, int mouthUp, int yearUp, int hourUp) {

        getWait5().until(ExpectedConditions.elementToBeClickable(workspaceSchema));
        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        timeIntervalField.click();
        getWait5().until(ExpectedConditions.elementToBeClickable(menuIntervalField));
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

        getWait5().until(ExpectedConditions.elementToBeClickable(workspaceSchema));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage();

        return this;
    }

    @Step("Делаю скрин всей страницы")
    public DashboardPage takeScreenshotPage() {

        getWait5().until(ExpectedConditions.visibilityOf(list1));
        getWait5().until(ExpectedConditions.visibilityOf(workspaceSchema));
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

    public long getTimeWindowsLoad() {

        long time1 = 0L;
        // жду пока окно табличные данные загрузится
        try {
            getWait5().until(ExpectedConditions.elementToBeClickable(workspaceWindows.get(2).findElement(By.xpath("//div[@id='panel1a-content']//div[contains(@class, 'MuiDataGrid-main')]/.."))));
        } catch (Exception e) {
            time1 += 20000;
        }
        // жду пока окно данные измерений загрузится
        try {
            getWait10().until(ExpectedConditions.visibilityOf(workspaceWindows.get(3).findElement(By.xpath("//div[@id='panel1a-content']//canvas"))));
        } catch (Exception e) {
            time1 += 25000;
        }

        return time1;
    }

    @Step("Получаю данные для агрегатов на вкладке Схема")
    public void getAggregateDataSchema() {

        getWait5().until(ExpectedConditions.elementToBeClickable(equipmentList));

        Allure.step("Перебираю все агрегаты на первой странице");
        for (int i = 0; i < listAggregate.size(); i++) {
            // засекаю начало времени загрузки данных об агрегате
            long startTime = System.currentTimeMillis();

            listAggregate.get(i).click();
            getWait5().until(ExpectedConditions.elementToBeClickable(workspaceSchema));

            // останавливаю время загрузки данных об агрегате
            long endTime = System.currentTimeMillis();
            LoggerUtil.info(String.format("Время загрузки информации о агрегате =  %s мс", (endTime - getTimeWindowsLoad() - startTime)));

            takeScreenshotWorkspace(nameUnit.getText(), endTime, startTime);
        }
    }

    @Step("Получаю данные для агрегатов на вкладке События")
    public void getAggregateDataEvents() {

        getWait5().until(ExpectedConditions.elementToBeClickable(equipmentList));

        Allure.step("Перебираю все агрегаты на первой странице");
        for (int i = 0; i < listAggregate.size(); i++) {
            // засекаю начало времени загрузки данных об агрегате
            long startTime = System.currentTimeMillis();
            long time = 0L;

            listAggregate.get(i).click();

            try {
                getWait5().until(ExpectedConditions.elementToBeClickable(workspaceEvents));
            } catch (Exception e) {
                time = 20000;
            }

            // останавливаю время загрузки данных об агрегате
            long endTime = System.currentTimeMillis() - time;
            LoggerUtil.info(String.format("Время загрузки информации о агрегате =  %s мс", (endTime - startTime)));

            takeScreenshotWorkspace(nameUnit.getText(), endTime, startTime);
        }
    }

    @Step("Получаю данные для агрегатов на вкладке Журнал")
    public void getAggregateDataMagazine() {

        getWait5().until(ExpectedConditions.elementToBeClickable(equipmentList));

        Allure.step("Перебираю все агрегаты на первой странице");
        for (int i = 0; i < listAggregate.size(); i++) {
            // засекаю начало времени загрузки данных об агрегате
            long startTime = System.currentTimeMillis();
            long time = 0L;

            listAggregate.get(i).click();
            try {
                getWait5().until(ExpectedConditions.elementToBeClickable(workspaceMagazine));
            } catch (Exception e) {
                time = 20000;
            }

            // останавливаю время загрузки данных об агрегате
            long endTime = System.currentTimeMillis() - time;
            LoggerUtil.info(String.format("Время загрузки информации о агрегате =  %s мс", (endTime - startTime)));

            takeScreenshotWorkspace(nameUnit.getText(), endTime, startTime);
        }
    }

    @Step("Перехожу на Журнал")
    public DashboardPage goMagazine() {
        magazine.click();
        getWait5().until(ExpectedConditions.urlContains("tab=2"));

        return this;
    }

    @Step("Перехожу на События")
    public DashboardPage goEvents() {
        events.click();
        getWait5().until(ExpectedConditions.urlContains("tab=3"));

        return this;
    }

    @Step("Прохожусь по оборудованию к станции ТДО")
    public DashboardPage goToTDO() {

        getWait10().until(ExpectedConditions.elementToBeClickable(list1));
        level1s.get(0).click();
        level2s.get(0).click();
        level3s.get(0).click();
        level4s.get(0).click();

        return this;
    }
}
