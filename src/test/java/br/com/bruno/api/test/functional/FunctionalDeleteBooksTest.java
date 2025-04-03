package br.com.bruno.api.test.functional;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.ConfigLoader;
import br.com.bruno.api.dataprovider.AddBooksDataProvider;
import br.com.bruno.api.dataprovider.DeleteBooksDataProvider;
import br.com.bruno.api.objects.Books;
import io.restassured.response.Response;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class FunctionalDeleteBooksTest extends BaseTest {

    private String id;

    @Test(dataProvider = "addBooks", dataProviderClass = AddBooksDataProvider.class)
    public void getIdBooksSupportTest(Books books) {
        Response response =

        given().
            spec(spec).
                header("api-key", ConfigLoader.getProperty("api-key")).
                body(books).
        when().
            post("books").
        then().
            statusCode(201).
                extract().response();

        id = response.jsonPath().getString("id");
    }

    @Test
    public void validateDeleteBooksTest() {
        given().
            spec(spec).
            header("api-key", ConfigLoader.getProperty("api-key")).
                pathParam("id", id).
        when().
            delete("books/{id}").
        then().
            statusCode(204);
    }

    @Test(dataProvider = "deleteBooks", dataProviderClass = DeleteBooksDataProvider.class)
    public void validateBookNotFoundError (Books books) {
        given().
            spec(spec).
            header("api-key", ConfigLoader.getProperty("api-key")).
            pathParam("id", books.id).
        when().
            delete("books/{id}").
        then().
            statusCode(404).body("message", is("Book with id '"+ books.id + "' not found"));
    }
}
