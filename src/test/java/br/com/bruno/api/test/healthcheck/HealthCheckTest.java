package br.com.bruno.api.test.healthcheck;

import br.com.bruno.api.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class HealthCheckTest extends BaseTest {

    @Test
    public void healthCheckTest() {
        given().
            spec(spec).
        when().
            get("").
        then().
            statusCode(200);
    }
}
