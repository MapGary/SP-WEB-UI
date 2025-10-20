package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.BaseTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StaticElementsPage extends BasePage{

    private final BaseTest baseTest;

    public StaticElementsPage(WebDriver driver, BaseTest baseTest) {
        super(driver);
        this.baseTest = baseTest;
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
    public void openTimeIntervalDropdown() {
        WebElement dropdown = baseTest.getWait10().until(ExpectedConditions.elementToBeClickable(selectedInterval));
        dropdown.click();
        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(listBox));
    }

    @Step("Получаю выбранный временной интервал (текст)")
    public String timeIntervalSelected() {
        WebElement element = baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(selectedInterval));
        String text = element.getText();
        if (text == null) text = "";
        return text.trim();
    }

    @Step("Получаю все значения выпадающего списка")
    public List<String> getAllOptions() {
        List<WebElement> elements = baseTest.getWait10().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(listOptions));

        List<String> result = new ArrayList<>();
        for (WebElement e : elements) {
            String text = e.getText();
            if (text == null) text = "";
            result.add(text.trim());
        }
        return result;
    }

}
