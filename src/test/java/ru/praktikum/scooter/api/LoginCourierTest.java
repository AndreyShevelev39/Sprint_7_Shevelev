package ru.praktikum.scooter.api;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.api.client.CourierClient;
import ru.praktikum.scooter.api.model.Courier;
import ru.praktikum.scooter.api.model.CourierCredentials;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    private CourierClient client;
    private Courier courier;
    private Integer courierId;
    private final Faker faker = new Faker();

    @Before
    public void setUp() {
        client = new CourierClient();

        String login = faker.letterify("ninja??????") + faker.number().digits(5);
        courier = new Courier(login, "password123", "Ivan");


        client.create(courier)
                .statusCode(HttpStatus.SC_CREATED);
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            client.delete(courierId);
        } else {
            ValidatableResponse loginRes = client.login(CourierCredentials.from(courier));
            if (loginRes.extract().statusCode() == HttpStatus.SC_OK) {
                int id = loginRes.extract().path("id");
                client.delete(id);
            }
        }
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void courierCanLoginTest() {
        ValidatableResponse response = client.login(CourierCredentials.from(courier));

        response.statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());

        courierId = response.extract().path("id");
    }

    @Test
    @DisplayName("Ошибка логина: неверный пароль")
    public void loginWithWrongPasswordTest() {
        CourierCredentials creds = new CourierCredentials(courier.getLogin(), "wrong_password");
        client.login(creds)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка логина: несуществующий пользователь")
    public void loginWithNonExistentUserTest() {
        CourierCredentials creds = new CourierCredentials("unexisting_user_" + faker.number().digits(10), "1234");
        client.login(creds)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка логина: без логина или пароля")
    public void loginWithoutRequiredFieldTest() {
        CourierCredentials creds = new CourierCredentials("", courier.getPassword());
        client.login(creds)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}