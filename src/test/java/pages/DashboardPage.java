package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static utils.Data.Dashboard.listParameters;

public class DashboardPage extends BasePage {

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    private long startTimeInterval = 0L;
    private long endTimeInterval = 0L;

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
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li[contains(@class, 'MuiTreeItem')]/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level1Links;
    // Список оборудования название 1 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li[contains(@class, 'MuiTreeItem')]/div/div[@class='MuiTreeItem-label']"))
    private List<WebElement> level1Name;

    // Список оборудования ссылки 2 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level2Links;
    // Список оборудования название 2 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-label']"))
    private List<WebElement> level2Name;

    // Список оборудования ссылки 3 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level3Links;
    // Список оборудования название 3 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/div/div[@class='MuiTreeItem-label']"))
    private List<WebElement> level3Name;

    // Список оборудования ссылки 4 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/ul/div/div/li/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level4Links;
    // Список оборудования название 4 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li/ul[@role='group']/div/div/li/ul[@role='group']/div/div/li/ul/div/div/li/div/div[@class='MuiTreeItem-label']"))
    private List<WebElement> level4Name;

    // окно станции со всеми агрегатами
    @FindBy(xpath = "//div[contains(@class, 'MuiBox-root')]/header/../..")
    private WebElement windowStation;

    // 4 окна на рабочей области
    @FindBy(xpath = "//div[@id='panel1a-content']")
    private List<WebElement> workspaceWindows;

    // список оборудования
    @FindBy(xpath = "//ul[contains(@class, 'MuiTreeView-root')]")
    private WebElement equipmentList;

    // кнопки в окне табличные данные
    // кнопки вкладок в окне табличные данные
    @FindBy(xpath = "//div[contains(@class,'MuiAccordionDetails-root')]//div[@aria-label='journals tabs']")
    private WebElement buttonTab;
    // кнопка события в окне табличные данные
    @FindBy(xpath = "//button/div[contains(text(), 'События')]/..")
    private WebElement eventsTableData;
    // кнопка Мероприятия ТОиР
    @FindBy(xpath = "//button/div[contains(text(), 'Мероприятия ТОиР')]/..")
    private WebElement machineArrangements;
    // кнопка дефекты
    @FindBy(xpath = "//button/div[contains(text(), 'Дефекты')]/..")
    private WebElement defects;
    // кнопка рекомендации
    @FindBy(xpath = "//button/div[contains(text(), 'Рекомендации')]/..")
    private WebElement recommendations;
    // кнопка отчеты
    @FindBy(xpath = "//button/div[contains(text(), 'Отчёты')]/..")
    private WebElement reports;

    // кнопки в окне данные измерений
    // кнопка график
    @FindBy(xpath = "//div[@aria-label='view switcher']/button[contains(@id,'T-0')]")
    private WebElement buttonGraph;
    // кнопка таблица
    @FindBy(xpath = "//div[@aria-label='view switcher']/button[contains(@id,'T-1')]")
    private WebElement buttonTable;

    // таблица в окне данные измерений
    @FindBy(xpath = "//table[@aria-label='analytics table']")
    private WebElement tableDataMeasurement;

    // активная кнопка следующая страница на данные измерений таблица
    @FindBy(xpath = "//*[local-name()='svg'][@data-testid='KeyboardArrowRightIcon']/../span")
    private WebElement tableNext;
    // кнопка следующая страница на данные измерений таблица
    @FindBy(xpath = "//*[local-name()='svg'][@data-testid='KeyboardArrowRightIcon']/..")
    private WebElement tableNextButton;

    // прогресс бар списка оборудования
    @FindBy(xpath = "//div[@id='equipment-content']//span[@role='progressbar']")
    private WebElement progressbar;

    // все выпадающие списки
    @FindBy(xpath = "//div[contains(@class,'MuiFormControl-root')]")
    private List<WebElement> dropdownList;

    // поля в выпадающем списке параметр в окне данные измерений вкладка график (для клика)
    @FindBy(xpath = "//li[contains(@class,'MuiMenuItem-gutters')]")
    private List<WebElement> dropdownDateMeasurement;

    // поля в выпадающем списке параметр в окне данные измерений вкладка график
    @FindBy(xpath = "//li[contains(@class,'MuiMenuItem-gutters')]")
    private List<WebElement> dropdownDateMeasurementValue;

    // выпадающие список параметр в окне данные измерений вкладка график
    @FindBy(xpath = "//ul[@aria-labelledby='select-helper-label']")
    private WebElement dropdownDateMeasurementAll;

    // блок с названиями графиков в окне данные измерений
    @FindBy(xpath = "//div[@id='legend-container-line']/ul")
    private WebElement blockNameGraph;

    // название графика в окне данные измерений
    @FindBy(xpath = "//li/span[contains(@style,'Roboto')]")
    private List<WebElement> nameGraph;

    // названия колонок в таблице в окне данные измерений
    @FindBy(xpath = "//thead/tr[2]/th")
    private List<WebElement> columnTitle;

    // картинка в окне схема агрегата
    @FindBy(xpath = "//img")
    private WebElement image;

    // картинка в окне состояние и прогнозирование
    @FindBy(xpath = "//div[contains(@class,'MuiBox-root')]/p[contains(@class,'MuiTypography-root')]/../div[contains(@class,'MuiBox-root')]")
    private WebElement speedometer;

    // стрелка в окне состояние и прогнозирование
    @FindBy(xpath = "//*[@id='needle']")
    private WebElement arrow;

    // таблица в окне табличные данные
    @FindBy(xpath = "//div[contains(@class,'MuiDataGrid-main')]")
    private WebElement table;

    // таблица Мероприятия ТОиР в окне табличные данные
    @FindBy(xpath = "//table[@aria-label='Machine Arrangements table']")
    private WebElement tableMachineArrangements;

    // таблица дефекты в окне табличные данные
    @FindBy(xpath = "//table[@aria-label='Faults table']")
    private WebElement tableDefects;

    // таблица рекомендации в окне табличные данные
    @FindBy(xpath = "//table[@aria-label='recommendations table']")
    private WebElement tableRecommendations;

    // таблица отчеты в окне табличные данные
    @FindBy(xpath = "//table[@aria-label='reports table']")
    private WebElement tableReports;

    // график в окне данные измерений
    @FindBy(xpath = "//div[@id='panel1a-content']//canvas")
    private WebElement graph;

    // шапка каждого окна в рабочей области
    @FindBy(xpath = "//div[contains(@class,'MuiAccordionSummary-content')]/p")
    private List<WebElement> cap;

    // окно состояние и прогнозирование кнопка вертикальная шкала
    @FindBy(xpath = "//button[contains(@class,'MuiToggleButtonGroup-groupedHorizontal')]/div[contains(@aria-label,'Вертикальная шкала')]")
    private WebElement scale;

    // окно состояние и прогнозирование вертикальная шкала
    @FindBy(xpath = "//div[contains(@class,'MuiBox-root')]/*[local-name()='svg']/*[local-name()='rect']/..")
    private WebElement scaleImage;

    @Step("Открываю выпадающий список временных интервалов")
    public DashboardPage openTimeIntervalDropdown() {
        getWait10().until(ExpectedConditions.elementToBeClickable(selectedInterval)).click();
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(listBox));

        return this;
    }

    @Step("Получаю выбранный временной интервал (текст)")
    public String timeIntervalSelected() {
        String text = getWait10().until(ExpectedConditions.visibilityOfElementLocated(selectedInterval)).getText();

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

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        timeIntervalField.click();
        getWait5().until(ExpectedConditions.elementToBeClickable(menuIntervalField));
        getWait5().until(ExpectedConditions.elementToBeClickable(getSelectedRange)).click();

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

//        getWait10().until(ExpectedConditions.invisibilityOf(progressbar));
        getWait10().until(ExpectedConditions.visibilityOf(windowStation));
        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Выбор интервала", page);

        return this;
    }

    @Step("{message}")
    public DashboardPage takeScreenshotPage(String message, WebElement element) {

        byte[] screen = null;
        try {
            screen = element.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(String.format("%s -> %s", message, String.format("%.1f", (double) (endTimeInterval - startTimeInterval) / 1000)), new ByteArrayInputStream(screen));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    @Step("Прохожусь по оборудованию к агрегату {st1}/{st2}/{st3}/{st4}/{agr}")
    public DashboardPage goTo(String st1, String st2, String st3, String st4, String agr) {

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        Allure.step(String.format("1 уровень %s", st1));
        for (int i = 0; i < level1Links.size(); i++) {
            if (level1Name.get(i).getText().equals(st1)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", level1Name.get(i));
                level1Name.get(i).click();
                break;
            }
        }
        Allure.step(String.format("2 уровень %s", st2));
        getStations(level2Links, level2Name, st2);
        Allure.step(String.format("3 уровень %s", st3));
        getStations(level3Links, level3Name, st3);
        Allure.step(String.format("4 уровень %s", st4));
        getStations(level4Links, level4Name, st4);
        Allure.step(String.format("Агрегат %s", agr));
        getMeasurementDataGraph(agr);

        getWait10().until(ExpectedConditions.elementToBeClickable(graph));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по агрегату", page);

        return this;
    }

    // проходим по дереву оборудования
    private void getStations(List<WebElement> link, List<WebElement> name, String station) {
        for (int i = 0; i < link.size(); i++) {
            getWait5().until(ExpectedConditions.elementToBeClickable(link.get(i)));
            if (name.get(i).getText().equals(station)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link.get(i));
                link.get(i).click();
                break;
            }
        }
    }

    // перехожу к агрегату
    public DashboardPage getMeasurementDataGraph(String unitName) {

        getWait5().until(ExpectedConditions.elementToBeClickable(equipmentList));

        for (WebElement aggregate : listAggregate) {
            if (aggregate.getText().equals(unitName)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", aggregate);
                aggregate.click();
            }
        }

        return this;
    }

    @Step("Получаю название параметров графика")
    public List<String> getNameGraph() {

        List<String> listNameGraph = new ArrayList<>();

        for (int i = 0; i < nameGraph.size(); i++) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nameGraph.get(i));
            listNameGraph.add(i, getWait5().until(ExpectedConditions.visibilityOf(nameGraph.get(i))).getText());
        }

        return listNameGraph;
    }

    @Step("Проверяю, что загрузилась картинка в окне Схема агрегата")
    public boolean checkUnitSchematicWindow() {
        try {
            getWait5().until(ExpectedConditions.visibilityOf(image));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверяю, что загрузилась стрелка в окне Состояние и Прогнозирование")
    public boolean checkStatusAndForecastWindow() {
        try {
            getWait5().until(ExpectedConditions.visibilityOf(arrow));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверяю, что загрузилась таблица в окне Табличные данные")
    public boolean checkTableDataWindow() {
        try {
            getWait5().until(ExpectedConditions.visibilityOf(table));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверяю, что загрузилась таблица в окне Данные измерений")
    public boolean checkMeasurementDataWindow() {
        try {
            getWait5().until(ExpectedConditions.visibilityOf(graph));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Сворачиваю окна на Рабочей области")
    public DashboardPage collapseWindows(int first, int second, int third) {

        getWait5().until(ExpectedConditions.elementToBeClickable(cap.get(first - 1))).click();
        getWait5().until(ExpectedConditions.elementToBeClickable(cap.get(second - 1))).click();
        getWait5().until(ExpectedConditions.elementToBeClickable(cap.get(third - 1))).click();

        return this;
    }

    @Step("Получаю размер картинки")
    public Dimension getSizeImage() {
        return image.getSize();
    }

    @Step("Получаю размер таблицы")
    public Dimension getSizeTable() {
        return table.getSize();
    }

    @Step("Получаю размер графика")
    public Dimension getSizeGraph() {
        return graph.getSize();
    }

    @Step("Выбираю параметр Оборотные")
    public DashboardPage selectParameterTurnover() {
        listParameters = new ArrayList<>(1);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        listParameters.add(0, dropdownDateMeasurement.get(0).getText());

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметру", page);

        return this;
    }

    @Step("Выбираю параметр Оборотные вид Таблица")
    public DashboardPage selectParameterTurnoverTable() {
        listParameters = new ArrayList<>(1);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        listParameters.add(0, dropdownDateMeasurement.get(0).getText());

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметру", page);

        return this;
    }

    @Step("Выбираю параметр Параметры вид Таблица")
    public DashboardPage selectParameterParametersTable() {
        listParameters = new ArrayList<>(1);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();
        listParameters.add(0, dropdownDateMeasurement.get(6).getText());
        dropdownDateMeasurement.get(6).click();

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметру", page);

        return this;
    }

    @Step("Выбираю параметр Замеры вид Таблица")
    public DashboardPage selectParameterMeasurementsTable() {
        listParameters = new ArrayList<>(2);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();
        listParameters.add(0, dropdownDateMeasurement.get(6).getText());
        dropdownDateMeasurement.get(6).click();
        dropdownList.get(6).click();
        listParameters.add(1, dropdownDateMeasurement.get(7).getText());
        dropdownDateMeasurement.get(7).click();

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметру", page);

        return this;
    }

    @Step("Выбираю параметр Параметры и оставляю по умолчанию")
    public DashboardPage selectParameterTurnoverParameters() {
        listParameters = new ArrayList<>(2);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listParameters.add(0, dropdownDateMeasurement.get(0).getText());
        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(5))).click();
        listParameters.add(1, dropdownDateMeasurement.get(5).getText());

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметру", page);

        return this;
    }

    @Step("Выбираю параметр Параметры и убираю по умолчанию")
    public DashboardPage selectParameterParameters() {
        listParameters = new ArrayList<>(1);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        dropdownDateMeasurement.get(0).click();
        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(5))).click();
        listParameters.add(0, dropdownDateMeasurement.get(5).getText());

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметру", page);

        return this;
    }

    @Step("Выбираю {count} однотипных параметров Замерные на График")
    public DashboardPage selectParameterMeasurementsSameTypeGraph(int count) {
        listParameters = new ArrayList<>(count);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(0))).click();

        for (int i = 6; i < 6 + count; i++) {
            dropdownDateMeasurement.get(i).click();
            listParameters.add(i - count - 1, dropdownDateMeasurement.get(i).getText());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getWait10().until(ExpectedConditions.elementToBeClickable(graph));
        }

        takeScreenshotPage("Выбранные параметры", dropdownDateMeasurementAll);

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметрам", page);

        return this;
    }

    @Step("Выбираю один параметр из Замеры на Таблица")
    public DashboardPage selectParameterMeasurementsSameTypeTable(int count) {
        listParameters = new ArrayList<>(count);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i = 6; i < 6 + count; i++) {
            listParameters.add(i - 6, dropdownDateMeasurement.get(i).getText());
            dropdownDateMeasurement.get(i).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getWait10().until(ExpectedConditions.elementToBeClickable(tableDataMeasurement));
        }

        takeScreenshotPage("Выбранные параметры", tableDataMeasurement);

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметрам", page);

        return this;
    }

    @Step("Выбираю один параметр с точками из Замеры на Таблица")
    public DashboardPage selectParameterMeasurementsWithPointsTable() {

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        dropdownDateMeasurement.get(29).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getWait10().until(ExpectedConditions.elementToBeClickable(tableDataMeasurement));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Таблица", page);

        return this;
    }

    @Step("Выбираю {count} не однотипных параметров Замерные")
    public DashboardPage selectParameterMeasurementsNotSameType(int count) {
        listParameters = new ArrayList<>(count);

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(0))).click();
        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(6))).click();
        listParameters.add(0, dropdownDateMeasurement.get(6).getText());
        takeScreenshotPage("Выбранный параметр", dropdownDateMeasurementAll);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getWait10().until(ExpectedConditions.elementToBeClickable(graph));

        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(14))).click();
        listParameters.add(1, dropdownDateMeasurement.get(14).getText());
        takeScreenshotPage("Выбранный параметр", dropdownDateMeasurementAll);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getWait10().until(ExpectedConditions.elementToBeClickable(graph));

        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(21))).click();
        listParameters.add(2, dropdownDateMeasurement.get(21).getText());
        takeScreenshotPage("Выбранный параметр", dropdownDateMeasurementAll);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getWait10().until(ExpectedConditions.elementToBeClickable(graph));

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметрам", page);

        return this;
    }

    //выбор интервала
    public DashboardPage selectIntervalByDataValue(String dataValue) {

        openTimeIntervalDropdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        By optionLocator = By.xpath("//ul[@role='listbox']/li[@data-value='" + dataValue + "']");
        getWait10().until(ExpectedConditions.elementToBeClickable(optionLocator)).click();

        // жду обновления дашборд на вкладке Схема
        getWait10().until(ExpectedConditions.urlContains("tab=1"));

        return this;
    }

    //выбранный текст
    public String getSelectedIntervalText() {
        return timeIntervalSelected();
    }

    @Step("Кликаю вкладку Дефекты в окне Табличные данные")
    public DashboardPage clickTabDefects() {

        getWait5().until(ExpectedConditions.elementToBeClickable(buttonTab));
        defects.click();
        getWait5().until(ExpectedConditions.visibilityOf(tableDefects));

        return this;
    }

    @Step("Получаю размер таблицы Дефекты")
    public Dimension getSizeTableDefects() {
        return tableDefects.getSize();
    }

    @Step("Кликаю вкладку Рекомендации в окне Табличные данные")
    public DashboardPage clickTabRecommendations() {

        getWait5().until(ExpectedConditions.elementToBeClickable(buttonTab));
        recommendations.click();
        getWait5().until(ExpectedConditions.visibilityOf(tableRecommendations));

        return this;
    }

    @Step("Получаю размер таблицы Дефекты")
    public Dimension getSizeTableRecommendations() {
        return tableRecommendations.getSize();
    }

    @Step("Кликаю вкладку Отчеты в окне Табличные данные")
    public DashboardPage clickTabReports() {

        getWait5().until(ExpectedConditions.elementToBeClickable(buttonTab));
        reports.click();
        getWait5().until(ExpectedConditions.visibilityOf(tableReports));

        return this;
    }

    @Step("Получаю размер таблицы Отчеты")
    public Dimension getSizeTableReports() {
        return tableReports.getSize();
    }

    @Step("Кликаю вкладку Мероприятия ТОиР в окне Табличные данные")
    public DashboardPage clickTabMachineArrangements() {

        getWait5().until(ExpectedConditions.elementToBeClickable(buttonTab));
        machineArrangements.click();
        getWait5().until(ExpectedConditions.visibilityOf(tableMachineArrangements));

        return this;
    }

    @Step("Получаю размер таблицы Мероприятия ТОиР")
    public Dimension getSizeTableMachineArrangements() {
        return tableMachineArrangements.getSize();
    }

    @Step("Кликаю вкладку События в окне Табличные данные")
    public DashboardPage clickTabEvents() {

        getWait5().until(ExpectedConditions.elementToBeClickable(buttonTab));
        eventsTableData.click();
        getWait5().until(ExpectedConditions.visibilityOf(table));

        return this;
    }

    @Step("Получаю размер картинки Спидометр")
    public Dimension getSizeSpeedometer() {
        return speedometer.getSize();
    }

    @Step("Кликаю кнопку вид Вертикальная шкала")
    public DashboardPage clickViewScale() {
        scale.click();
        getWait5().until(ExpectedConditions.elementToBeClickable(scaleImage));

        return this;
    }

    @Step("Получаю размер картинки Вертикальная шкала")
    public Dimension getSizeScale() {
        return scaleImage.getSize();
    }

    @Step("Кликаю кнопку Таблица в окне Данные измерений")
    public DashboardPage clickButtonTable() {

        buttonTable.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getWait5().until(ExpectedConditions.elementToBeClickable(tableDataMeasurement));

        takeScreenshotPage("Таблица", tableDataMeasurement);

        return this;
    }

    @Step("Кликаю кнопку График в окне Данные измерений")
    public DashboardPage clickButtonGraph() {

        buttonGraph.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getWait5().until(ExpectedConditions.elementToBeClickable(graph));

        takeScreenshotPage("График", page);

        return this;
    }

    @Step("Получаю названия колонок")
    public List<String> getColumnTitle() {

        List<String> listName = new ArrayList<>();
        int x = 0;

        if (isElementPresentWithWait(By.xpath("//*[local-name()='svg'][@data-testid='KeyboardArrowRightIcon']/../span"), 1)) {
            try {
                do {
                    for (int i = 0; i < columnTitle.size(); i++) {
                        listName.add(i + x, columnTitle.get(i).getText());
                    }
                    tableNextButton.click();
                    x += 3;
                } while (tableNext.isDisplayed());
            } catch (Exception e) {
            }
        }

        for (int i = 0; i < columnTitle.size(); i++) {
            listName.add(i + x, columnTitle.get(i).getText());
        }

        return listName;
    }

    public boolean isElementPresentWithWait(By locator, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
