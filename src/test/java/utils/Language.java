package utils;

public enum Language {

    US("Login", "Login", "Password",
            "Set new password", "Login", "Switch Language",
            "Current password", "New password", "Submit"),
    RU("Вход", "Логин", "Пароль",
            "Сменить пароль", "Войти", "Сменить язык",
            "Текущий пароль", "Новый пароль", "Отправить");

    private String nameForm;
    private String login;
    private String password;
    private String buttonNewPassword;
    private String buttonLogin;
    private String helperLanguage;
    private String currentPassword;
    private String newPassword;
    private String buttonSubmit;

    Language(String nameForm, String login, String password,
             String buttonNewPassword, String buttonLogin, String helperLanguage,
             String currentPassword, String newPassword, String buttonSubmit) {
        this.nameForm = nameForm;
        this.login = login;
        this.password = password;
        this.buttonNewPassword = buttonNewPassword;
        this.buttonLogin = buttonLogin;
        this.helperLanguage = helperLanguage;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.buttonSubmit = buttonSubmit;
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

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getButtonSubmit() {
        return buttonSubmit;
    }
}
