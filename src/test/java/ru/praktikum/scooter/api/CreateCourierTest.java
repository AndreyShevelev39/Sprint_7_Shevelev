package ru.praktikum.scooter.api;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;
import ru.praktikum.scooter.api.client.CourierClient;
import ru.praktikum.scooter.api.model.Courier;
import ru.praktikum.scooter.api.model.CourierCredentials;
import static org.hamcrest.CoreMatchers.*;

public class CreateCourierTest {
    private CourierClient client;
    private Courier courier;
    private final Faker faker = new Faker();

    @Before
    public void setUp() { client = new CourierClient(); }

    @After
    public void tearDown() {
        if (courier != null) {
            ValidatableResponse loginRes = client.login(CourierCredentials.from(courier));
            if (loginRes.extract().statusCode() == HttpStatus.SC_OK) {
                client.delete(loginRes.extract().path("id"));
            }
        }
    }

    @Test @DisplayName("Успешное создание курьера")
    public void courierCanBeCreatedTest() {
        courier = new Courier(faker.name().username(), "1234", "Ivan");
        client.create(courier).statusCode(HttpStatus.SC_CREATED).body("ok", is(true));
    }

    @Test @DisplayName("Ошибка при создании дубликата курьера")
    public void cannotCreateDuplicateCourierTest() {
        courier = new Courier(faker.name().username(), "1234", "Ivan");
        client.create(courier);
        client.create(courier).statusCode(HttpStatus.SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test @DisplayName("Ошибка создания курьера без логина")
    public void cannotCreateWithoutLoginTest() {
        client.create(new Courier(null, "1234", "Ivan"))
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test @DisplayName("Ошибка создания курьера без пароля")
    public void cannotCreateWithoutPasswordTest() {
        client.create(new Courier("LoginOnly", null, "Ivan"))
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}