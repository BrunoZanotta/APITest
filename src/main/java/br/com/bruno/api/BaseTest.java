package br.com.bruno.api;

import io.qameta.allure.testng.AllureTestNg;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import static io.restassured.RestAssured.*;


@Listeners(AllureTestNg.class)
public class BaseTest {

    public static RequestSpecification spec;

    @BeforeClass
    public void setUp() {
        baseURI = "https://library-api.postmanlabs.com/";
        basePath = "";
        spec = new RequestSpecBuilder()
                .setRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .build();

        enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
