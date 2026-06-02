package org.acme.interfaces.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ListResourceTest {

    @Test
    void getListsShouldReturnEmptyOrExistingLists() {
        given()
                .when().get("/api/lists")
                .then()
                .statusCode(200)
                .body("$", isA(java.util.List.class));
    }

    @Test
    void createListShouldPersistAndReturn201() {
        String body = "{\"title\":\"Mi lista\",\"description\":\"Descripción\",\"color\":\"#6366f1\"}";

        given()
                .contentType(JSON)
                .body(body)
                .when().post("/api/lists")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Mi lista"))
                .body("description", equalTo("Descripción"))
                .body("color", equalTo("#6366f1"))
                .body("taskCount", equalTo(0))
                .body("completedCount", equalTo(0));
    }

    @Test
    void createListShouldReturn400WhenTitleMissing() {
        String body = "{\"description\":\"Sin título\"}";

        given()
                .contentType(JSON)
                .body(body)
                .when().post("/api/lists")
                .then()
                .statusCode(400);
    }

    @Test
    void getListByIdShouldReturnListAfterCreation() {
        String createBody = "{\"title\":\"Lista detalle\",\"color\":\"#10b981\"}";

        String id = given()
                .contentType(JSON)
                .body(createBody)
                .when().post("/api/lists")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        given()
                .when().get("/api/lists/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("title", equalTo("Lista detalle"));
    }

    @Test
    void getListByIdShouldReturn404ForUnknownId() {
        given()
                .when().get("/api/lists/00000000-0000-0000-0000-000000000099")
                .then()
                .statusCode(404);
    }

    @Test
    void updateListShouldModifyTitleAndDescription() {
        String id = given()
                .contentType(JSON)
                .body("{\"title\":\"Original\"}")
                .when().post("/api/lists")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        given()
                .contentType(JSON)
                .body("{\"title\":\"Modificada\",\"description\":\"Nueva desc\"}")
                .when().put("/api/lists/" + id)
                .then()
                .statusCode(200)
                .body("title", equalTo("Modificada"))
                .body("description", equalTo("Nueva desc"));
    }

    @Test
    void deleteListShouldReturn204AndThenNotFound() {
        String id = given()
                .contentType(JSON)
                .body("{\"title\":\"Para borrar\"}")
                .when().post("/api/lists")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        given()
                .when().delete("/api/lists/" + id)
                .then()
                .statusCode(204);

        given()
                .when().get("/api/lists/" + id)
                .then()
                .statusCode(404);
    }
}
