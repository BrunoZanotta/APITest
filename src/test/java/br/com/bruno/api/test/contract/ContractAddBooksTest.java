package br.com.bruno.api.test.contract;

import br.com.bruno.api.BaseTest;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ContractAddBooksTest extends BaseTest {

    @Test
    public void validateContractAddBooks() {
        given().
            spec(spec).
        when().
            get("books").
        then().
            body(matchesJsonSchema(new File("src/test/resources /json_schemas/add_books_schema.json")));
    }

}
