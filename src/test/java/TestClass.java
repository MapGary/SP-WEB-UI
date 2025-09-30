import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseTest;

public class TestClass extends BaseTest {

    @Test
    public void testFirst() {

        Assert.assertEquals(getDriver().getCurrentUrl(), "http://10.0.0.238/login");
    }
}
