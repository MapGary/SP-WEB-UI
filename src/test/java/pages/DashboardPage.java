package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    // кнопка фильров в меню оборудование
    private final By equipmentFilterBtn = By.cssSelector("button svg[data-testid='TuneIcon']");
    // окно список фильтров
    private final By dialogContainer = By.cssSelector("div[role='dialog']");
    // спинер в окне список фильтров
    private final By loadingSpinner = By.cssSelector("svg.MuiCircularProgress-svg");
    // поле путь в окне список фильтров
    private final By pathInput = By.cssSelector("input[name='path']");
    // выпадающий список поля в окне список фильтров
//    private final By pathSelector = By.xpath("//li[contains(@class,'MuiAutocomplete-option') and not(contains(@class,'MuiListSubheader-root'))]");
    @FindBy(xpath = "//li[contains(@class,'MuiAutocomplete-option') and not(contains(@class,'MuiListSubheader-root'))]")
    private List<WebElement> dropdownListWindowFilterList;
    // первый результат в выпадающем списке поля путь
    private final By firstResult = By.xpath("//li[@data-option-index='1']");
    // кнопка ОК в окне список фильтров
    private final By okButton = By.xpath("//button[normalize-space() = 'Ок']");
    private final By autocompleteListItems = By.xpath("//ul[@role='listbox']/li");

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

    // лист агрегатов на рабочей области для станции модуль схема
    @FindBy(xpath = "//div[@aria-label='Station scheme view']/../../div[contains(@class,'MuiBox')]/div/div")
    private List<WebElement> listUnitsDashboard;

    // Вся страница
    @FindBy(xpath = "//div[contains(@class, 'MuiBox-root')]/header/../..")
    private WebElement page;

    // стрелка первая страница с агрегатами на дашборде для станции
    @FindBy(xpath = "//button[@aria-label='first page']")
    private WebElement firstPage;
    // стрелка следующая страница с агрегатами на дашборде для станции
    @FindBy(xpath = "//button[@aria-label='next page']")
    private WebElement nextPage;

    // фильтр оборудования Всё оборудование (красный, жёлтый, зелёный)
    private final By buttonFirst = By.xpath("(//div[contains(@class,'MuiToolbar-root')]//button)[3]");
    // Оборудование со статусом предупреждение или авария (красный, жёлтый)
    private final By buttonSecond = By.xpath("(//div[contains(@class,'MuiToolbar-root')]//button)[4]");
    // Остальное оборудование (синий, фиолетовый)
    private final By buttonThird = By.xpath("(//div[contains(@class,'MuiToolbar-root')]//button)[5]");

    // Список оборудования ссылки 1 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li[contains(@class, 'MuiTreeItem')]/div/div[@class='MuiTreeItem-iconContainer']"))
    private List<WebElement> level1Links;
    // Список оборудования название 1 уровень
    @FindBys(@FindBy(xpath = "//ul[@role='tree']/li[contains(@class, 'MuiTreeItem')]/div/div[@class='MuiTreeItem-label']"))
    private List<WebElement> level1Name;
    // Верхняя станция токрытая по дефолту
    @FindBy(xpath = "//li[contains(@class,'MuiTreeItem')]")
    private List<WebElement> stationLinks;

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

    // кнопка модуль журнал
    @FindBy(xpath = "//button[contains(@id,'T-2')]")
    private WebElement buttonModuleMagazine;
    // кнопка модуль события
    @FindBy(xpath = "//button[contains(@id,'T-3')]")
    private WebElement buttonModuleEvents;
    // кнопка модуль аналитика
    @FindBy(xpath = "//button[contains(@id,'T-4')]")
    private WebElement buttonModuleAnalytics;

    // нижняя часть таблицы модуль журнал
    @FindBy(xpath = "//div[contains(@class,'MuiDataGrid')][@role='grid']/div/div[contains(@class,'MuiBox')]")
    private WebElement footerTableModuleMagazine;
    // столбец с точками таблица модуль журнал
    @FindBy(xpath = "//div[contains(@class,'pinnedColumns')]")
    private WebElement columnTableModuleMagazine;
    // столбец с замерами таблица модуль журнал
    @FindBy(xpath = "//div[contains(@class,'pinnedColumns--right')]/div[@role='row']")
    private List<WebElement> countMeteringTableModuleMagazine;

    // таблица модуль события
    @FindBy(xpath = "//table[contains(@class,'MuiTable')]")
    private WebElement tableEventsModule;
    // хедер таблицы модуль события
    @FindBy(xpath = "//div[contains(@id,'P-3')]//div[contains(@class,'columnHeaders ')]")
    private WebElement headerTableEventsModule;
    // поле шаблон модуль аналитика
    @FindBy(xpath = "//div[contains(@class,'MuiAutocomplete')]//label[contains(@id,'label')]/..")
    private WebElement fieldSampleAnalyticsModule;
    // поле вид отображения модуль аналитика
    @FindBy(xpath = "//div[@id='viewStyle-select']")
    private WebElement fieldViewStyle;
    // блок с диаграммами модуль аналитика
    @FindBy(xpath = "//h3/..")
    private WebElement blockDiagrams;
    // блок с таблицами модуль аналитика
    @FindBy(xpath = "//table")
    private WebElement blockTables;

    // поле для ввода данных шаблона модуль аналитика
    @FindBy(xpath = "//input[@name='templateId']")
    private WebElement fieldValueSampleAnalyticsModule;
    // выпадающий список шаблонов модуль аналитика
    @FindBy(xpath = "//div[@role='presentation']//li")
    private List<WebElement> listSample;
    // кнопка применить модуль аналитика
    @FindBy(xpath = "//button[contains(@class,'MuiButton-contained')]")
    private WebElement buttonApplyAnalyticsModule;

    // иконка агрегатов для станции
//    @FindBy(xpath = "//div[@aria-label='Station scheme view']/../../div[contains(@class,'MuiBox')]//*[local-name()='svg'][contains(@class,'fontSizeMedium')]")
//    private List<WebElement> iconUnit;
    @FindBy(xpath = "//div[@aria-label='Station scheme view']/../../div[contains(@class,'MuiBox')]")
    private WebElement fieldUnit;

    // иконки на картинке всех агрегатов для станции
//    @FindBy(xpath = "//div[@aria-label='Station scheme view']/../../div[contains(@class,'MuiBox')]/*[local-name()='svg']/*[local-name()='g']")
    @FindBy(xpath = "//div[@aria-label='Station scheme view']/../../div[contains(@class,'MuiBox')]/*[local-name()='svg']")
    private List<WebElement> iconImageUnits;

    // кнопка диаграмма для станции
    @FindBy(xpath = "//button[contains(@value,'diagram')]")
    private WebElement buttonDiagram;
    // кнопка схема для станции
    @FindBy(xpath = "//button[contains(@value,'default')]")
    private WebElement buttonGraphStation;

    // все диаграммы для станции
    @FindBy(xpath = "//div[contains(@class,'MuiBox')]/canvas")
    private List<WebElement> diagram;

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

    // выпадающее меню тип измерения
    @FindBy(xpath = "//ul[contains(@id,'rgi')]")
    private WebElement dropdownMenuMeasurementType;
    // выпадающий список тип измерения
    @FindBy(xpath = "//ul[contains(@class,'MuiMenu')]/li")
    private List<WebElement> dropdownMeasurementType;

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
    private WebElement progressbarMenu;
    // прогресс бар дашборд
    @FindBy(xpath = "//div[contains(@id,'P-1')]//span[@role='progressbar']")
    private WebElement progressbarDashboard;
    // прогресс бар окно данные измерений
    @FindBy(xpath = "//div[@id='panel1a-content']//span[@role='progressbar']")
    private WebElement progressbarMeasurementDate;

    // все выпадающие списки
    @FindBy(xpath = "//div[contains(@class,'MuiFormControl-root')]")
    private List<WebElement> dropdownList;

    // поля в выпадающем списке параметр в окне данные измерений вкладка график (для клика)
    @FindBy(xpath = "//li[contains(@class,'MuiMenuItem-gutters')]")
    private List<WebElement> dropdownDateMeasurement;

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

    // картинка нет данных в окне данные измерений
    @FindBy(xpath = "//div[contains(@class,'MuiBox')]/*[local-name()='svg']/*[local-name()='svg'][@data-testid='AnalyticsIcon']")
    private WebElement iconNotData;

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

        isElementPresentWithWait(By.xpath("//div[@aria-label='Station scheme view']/../../div[contains(@class,'MuiBox')]"), 1000);
//        getWait10().until(ExpectedConditions.invisibilityOf(progressbarMenu));
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

        getWait10().until(ExpectedConditions.visibilityOf(graph));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по агрегату", page);

        return this;
    }

    @Step("Прохожусь по оборудованию к агрегату {st1}/{st2}/{st3}/{st4}/{agr}")
    public DashboardPage goToMagazine(String st1, String st2, String st3, String st4, String agr) {

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        Allure.step(String.format("4 уровень %s", st4));
        getStations(level4Links, level4Name, st4);
        Allure.step(String.format("Агрегат %s", agr));
        getMeasurementDataGraph(agr);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getWait10().until(ExpectedConditions.visibilityOf(footerTableModuleMagazine));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по агрегату", page);

        return this;
    }

    @Step("Прохожусь по оборудованию к агрегату {st1}/{st2}/{st3}/{st4}/{agr}")
    public DashboardPage goToEvents(String st1, String st2, String st3, String st4, String agr) {

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        Allure.step(String.format("4 уровень %s", st4));
        getStations(level4Links, level4Name, st4);
        Allure.step(String.format("Агрегат %s", agr));
        getMeasurementDataGraph(agr);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getWait10().until(ExpectedConditions.visibilityOf(headerTableEventsModule));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по агрегату", page);

        return this;
    }

    @Step("Перехожу к станции {station}")
    public DashboardPage goTo(String station) {

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        Allure.step(String.format("1 уровень %s", station));
        for (int i = 0; i < level1Links.size(); i++) {
            if (level1Name.get(i).getText().equals(station)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", level1Name.get(i));
                level1Name.get(i).click();
                break;
            }
        }

        getWait10().until(ExpectedConditions.elementToBeClickable(fieldUnit));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по станции", page);

        return this;
    }

    @Step("Прохожусь по оборудованию к станции {st1}/{st2}/{st3}/{st4}")
    public DashboardPage goTo(String st1, String st2, String st3, String st4) {

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
//        private void getStations(List<WebElement> link, List<WebElement> name, String station) {
        for (int i = 0; i < level4Name.size(); i++) {
            getWait5().until(ExpectedConditions.elementToBeClickable(level4Links.get(i)));
            if (level4Name.get(i).getText().equals(st4)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", level4Name.get(i));
                level4Name.get(i).click();
                break;
            }
        }
//        }

        getWait10().until(ExpectedConditions.visibilityOf(fieldUnit));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по станции", page);

        return this;
    }

    @Step("Перехожу к станции {station}")
    public DashboardPage goToImage(String station) {

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        Allure.step(String.format("1 уровень %s", station));
        for (int i = 0; i < level1Links.size(); i++) {
            if (level1Name.get(i).getText().equals(station)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", level1Name.get(i));
                level1Name.get(i).click();
                break;
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getWait10().until(ExpectedConditions.invisibilityOf(progressbarDashboard));

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

    @Step("Проверяю, что загрузился график в окне Данные измерений")
    public boolean checkMeasurementDataWindow() {
        try {
            getWait5().until(ExpectedConditions.visibilityOf(graph));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Сворачиваю/разворачиваю окна на Рабочей области")
    public DashboardPage collapseWindows(String[] elements) {

        for (String el : elements) {
            for (WebElement element : cap) {
                if (element.getText().equals(el)) {
                    element.click();
                }
            }
        }

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

    public DashboardPage selectParameterMeasurementType(String parameter) {
        listParameters = new ArrayList<>();

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(0)));

        for (int i = 0; i < dropdownDateMeasurement.size(); i++) {
            if (i > 0) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownDateMeasurement.get(i - 1));
            }

            if (dropdownDateMeasurement.get(i).getText().equals(parameter)) {
                dropdownDateMeasurement.get(i).click();
                break;
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        getWait10().until(ExpectedConditions.elementToBeClickable(graph));
//
////        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
//
//        // останавливаю время загрузки временного интервала
//        endTimeInterval = System.currentTimeMillis();
//
//        takeScreenshotPage("Данные по параметрам", page);

        return this;
    }

    @Step("Выбираю параметр {parameter} на Таблица")
    public DashboardPage selectParameterTable(String parameter) {
        selectParameterMeasurementType(parameter);

        getWait10().until(ExpectedConditions.elementToBeClickable(tableDataMeasurement));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметрам", tableDataMeasurement);

        return this;
    }

    @Step("Выбираю параметр {parameter} на График")
    public DashboardPage selectParameterGraph(String parameter) {
        selectParameterMeasurementType(parameter);

        getWait10().until(ExpectedConditions.elementToBeClickable(graph));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметрам", graph);

        return this;
    }

    @Step("Выбираю параметры {parameters}")
    public DashboardPage selectParametersMeasurementType(String[] parameters) {
        listParameters = new ArrayList<>();
        int x = 0;

        // засекаю время загрузки временного интервала
        startTimeInterval = System.currentTimeMillis();

        dropdownList.get(6).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // снимаю чек боксы
        getWait10().until(ExpectedConditions.elementToBeClickable(dropdownDateMeasurement.get(0)));

        for (int i = 0; i < dropdownDateMeasurement.size(); i++) {
            if (i != 0) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownDateMeasurement.get(i - 1));
            }
            WebElement element = dropdownDateMeasurement.get(i);

            if (element.getAttribute("aria-selected").equals("true")) {
                element.click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
        dropdownList.get(6).click();

        // ставлю чек боксы
        for (String parameter : parameters) {
            for (int j = 0; j < dropdownDateMeasurement.size(); j++) {
                if (j != 0) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownDateMeasurement.get(j - 1));
                }

                WebElement element = dropdownDateMeasurement.get(j);

                if (element.getText().equals(parameter)) {
                    listParameters.add(x, element.getText());
                    x++;
                    element.click();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    getWait10().until(ExpectedConditions.elementToBeClickable(graph));
                    break;
                }
            }
        }

        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
        getWait10().until(ExpectedConditions.elementToBeClickable(graph));

        // останавливаю время загрузки временного интервала
        endTimeInterval = System.currentTimeMillis();

        takeScreenshotPage("Данные по параметрам", graph);

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

    @Step("Получаю размер таблицы Рекомендации")
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

    private DashboardPage clickMeasurementType(String type) {

        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownList.get(3))).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getWait5().until(ExpectedConditions.elementToBeClickable(dropdownMeasurementType.get(0)));

        for (WebElement dropdown : dropdownMeasurementType) {
            if (dropdown.getText().equals(type)) {
                dropdown.click();
                break;
            }
        }

        return this;
    }

    @Step("Открываю выпадающий список Тип измерения выбираю {type}")
    public DashboardPage clickMeasurementTypeIcon(String type) {

        clickMeasurementType(type);

        getWait10().until(ExpectedConditions.visibilityOf(iconNotData));

        return this;
    }

    @Step("Открываю выпадающий список Тип измерения выбираю {type}")
    public DashboardPage clickMeasurementTypeGraph(String type) {

        clickMeasurementType(type);

        try {
            getWait5().until(ExpectedConditions.invisibilityOf(progressbarMeasurementDate));
        } catch (Exception e) {
            getWait10().until(ExpectedConditions.visibilityOf(graph));
        }

        return this;
    }

    @Step("Открываю выпадающий список Тип измерения выбираю {type}")
    public DashboardPage clickMeasurementTypeNull(String type) {

        clickMeasurementType(type);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    @Step("Кликаю кнопку Диаграмма")
    public DashboardPage clickChartButton() {
        buttonDiagram.click();

        return this;
    }

    @Step("Проверяю, что загрузились диаграммы для станции")
    public boolean checkChartStation() {
        try {
            getWait5().until(ExpectedConditions.visibilityOf(diagram.get(0)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Кликаю кнопку Диаграмма")
    public DashboardPage clickSchemaButton() {
        buttonGraphStation.click();

        return this;
    }

    @Step("Проверяю, что загрузились иконки агрегатов для станции")
    public boolean checkSchemaStation() {
        try {
            getWait10().until(ExpectedConditions.invisibilityOf(progressbarDashboard));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Перешел в модуль Журнал")
    public DashboardPage clickMagazineModul() {
        buttonModuleMagazine.click();
        getWait10().until(ExpectedConditions.visibilityOf(footerTableModuleMagazine));

        takeScreenshotPage("Дашборд Журнал", page);

        return this;
    }

    @Step("Получил названия колонок в таблице")
    public boolean getNameColumnTableModuleMagazine() {

        if (columnTableModuleMagazine.getText().equals("")) {
            return false;
        }
        return true;
    }

    @Step("Получил количество замеров в таблице")
    public int getCountMetering() {

        int summa = 0;

        for (int i = 1; i < countMeteringTableModuleMagazine.size(); i++) {
            summa += Integer.parseInt(countMeteringTableModuleMagazine.get(i).getText());
        }

        return summa;
    }

    @Step("Перешел в модуль События")
    public DashboardPage clickEventsModul() {
        buttonModuleEvents.click();
        getWait10().until(ExpectedConditions.visibilityOf(tableEventsModule));

        takeScreenshotPage("Дашборд События", page);

        return this;
    }

    @Step("Проверяю, что в таблице построена")
    public boolean checkStationTableEvents() {
        try {
            getWait5().until(ExpectedConditions.visibilityOf(tableEventsModule));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверяю, что в таблице построена")
    public boolean checkUnitTableEvents() {
        try {
            getWait5().until(ExpectedConditions.visibilityOf(headerTableEventsModule));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Перешел в модуль Аналитика")
    public DashboardPage clickAnalyticsModule() {
        buttonModuleAnalytics.click();
        getWait10().until(ExpectedConditions.visibilityOf(fieldSampleAnalyticsModule));

        takeScreenshotPage("Дашборд Аналитика", page);

        return this;
    }

    @Step("Выбрал шаблон {value}")
    public DashboardPage selectSampleAnalyticsModule(String value) {
        getWait5().until(ExpectedConditions.elementToBeClickable(fieldSampleAnalyticsModule)).click();
        getWait5().until(ExpectedConditions.visibilityOf(listSample.get(0)));

        for (WebElement element : listSample) {
            if (element.getText().equals(value)) {
                element.click();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                break;
            }
        }

        getWait10().until(ExpectedConditions.visibilityOf(fieldViewStyle));

        return this;
    }

    @Step("Нажал кнопку Применить")
    public DashboardPage clickButtonApply() {
        getWait5().until(ExpectedConditions.elementToBeClickable(buttonApplyAnalyticsModule)).click();

        return this;
    }

    @Step("Проверяю, что построены диаграммы")
    public boolean checkDiagramAnalyticsModul() {
        try {
            getWait10().until(ExpectedConditions.visibilityOf(blockDiagrams));
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            takeScreenshotPage("Дашборд Аналитика", page);
        }
    }

    @Step("Выбрал Вид отображения таблица")
    public DashboardPage selectViewStyleAnalyticsModul(String value) {
        fieldViewStyle.click();

        for (WebElement element : listSample) {
            if (element.getText().equals(value)) {
                element.click();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                break;
            }
        }

        return this;
    }

    @Step("Проверяю, что построены таблицы")
    public boolean checkTableAnalyticsModul() {
        try {
            getWait10().until(ExpectedConditions.visibilityOf(blockTables));
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            takeScreenshotPage("Дашборд Аналитика", page);
        }
    }

    @Step("Получаю количество всех агрегатов")
    public int getCountUnits() {

        int count = 0;

        try {
            do {
                count += listUnitsDashboard.size();
                nextPage.click();
            } while (nextPage.isDisplayed());
        } catch (Exception e) {
        }

        return count;
    }

    @Step("Нажимаю на кнопку фильтрации 'Всё оборудование' (красный-жёлтый-зелёный)")
    public DashboardPage clickFirstButton() {
        getWait5().until(ExpectedConditions.elementToBeClickable(buttonFirst)).click();

        if (firstPage.getAttribute("tabindex").equals("0")) {
            firstPage.click();
        }

        return this;
    }

    @Step("Нажимаю на кнопку фильтрации 'Оборудование со статусом 'Предупреждение' и 'Авария''(красный-жёлтый)")
    public DashboardPage clickSecondButton() {
        getWait5().until(ExpectedConditions.elementToBeClickable(buttonSecond)).click();

        if (firstPage.getAttribute("tabindex").equals("0")) {
            firstPage.click();
        }

        return this;
    }

    @Step("Нажимаю на кнопку фильтрации ''(синий-фиолетовый)")
    public DashboardPage clickThirdButton() {
        getWait5().until(ExpectedConditions.elementToBeClickable(buttonThird)).click();

        if (firstPage.getAttribute("tabindex").equals("0")) {
            firstPage.click();
        }

        return this;
    }

    @Step("Открываю фильтр оборудования и ожидаю загрузку поля 'Путь'")
    public DashboardPage openEquipmentFilterAndWait() {
        getWait5().until(ExpectedConditions.elementToBeClickable(equipmentFilterBtn)).click();
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(dialogContainer));
        // жду исчезновение спиннера
        getWait10().until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        // жду доступности поля Путь
        getWait10().until(ExpectedConditions.elementToBeClickable(pathInput));
        Allure.step("Диалог открыт, поле 'Путь' доступно");
        return this;
    }

    @Step("Ввожу в поле 'Путь': {part}")
    public DashboardPage setPath(String part) {

        if (!part.equals("")) {
            WebElement el = getWait5().until(ExpectedConditions.elementToBeClickable(pathInput));
            el.clear();
            el.sendKeys(part);

            try {
                WebElement firstOption = getWait5().until(ExpectedConditions.elementToBeClickable(dropdownListWindowFilterList.get(0)));
                dropdownListWindowFilterList.get(0).click();
//            firstOption.click();
//            WebElement option = getWait5().until(ExpectedConditions.elementToBeClickable(firstResult));
//            option.click();
            } catch (TimeoutException e) {
                Allure.step("Выпадающий список не появился для: " + part);
            }

            getWait5().until(d -> {
                try {
                    String v = getWait5().until(ExpectedConditions.elementToBeClickable(pathInput)).getAttribute("value");
                    return v != null && (v.equals(part) || v.contains(part));
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            });
        }

        Allure.step("В поле 'Путь' введено и выбран вариант: " + part);

        return this;
    }

    @Step("Заполняю все поля")
    public DashboardPage setAllFields(String trainName, String trainSerial,
                                      String trainPosition, String instanceNumber, String rfid,
                                      String trainType, String technologicalProcess, String manufacturer, String equipmentGroup) {

        if (!trainName.isBlank()) setTextField("trainName", trainName);
        if (!trainSerial.isBlank()) setTextField("trainSerial", trainSerial);
        if (!trainPosition.isBlank()) setTextField("trainPosition", trainPosition);
        if (!instanceNumber.isBlank()) setTextField("instanceNumber", instanceNumber);
        if (!rfid.isBlank()) setTextField("rfid", rfid);

        //?? выбор в автокомплетах (если данные заданы)
        if (!trainType.isBlank()) {
            try {
                selectOptionFromAutocomplete("trainType", trainType);
            } catch (Exception e) {
                Allure.step("Не удалось выбрать trainType: " + e.getMessage());
            }
        }
        if (!technologicalProcess.isBlank()) {
            try {
                selectOptionFromAutocomplete("technologicalProcess", technologicalProcess);
            } catch (Exception e) {
                Allure.step("Не удалось выбрать technologicalProcess: " + e.getMessage());
            }
        }
        if (!manufacturer.isBlank()) {
            try {
                selectOptionFromAutocomplete("manufacturer", manufacturer);
            } catch (Exception e) {
                Allure.step("Не удалось выбрать manufacturer: " + e.getMessage());
            }
        }
        if (!equipmentGroup.isBlank()) {
            try {
                selectOptionFromAutocomplete("equipmentGroup", equipmentGroup);
            } catch (Exception e) {
                Allure.step("Не удалось выбрать equipmentGroup: " + e.getMessage());
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        takeScreenshotPage("Список фильтров", page);

        return this;
    }

    private By inputByName(String name) {
        return By.cssSelector("input[name='" + name + "']");
    }

    private void openAutocompleteForInput(String inputName) {
        By openBtn = By.xpath("//input[@name='" + inputName + "']/following::button[@title='Open'][1]");
        getWait5().until(ExpectedConditions.elementToBeClickable(openBtn)).click();
    }

    @Step("Заполняю поле {name} значением '{value}'")
    private DashboardPage setTextField(String name, String value) {
        By locator = inputByName(name);
        WebElement el = getWait5().until(ExpectedConditions.elementToBeClickable(locator));
        el.clear();
        el.sendKeys(value);

        getWait5().until(d -> {
            try {
                String v = el.getAttribute("value");
                return v != null && (v.equals(value) || v.contains(value));
            } catch (StaleElementReferenceException ex) {
                return false;
            }
        });
        Allure.step(String.format("Поле %s заполнено: %s", name, value));

        return this;
    }

    @Step("Выбираю вариант '{optionText}' в автокомплете поля {inputName}")
    private DashboardPage selectOptionFromAutocomplete(String inputName, String optionText) {
        openAutocompleteForInput(inputName);

        getWait10().until(ExpectedConditions.elementToBeClickable(dropdownListWindowFilterList.get(0)));

        for (WebElement value : dropdownListWindowFilterList) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", value);
            if (value.getText().equals(optionText)) {
                value.click();
//                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", value);
                Allure.step(String.format("Выбран '%s' для поля %s", value.getText(), inputName));
                break;
            }
        }

        return this;

//        List<WebElement> items = getWait10().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(autocompleteListItems));
//
//        for (WebElement item : items) {
//            String text = item.getText();
//            if (text != null && text.toLowerCase().contains(optionText.toLowerCase())) {
//                try {
//                    getWait5().until(ExpectedConditions.elementToBeClickable(item));
//                    item.click();
//                } catch (Exception e) {
//                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
//                }
//                Allure.step(String.format("Выбран '%s' для поля %s", text, inputName));
//                return this;
//            }
//        }
//
//        Allure.addAttachment("Dropdown items for " + inputName, String.join("\n", items.stream().map(WebElement::getText).toList()));
//        throw new NoSuchElementException("Не найден вариант '" + optionText + "' в автокомплете " + inputName);
    }

    @Step("Нажимаю кнопку 'Ок' и жду закрытия диалога")
    public DashboardPage clickOkAndWait() {
        getWait5().until(ExpectedConditions.elementToBeClickable(okButton)).click();
        getWait10().until(ExpectedConditions.invisibilityOfElementLocated(dialogContainer));
        Allure.step("Нажата кнопка Ок, диалог закрылся");
        takeScreenshotPage("Рабочая область после фильтра", page);
        return this;
    }
}
