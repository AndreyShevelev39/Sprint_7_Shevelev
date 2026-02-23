package ru.praktikum.scooter.api;

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

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()},
        };
    }

    @Test
    public void createOrderWithDifferentColors() {
        OrderClient orderClient = new OrderClient();
        Order order = new Order("Naruto", "Uzumaki", "Konoha, 14", 4, "+7 999 000 11 22", 5, "2024-10-10", "Comment", colors);

        orderClient.create(order)
                .statusCode(201)
                .body("track", notNullValue());
    }
}