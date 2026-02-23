package ru.praktikum.scooter.api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.api.client.CourierClient;
import ru.praktikum.scooter.api.model.Courier;
import ru.praktikum.scooter.api.model.CourierCredentials;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CreateCourierTest {
    private CourierClient courierClient;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }

    @Test
    public void courierCanBeCreated() {
        String login = "ninja_" + (int) (Math.random() * 1000000);
        Courier courier = new Courier(login, "1234", "sasha");

        ValidatableResponse response = courierClient.create(courier);
        response.statusCode(201).body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    public void cannotCreateTwoIdenticalCouriers() {
        String login = "duplicate_" + (int) (Math.random() * 1000000);
        Courier courier = new Courier(login, "1234", "sasha");

        courierClient.create(courier);
        ValidatableResponse response = courierClient.create(courier);

        response.statusCode(409).body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, "1234", "sasha");
        ValidatableResponse response = courierClient.create(courier);

        response.statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}