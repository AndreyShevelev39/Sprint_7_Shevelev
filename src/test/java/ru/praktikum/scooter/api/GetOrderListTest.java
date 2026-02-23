package ru.praktikum.scooter.api;

import org.apache.http.HttpStatus;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderListTest {
    @Test
    public void getOrderListTest() {
        given().get("https://qa-scooter.praktikum-services.ru/api/v1/orders")
                .then().statusCode(HttpStatus.SC_OK).body("orders", notNullValue());
    }
}