package br.com.bruno.api.test.contract;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.ConfigLoader;
import br.com.bruno.api.objects.Books;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ContractAddBooksTest extends BaseTest {

    @Test(dataProvider = "addBooks", dataProviderClass = br.com.bruno.api.dataprovider.AddBooksDataProvider.class)
    public void validateContractAddBooks(Books books) {
        given().
            spec(spec).
                header("api-key", ConfigLoader.getProperty("api-key")).
                body(books).
        when().
            post("books").
        then().
            body(matchesJsonSchema(new File("src/test/resources /json_schemas/add_books_schema.json")));
    }

}
