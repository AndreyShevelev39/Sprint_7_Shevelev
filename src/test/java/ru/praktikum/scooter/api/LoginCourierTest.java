package ru.praktikum.scooter.api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.api.client.CourierClient;
import ru.praktikum.scooter.api.model.Courier;
import ru.praktikum.scooter.api.model.CourierCredentials;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        Courier courier = new Courier("login_ninja_2", "1234", "sasha");
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    public void courierCanLogin() {
        CourierCredentials creds = new CourierCredentials("login_ninja_2", "1234");
        ValidatableResponse response = courierClient.login(creds);

        response.statusCode(200).body("id", notNullValue());
        courierId = response.extract().path("id");
    }

    @Test
    public void loginWithWrongPasswordFails() {
        CourierCredentials creds = new CourierCredentials("login_ninja_2", "wrong_pass");
        ValidatableResponse response = courierClient.login(creds);

        response.statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }
}