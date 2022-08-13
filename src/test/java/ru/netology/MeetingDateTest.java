package ru.netology;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MeetingDateTest {


    @Test
    void shouldSendAnApplication() {
        Configuration.holdBrowserOpen = true;

        open("http://localhost:9999");
        RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        $("[placeholder=Город]").setValue(info.getCity());

        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        String meetingDate = DataGenerator.generateDate(5);
        $("[data-test-id=date] input").setValue(meetingDate);

        $("[data-test-id=name] input").setValue(info.getName());
        $("[data-test-id=phone] input").setValue(info.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=success-notification]").shouldHave(text("Успешно!"));
        $("[class=notification__content]")
                .shouldHave(text("Встреча успешно запланирована на " + meetingDate));

        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        String otherMeetingDate = DataGenerator.generateDate(7);
        $("[data-test-id=date] input").setValue(otherMeetingDate);

        $(byText("Запланировать")).click();

        $("[data-test-id=replan-notification]")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Необходимо подтверждение"));
        $(byText("Перепланировать")).click();
        $("[class=notification__content]")
                .shouldHave(text("Встреча успешно запланирована на " + otherMeetingDate));

    }

}
