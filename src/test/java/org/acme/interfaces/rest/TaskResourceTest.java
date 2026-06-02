package org.acme.interfaces.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class TaskResourceTest {

    private String createList(String title) {
        return given()
                .contentType(JSON)
                .body("{\"title\":\"" + title + "\"}")
                .when().post("/api/lists")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test
    void getTasksShouldReturnEmptyListForNewList() {
        String listId = createList("Lista vacía");

        given()
                .when().get("/api/lists/" + listId + "/tasks")
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    void createTaskShouldReturn201WithTaskData() {
        String listId = createList("Lista con tarea");
        String body = "{\"title\":\"Tarea 1\",\"description\":\"Hacer algo\"}";

        given()
                .contentType(JSON)
                .body(body)
                .when().post("/api/lists/" + listId + "/tasks")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Tarea 1"))
                .body("description", equalTo("Hacer algo"))
                .body("completed", equalTo(false))
                .body("listId", equalTo(listId));
    }

    @Test
    void createTaskShouldReturn400WhenTitleMissing() {
        String listId = createList("Lista validación");

        given()
                .contentType(JSON)
                .body("{\"description\":\"Sin título\"}")
                .when().post("/api/lists/" + listId + "/tasks")
                .then()
                .statusCode(400);
    }

    @Test
    void createTaskShouldReturn404ForUnknownList() {
        given()
                .contentType(JSON)
                .body("{\"title\":\"Huérfana\"}")
                .when().post("/api/lists/00000000-0000-0000-0000-000000000099/tasks")
                .then()
                .statusCode(404);
    }

    @Test
    void updateTaskShouldModifyTitleAndDescription() {
        String listId = createList("Lista update");

        String taskId = given()
                .contentType(JSON)
                .body("{\"title\":\"Original\"}")
                .when().post("/api/lists/" + listId + "/tasks")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        given()
                .contentType(JSON)
                .body("{\"title\":\"Modificada\",\"description\":\"Nuevo desc\"}")
                .when().put("/api/lists/" + listId + "/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Modificada"))
                .body("description", equalTo("Nuevo desc"));
    }

    @Test
    void toggleTaskShouldFlipCompletedFlag() {
        String listId = createList("Lista toggle");

        String taskId = given()
                .contentType(JSON)
                .body("{\"title\":\"Completar esto\"}")
                .when().post("/api/lists/" + listId + "/tasks")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        given()
                .contentType(JSON)
                .body("{\"completed\":true}")
                .when().patch("/api/lists/" + listId + "/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("completed", equalTo(true));

        given()
                .contentType(JSON)
                .body("{\"completed\":false}")
                .when().patch("/api/lists/" + listId + "/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("completed", equalTo(false));
    }

    @Test
    void deleteTaskShouldReturn204AndThenNotFound() {
        String listId = createList("Lista delete");

        String taskId = given()
                .contentType(JSON)
                .body("{\"title\":\"Para borrar\"}")
                .when().post("/api/lists/" + listId + "/tasks")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        given()
                .when().delete("/api/lists/" + listId + "/tasks/" + taskId)
                .then()
                .statusCode(204);

        given()
                .when().get("/api/lists/" + listId + "/tasks")
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    void getTasksShouldReturn404ForUnknownList() {
        given()
                .when().get("/api/lists/00000000-0000-0000-0000-000000000099/tasks")
                .then()
                .statusCode(404);
    }
}
