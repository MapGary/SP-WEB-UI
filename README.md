# AmericanEagle Test Project Java

[![Java](https://img.shields.io/badge/Java-17-%23ED8B00?logo=coffee)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9.8-%23ED8B00?logo=apachemaven)](https://maven.apache.org/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-%23ED8B00?logo=testng)](https://testng.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.33-%23ED8B00?logo=selenium)](https://www.selenium.dev/)
[![Allure](https://img.shields.io/badge/Allure-Report-%23FF6A00?logo=allure)](https://allurereport.org/)

---

Это репозиторий для проекта по автоматизации тестовых сценариев для сайта Safe Plant web с
использованием UI тестов.

## Содержание

- [🛠️ Технологический стек](#-технологический-стек)
- [🚀 Запуск тестов](#-запуск-тестов)
- [⚙️ Файл конфигурации](#-файл-конфигурации)

---

## 🛠️ Технологический стек

<p align="center">
  <a href="https://www.jetbrains.com/idea/" rel="nofollow"><img width="10%" title="IntelliJ IDEA" src="images/logo/intellij.png" alt="Intellij_IDEA" style="max-width: 100%;"></a>
  <a href="https://www.java.com/" rel="nofollow"><img width="10%" title="Java" src="images/logo/Java.png" alt="Java" style="max-width: 100%;"></a>
  <a href="https://www.selenium.dev/" rel="nofollow"><img width="10%" title="Selenium" src="images/logo/selenium.png" alt="Selenium" style="max-width: 100%;"></a>
  <a href="https://maven.apache.org/" rel="nofollow"><img width="10%" title="Gradle" src="images/logo/Maven.png" alt="Maven"></a>
  <a href="https://testng.org/" rel="nofollow"><img width="10%" title="JUnit5" src="images/logo/TestNG.png" alt="TestNG" style="max-width: 100%;"></a>
  <a href="https://allurereport.org/" rel="nofollow"><img width="10%" title="Allure Report" src="images/logo/Allure.png" alt="Allure" style="max-width: 100%;"></a>
</p>

- **Язык программирования:** Java 17
- **UI тестирование:** Selenium
- **Сборка:** Maven
- **Тестовый фреймворк:** TestNG
- **Шаблон проектирования:** Page Object Model (POM)
- **Отчетность:** Allure Report

**Содержание Allure отчёта:**

- Шаги тестов
- Автоматические скриншоты для упавших UI-тестов (кроме тестов с секретными данными)
- Page Source для упавших UI-тестов

---

## 🚀 Запуск тестов

### Команды для запуска:

Все тесты (кроме дефектных):

   ```bash     
       mvn clean test       
   ```

---

## ⚙️ Файл конфигурации

Чтением данных из Properties файлов занимается класс `TestConfig`

Если у нас есть несколько properties файлов, то нам нужен способ переключаться между ними.
Для этого нужно добавить поля: env и properties и конструктор, который будет инициализировать эти поля.
В данном случае название переменной "env", значение по умолчанию "default". И уже по выбранному значению env нужно загружать значения из properties файла.
```java
public class TestConfig {
    String env;
    Properties properties;

    public TestConfig() {
        env = System.getProperty("env", "default");
        properties = getPropertiesByEnv(env);
    }
}
```

Чтобы реализовать утилитарный `getPropertiesByEnv` метод нужно добавить импорт `import java.util.Properties;` также стоит реализовать обработку исключений.
Например, ситуации, если файла для такого тестового окружения по имени env не существует, либо внутри properties файла нет нужной переменной.
Цепочка методов `load(getClass().getClassLoader().getResourceAsStream(env + ".properties"));` служит для загрузки properties файла по имени env из resources.

```java
public class TestConfig {
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
```
Теперь когда у нас читается properties файл можно получить конкретное значение переменной, например, baseUrl:

```java
public class TestConfig {
    public String getBaseUrl() {
        String baseUrl = properties.getProperty("baseUrl");
        assertNotNull(baseUrl, String.format("BaseUrl is not found in %s.properties", env));
        System.out.println("Base URL: " + baseUrl);
        return baseUrl;
    }
}
```

## Добавление конфиденциальных данных

При работе с проектами может возникнуть необходимость передавать не только baseUrl, но и логины, пароли, API-ключи. Ни в коем случае не храните такие данные в коде! Это может привести к утечке данных и компрометации учетных записей.

Одним из возможных вариантов является использование properties файлов, в которых значения переменных будет храниться только локально.
А в CI/CD данные поля можно будет передать как secret параметры на уровне самой джобы.

Алгоритм действий:
1. Создайте копию файла `default.properties.TEMPLATE`, удалите изназвания `.TEMPLATE`
2. Добавьте ключи для секретных данных, но без значений, и закоммитьте
```properties
baseUrl=http://10.0.0.238
userName=
password=
```
3. Добавьте .properties файлы в .gitignore, чтобы Git перестал их отслеживать
```gitignore
src/test/resources/*.properties
```
4. Добавьте секретные значения локально и убедитесь, что они не попадают в коммиты
```properties
baseUrl=http://10.0.0.238
userName=username
password=password
```
5. Дополнительно: Если нужно перестать отслеживать изменения в файле, можно выполнить комманду:
```bash
git update-index --skip-worktree src/test/resources/yourName.properties
```