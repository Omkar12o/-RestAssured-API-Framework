package api.tests;

import api.base.BaseTest;
import api.pojo.User;
import api.utils.DataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * End-to-end CRUD test suite for the GoRest /users resource.
 * Covers: Create, Read (single + list), Update (PUT + PATCH), Delete,
 * plus negative scenarios and schema/status validations.
 *
 * Run order is enforced via TestNG priority since Update/Delete depend
 * on the user created in createUser_shouldReturn201().
 */
public class UserCrudTests extends BaseTest {

    private static Long createdUserId;

    @Test(priority = 1, description = "POST /users - create a new user, expect 201 Created")
    public void createUser_shouldReturn201() {
        User newUser = new User(
                DataGenerator.randomName(),
                DataGenerator.randomEmail(),
                DataGenerator.randomGender(),
                DataGenerator.randomStatus()
        );

        Response response = given()
                .spec(requestSpec)
                .body(newUser)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo(newUser.getName()))
                .body("email", equalTo(newUser.getEmail()))
                .body("id", notNullValue())
                .extract().response();

        createdUserId = response.jsonPath().getLong("id");
        Assert.assertNotNull(createdUserId, "Created user id should not be null");
    }

    @Test(priority = 2, description = "GET /users/{id} - fetch the created user, expect 200 OK")
    public void getUserById_shouldReturn200() {
        given()
                .spec(requestSpec)
                .pathParam("id", createdUserId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdUserId.intValue()));
    }

    @Test(priority = 3, description = "GET /users - list users filtered by status=active, expect 200 OK")
    public void listUsers_filterByStatus_shouldReturn200() {
        given()
                .spec(requestSpec)
                .queryParam("status", "active")
                .when()
                .get("/users")
                .then()
                .statusCode(200);
    }

    @Test(priority = 4, description = "PUT /users/{id} - full update of the user, expect 200 OK")
    public void updateUser_putShouldReturn200() {
        User updatedUser = new User(
                "Updated " + DataGenerator.randomName(),
                DataGenerator.randomEmail(),
                "female",
                "active"
        );

        given()
                .spec(requestSpec)
                .pathParam("id", createdUserId)
                .body(updatedUser)
                .when()
                .put("/users/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedUser.getName()))
                .body("status", equalTo("active"));
    }

    @Test(priority = 5, description = "PATCH /users/{id} - partial update (status only), expect 200 OK")
    public void updateUser_patchStatus_shouldReturn200() {
        given()
                .spec(requestSpec)
                .pathParam("id", createdUserId)
                .body("{\"status\": \"inactive\"}")
                .when()
                .patch("/users/{id}")
                .then()
                .statusCode(200)
                .body("status", equalTo("inactive"));
    }

    @Test(priority = 6, description = "DELETE /users/{id} - delete the user, expect 204 No Content")
    public void deleteUser_shouldReturn204() {
        given()
                .spec(requestSpec)
                .pathParam("id", createdUserId)
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(204);
    }

    @Test(priority = 7, description = "GET /users/{id} - fetching deleted user, expect 404 Not Found")
    public void getDeletedUser_shouldReturn404() {
        given()
                .spec(requestSpec)
                .pathParam("id", createdUserId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(404);
    }

    @Test(priority = 8, description = "POST /users - create with invalid email, expect 422 Unprocessable Entity")
    public void createUser_invalidEmail_shouldReturn422() {
        given()
                .spec(requestSpec)
                .body("{\"name\":\"Invalid Email User\",\"email\":\"not-an-email\",\"gender\":\"male\",\"status\":\"active\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(422)
                .body("[0].field", equalTo("email"));
    }

    @Test(priority = 9, description = "POST /users - missing required field 'name', expect 422")
    public void createUser_missingName_shouldReturn422() {
        given()
                .spec(requestSpec)
                .body("{\"email\":\"" + DataGenerator.randomEmail() + "\",\"gender\":\"male\",\"status\":\"active\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(422)
                .body("[0].field", equalTo("name"));
    }

    @Test(priority = 10, description = "GET /users/{invalidId} - non-existent id, expect 404")
    public void getUser_nonExistentId_shouldReturn404() {
        given()
                .spec(requestSpec)
                .pathParam("id", 999999999)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(404);
    }
}
