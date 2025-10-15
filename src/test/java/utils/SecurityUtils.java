package utils;

public class SecurityUtils {

    public static String sqlInjectionPayload() {
        return "' OR '1'='1";
    }

}
