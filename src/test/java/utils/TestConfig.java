package utils;

import java.io.IOException;
import java.util.Properties;

import static org.testng.Assert.assertNotNull;

public class TestConfig {
    String env;
    Properties properties;

    public TestConfig() {
        env = System.getProperty("env", "demo");
        properties = getPropertiesByEnv(env);
    }

    public String getBaseUrl() {
        String baseUrl = properties.getProperty("baseUrl");
        assertNotNull(baseUrl, String.format("BaseUrl is not found in %s.properties", env));

        return baseUrl;
    }

    public String getUserName() {
        String baseUrl = properties.getProperty("userName");
        assertNotNull(baseUrl, String.format("UserName is not found in %s.properties", env));

        return baseUrl;
    }

    public String getPassword() {
        String baseUrl = properties.getProperty("password");
        assertNotNull(baseUrl, String.format("Password is not found in %s.properties", env));

        return baseUrl;
    }

    private Properties getPropertiesByEnv(String env) {
        Properties testProperties = new Properties();
        try {
            testProperties.load(getClass().getClassLoader().getResourceAsStream(env + ".properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot open %s.properties", env));
        }
        return testProperties;
    }
}