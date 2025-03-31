package br.com.bruno.api;

import com.aventstack.extentreports.testng.listener.ExtentITestListenerClassAdapter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import static io.restassured.RestAssured.*;


@Listeners(ExtentITestListenerClassAdapter.class)
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
