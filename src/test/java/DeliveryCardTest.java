import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryCardTest {


    @BeforeEach
    void setUp() {//Открываем страницу.
        //   Configuration.headless = true;
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
// ЗАПУСКАЕМ
// gradlew clean test allureReport
// gradlew allureServe
    }



    DataGenerator.UserInfo user = DataGenerator.Registration.generateValidUser();//Создаем юзера из дата класса
    String firstMeetingDay = DataGenerator.generateDate(3);//Встреча доступна через 3 дня
    String secondMeetingDay = DataGenerator.generateDate(7);// Для смены даты встречи
    String badMeetingDay = DataGenerator.generateDate(2);// Косячная дата для тестов


    @Test
    void shouldTestHappyPath() {//Тестируем форму валидными значениями
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".notification__title").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDay));
    }

    @Test
    void shouldTestChangeDateAfterRegistration() {//После успешной регистрации, меняем дату и регим снова
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".notification__title").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDay));
        $("[data-test-id=date] input").doubleClick().sendKeys(secondMeetingDay);
        $(".button__text").click();
        $(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Перепланировать")).click();
        $(withText("Успешно!")).shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDay));
    }

    //Тестируем пустые поля.
    @Test
    void shouldTestWarnIfAllFieldsEmpty() {
        $(".button__text").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestWarnIfCityFieldEmpty() {
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestWarnIfNameFieldEmpty() {
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestWarnIfPhoneFieldEmpty() {
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestWarnIfCheckBoxEmpty() {
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $(".button__text").click();
        $(".input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    //Тестируем неккоректный ввод данных.

    @Test
    void shouldTestWarnIfBadCityProvince() {//
        $("[data-test-id=city] input").setValue("Тольятти");
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestWarnIfBadCityEnglish() {
        $("[data-test-id=city] input").setValue("Tomsk");
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }


    @Test
    void shouldTestWarnIfBadDate() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(badMeetingDay);
        $("[data-test-id=name] input").setValue("Денисов Андрей");
        $("[data-test-id=phone] input").setValue("+79370400780");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestWarnIfBadUserNameEnglish() {
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDay);
        $("[data-test-id=name] input").setValue("Ivanov Denis");
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

}
