package br.com.bruno.api.test.functional;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.ConfigLoader;
import br.com.bruno.api.dataprovider.AddBooksDataProvider;
import br.com.bruno.api.dataprovider.DeleteBooksDataProvider;
import br.com.bruno.api.objects.Books;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FunctionalDeleteBooksTest extends BaseTest {

    private Books createBook(Books requestBook) {
        return given()
            .spec(spec)
            .header("api-key", ConfigLoader.getProperty("api-key"))
            .body(requestBook)
        .when()
            .post("books")
        .then()
            .statusCode(HttpStatus.SC_CREATED)
                .extract().as(Books.class);
    }

    private Response deleteBookById(String bookId, int expectedStatusCode) {
        return given()
            .spec(spec)
            .header("api-key", ConfigLoader.getProperty("api-key"))
            .pathParam("id", bookId)
        .when()
            .delete("books/{id}")
        .then()
            .statusCode(expectedStatusCode)
                .extract().response();
    }
    
    @Test(dataProvider = "addBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateDeleteBooksTest(Books requestBook) {
        Books createdBook = createBook(requestBook);

        deleteBookById(createdBook.getId(), HttpStatus.SC_NO_CONTENT);
    }


    @Test(dataProvider = "deleteBooks", dataProviderClass = DeleteBooksDataProvider.class)
    public void validateBookNotFoundError(Books requestBook) {
        String expectedMessage = "Book with id '" + requestBook.getId() + "' not found";

        Response response = deleteBookById(requestBook.getId(), HttpStatus.SC_NOT_FOUND);

        assertThat("Status code does not match", response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Error message does not match", response.jsonPath().getString("message"), equalTo(expectedMessage));
    }
}