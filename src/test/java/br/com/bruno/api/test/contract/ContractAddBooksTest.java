package br.com.bruno.api.test.contract;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.dataprovider.AddBooksDataProvider;
import br.com.bruno.api.objects.Books;
import br.com.bruno.api.services.BookService;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ContractAddBooksTest extends BaseTest {

    @Test(description = "Should validate JSON schema of successfully created book", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateContractAddBooks(Books requestBook) {
        Response response = BookService.createBookResponse(requestBook);

        response.then().statusCode(HttpStatus.SC_CREATED).body(matchesJsonSchema(new File("src/test/resources/json_schemas/add_books_schema.json")));
    }
}