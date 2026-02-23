package ru.praktikum.scooter.api;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.scooter.api.client.OrderClient;
import ru.praktikum.scooter.api.model.Order;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final List<String> colors;
    private OrderClient orderClient;
    private int track;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвет заказа: {0}")
    public static Object[][] data() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()},
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        if (track != 0) {
            orderClient.cancel(track);
        }
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void createOrderWithDifferentColorsTest() {
        Order order = new Order("Ivan", "Ivanov", "Moscow, 1", 4, "+79991234567", 2, "2024-12-12", "Comment", colors);

        ValidatableResponse response = orderClient.create(order);

        response.statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());


        track = response.extract().path("track");
    }
}