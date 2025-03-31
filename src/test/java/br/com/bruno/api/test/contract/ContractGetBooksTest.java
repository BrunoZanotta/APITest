package br.com.bruno.api.test.contract;

import br.com.bruno.api.BaseTest;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ContractGetBooksTest extends BaseTest {

    @Test
    public void validateContractGetBooks() {
        given().
            spec(spec).
        when().
            get("books").
        then().
            body(matchesJsonSchema(new File("src/test/resources /json_schemas/get_books_schema.json")));
    }
}
