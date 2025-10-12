package utils;

public enum Language {

    US("Login", "Login", "Password", "Set new password", "Login", "Switch Language"),
    RU("Вход", "Логин", "Пароль", "Сменить пароль", "Войти", "Сменить язык");

    private String nameForm;
    private String login;
    private String password;
    private String buttonNewPassword;
    private String buttonLogin;
    private String helperLanguage;

    Language(String nameForm, String login, String password, String buttonNewPassword, String buttonLogin, String helperLanguage) {
        this.nameForm = nameForm;
        this.login = login;
        this.password = password;
        this.buttonNewPassword = buttonNewPassword;
        this.buttonLogin = buttonLogin;
        this.helperLanguage = helperLanguage;
    }

    public String getNameForm() {
        return nameForm;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getButtonNewPassword() {
        return buttonNewPassword;
    }

    public String getButtonLogin() {
        return buttonLogin;
    }

    public String getHelperLanguage() {
        return helperLanguage;
    }
}
