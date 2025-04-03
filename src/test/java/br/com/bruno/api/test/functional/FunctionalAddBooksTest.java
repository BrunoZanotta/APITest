package br.com.bruno.api.test.functional;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.ConfigLoader;
import br.com.bruno.api.dataprovider.AddBooksDataProvider;
import br.com.bruno.api.objects.Books;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class FunctionalAddBooksTest extends BaseTest {

    @Test(dataProvider = "addBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateAddProductSuccessTest(Books books) {
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

        response.then().body("title", is(books.getTitle()),
                "author", is(books.getAuthor()),
                "genre", is(books.getGenre()),
                "yearPublished", is(books.getYearPublished()));
    }

    @Test(dataProvider = "addBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateInvalidCredentialsforAddBooksTest(Books books) {
        given().
            spec(spec).
                body(books).
        when().
            post("books").
        then().
            statusCode(401).
                body("message", is("Invalid or missing credentials. Make sure you add 'api-key' to the request headers with a valid value"));
    }
}
