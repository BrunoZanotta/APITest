package br.com.bruno.api.test.functional;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.dataprovider.AddBooksDataProvider;
import br.com.bruno.api.objects.Books;
import br.com.bruno.api.services.BookService;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FunctionalAddBooksTest extends BaseTest {

    @AllureId("7")
    @Test(description = "Should successfully add a valid book to the system", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateAddProductTest(Books requestBook) {
        Response response = BookService.createBookResponse(requestBook);
        Books createdBook = response.as(Books.class);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_CREATED));
        assertThat(createdBook.getTitle(), equalTo(requestBook.getTitle()));
        assertThat(createdBook.getAuthor(), equalTo(requestBook.getAuthor()));
        assertThat(createdBook.getGenre(), equalTo(requestBook.getGenre()));
        assertThat(createdBook.getYearPublished(), equalTo(requestBook.getYearPublished()));
    }

    @AllureId("8")
    @Test(description = "Should return 401 Unauthorized when trying to add books with invalid credentials", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateInvalidCredentialsforAddBooksTest(Books requestBook) {
        Response response = BookService.createBookWithInvalidCredentials(requestBook);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat(response.jsonPath().getString("message"), equalTo("Invalid or missing credentials. Make sure you add 'api-key' to the request headers with a valid value"));
    }

    @AllureId("9")
    @Test(description = "Should validate that the book ID is never null or empty", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateBookIdIsNotNullOrEmpty(Books requestBook) {
        Response response = BookService.createBookResponse(requestBook);
        Books createdBook = response.as(Books.class);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_CREATED));
        assertThat(createdBook.getId(), notNullValue());
        assertThat(createdBook.getId().trim().isEmpty(), equalTo(false));
    }

    @AllureId("10")
    @Test(description = "Should validate that the book ID is never null or empty", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateUniqueIdsForMultipleBooks(Books requestBook) {
        Response response = BookService.createBookResponse(requestBook);
        Books firstBook = BookService.createBook(requestBook);
        Books secondBook = BookService.createBook(requestBook);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_CREATED));
        assertThat(firstBook.getId(), notNullValue());
        assertThat(secondBook.getId(), notNullValue());
        assertThat(firstBook.getId(), not(equalTo(secondBook.getId())));
    }

    @AllureId("11")
    @Test(description = "Should return 400 Bad Request when trying to create a book without a title", dataProvider = "booksWithNullTitle", dataProviderClass = AddBooksDataProvider.class)
    public void validateAddBookWithoutTitle(Books requestBook) {
        Response response = BookService.createBookResponse(requestBook);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(response.jsonPath().getString("message"), equalTo("body/title must NOT have fewer than 1 characters"));
        assertThat(response.jsonPath().getString("errors[0].instancePath"), equalTo("/title"));
        assertThat(response.jsonPath().getString("errors[0].schemaPath"), equalTo("#/properties/title/minLength"));
        assertThat(response.jsonPath().getString("errors[0].keyword"), equalTo("minLength"));
        assertThat(response.jsonPath().getInt("errors[0].params.limit"), equalTo(1));
        assertThat(response.jsonPath().getString("errors.message"), equalTo("[must NOT have fewer than 1 characters]"));
    }

    @AllureId("12")
    @Test(description = "Should return 500 Internal Server Error when title exceeds character limit", dataProvider = "booksWithTitleAtLimit", dataProviderClass = AddBooksDataProvider.class)
    public void validateTitleExceedingMaxLength(Books requestBook) {
        Response response = BookService.createBookResponse(requestBook);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        assertThat(response.jsonPath().getString("code"), equalTo("22001"));
        assertThat(response.jsonPath().getString("error"), equalTo("Internal Server Error"));
        assertThat(response.jsonPath().getString("message"), equalTo("insert into \"books\" (\"author\", \"genre\", \"title\", \"yearPublished\") values ($1, $2, $3, $4) returning * - value too long for type character varying(255)"));
    }

    @AllureId("13")
    @Test(description = "Should return 500 Internal Server Error when title empty", dataProvider = "booksWithEmptyTitle", dataProviderClass = AddBooksDataProvider.class)
    public void validateAddBookEmptyTitle(Books requestBook) {
        Response response = BookService.createBookResponse(requestBook);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        assertThat(response.jsonPath().getString("error"),  equalTo("Internal Server Error"));
        assertThat(response.jsonPath().getString("message"), equalTo("Cannot read properties of null (reading '0')"));
    }

    @AllureId("14")
    @Test(description = "Should return 500 Internal Server Error when body empty", dataProvider = "booksWithEmptyBody", dataProviderClass = AddBooksDataProvider.class)
    public void validateAddBookEmptyBody(String rawBody, String expectedMessage) {
        Response response = BookService.createBookWithRawBody(rawBody);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(response.jsonPath().getString("message"), equalTo(expectedMessage));
        assertThat(response.jsonPath().getString("errors[0].instancePath"), equalTo(""));
        assertThat(response.jsonPath().getString("errors[0].schemaPath"), equalTo("#/required"));
        assertThat(response.jsonPath().getString("errors[0].keyword"), equalTo("required"));
        assertThat(response.jsonPath().getString("errors[0].params.missingProperty"), equalTo("title"));
        assertThat(response.jsonPath().getString("errors[0].message"), equalTo("must have required property 'title'"));

        assertThat(response.jsonPath().getString("errors[1].instancePath"), equalTo(""));
        assertThat(response.jsonPath().getString("errors[1].schemaPath"), equalTo("#/required"));
        assertThat(response.jsonPath().getString("errors[1].keyword"), equalTo("required"));
        assertThat(response.jsonPath().getString("errors[1].params.missingProperty"), equalTo("author"));
        assertThat(response.jsonPath().getString("errors[1].message"), equalTo("must have required property 'author'"));

        assertThat(response.jsonPath().getString("errors[2].instancePath"), equalTo(""));
        assertThat(response.jsonPath().getString("errors[2].schemaPath"), equalTo("#/required"));
        assertThat(response.jsonPath().getString("errors[2].keyword"), equalTo("required"));
        assertThat(response.jsonPath().getString("errors[2].params.missingProperty"), equalTo("genre"));
        assertThat(response.jsonPath().getString("errors[2].message"), equalTo("must have required property 'genre'"));

        assertThat(response.jsonPath().getString("errors[3].instancePath"), equalTo(""));
        assertThat(response.jsonPath().getString("errors[3].schemaPath"), equalTo("#/required"));
        assertThat(response.jsonPath().getString("errors[3].keyword"), equalTo("required"));
        assertThat(response.jsonPath().getString("errors[3].params.missingProperty"), equalTo("yearPublished"));
        assertThat(response.jsonPath().getString("errors[3].message"), equalTo("must have required property 'yearPublished'"));
    }


}
