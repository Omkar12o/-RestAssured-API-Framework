package api.tests;

import api.base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

/**
 * Non-functional checks: response time SLAs for key endpoints.
 * Useful to demonstrate basic performance-aware API testing
 * alongside functional CRUD coverage.
 */
public class PerformanceTests extends BaseTest {

    @Test(description = "GET /users should respond within 3000 ms")
    public void listUsers_responseTime_underThreeSeconds() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .time(lessThan(3000L));
    }
}
