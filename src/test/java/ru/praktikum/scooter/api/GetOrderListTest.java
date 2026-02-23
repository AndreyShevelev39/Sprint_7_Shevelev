package ru.praktikum.scooter.api;

import org.junit.Test;
import ru.praktikum.scooter.api.client.OrderClient;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest {
    @Test
    public void getOrderListReturnsData() {
        OrderClient orderClient = new OrderClient();
        orderClient.getList()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}