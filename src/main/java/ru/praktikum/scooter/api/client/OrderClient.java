package ru.praktikum.scooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.scooter.api.config.Config; // Импортируем конфиг
import ru.praktikum.scooter.api.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ROOT = Config.BASE_URL + "/api/v1/orders";

    @Step("Создание заказа")
    public ValidatableResponse create(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ROOT)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getList() {
        return given()
                .get(ROOT)
                .then();
    }
}