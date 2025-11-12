package utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import org.testng.Assert;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TimeUtils {

    public enum Interval {
        WORK_DAY(8, ChronoUnit.HOURS),
        FULL_DAY(24, ChronoUnit.HOURS),
        WEEK(1, ChronoUnit.WEEKS),
        MONTH(1, ChronoUnit.MONTHS),
        YEAR(1, ChronoUnit.YEARS);

        public final long amount;
        public final ChronoUnit unit;
        Interval(long amount, ChronoUnit unit) { this.amount = amount; this.unit = unit; }
    }

    // ожидаемое начало интервала (now - amount unit) в заданной зоне
    public static Instant expectedStartInstantFor(Interval interval, ZoneId zone) {
        ZonedDateTime now = ZonedDateTime.now(zone);

//        if (interval == Interval.WORK_DAY) {
//            // Для WORK_DAY: начало в 10:00 текущего дня
//            ZonedDateTime startOfDay = now.truncatedTo(ChronoUnit.DAYS).withHour(10);
//            return startOfDay.toInstant();
//        }
        // для остальных
        ZonedDateTime start = now.minus(interval.amount, interval.unit);

        return start.toInstant();
    }

    // сравнение Instant с допуском (сек)
    public static void assertTimestampClose(Instant actual, Instant expected, long toleranceSeconds, String message) {
        long diff = Math.abs(Duration.between(actual, expected).getSeconds());
        Allure.step(message + " (diff sec = " + diff + ", tolerance sec = " + toleranceSeconds + ")");
        Assert.assertTrue(diff <= toleranceSeconds,
                message + ". Difference seconds: " + diff + ", tolerance: " + toleranceSeconds);
    }

}
