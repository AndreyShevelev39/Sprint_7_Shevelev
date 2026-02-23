package ru.praktikum.scooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.scooter.api.config.Config; // Импортируем конфиг
import ru.praktikum.scooter.api.model.Courier;
import ru.praktikum.scooter.api.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String ROOT = Config.BASE_URL + "/api/v1/courier";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(ROOT)
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post(ROOT + "/login")
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse delete(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(ROOT + "/" + courierId)
                .then();
    }
}