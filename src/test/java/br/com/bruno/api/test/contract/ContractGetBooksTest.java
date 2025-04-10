package br.com.bruno.api.test.contract;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.objects.Books;
import br.com.bruno.api.services.BookService;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static java.util.stream.Stream.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContractGetBooksTest extends BaseTest {

    @Test
    public void validateContractGetBooks() {
        Response response = BookService.getBooksResponse();

        response.then()
            .statusCode(HttpStatus.SC_OK)
            .body(matchesJsonSchema(new File("src/test/resources/json_schemas/get_books_schema.json")));

        List<Books> booksList = Arrays.asList(response.as(Books[].class));

        assertThat("The book list must not be empty", booksList, is(not(empty())));

        for (Books book : booksList) {
            assertThat("Each book must have an ID", book.getId(), notNullValue());
            assertThat("Each book must have a title", book.getTitle(), notNullValue());
        }
    }
}
