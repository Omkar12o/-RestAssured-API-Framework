package api.base;

import api.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

/**
 * Base class for all API test classes.
 * Centralizes RestAssured configuration: base URI, auth header, logging.
 */
public class BaseTest {

    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setUp() {
        String baseUri = ConfigReader.get("base.uri");
        String token = ConfigReader.get("access.token");

        RestAssured.baseURI = baseUri;

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
}
