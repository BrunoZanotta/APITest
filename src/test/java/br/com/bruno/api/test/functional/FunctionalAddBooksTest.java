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

@Epic("Books Management")
@Feature("Add Books Endpoint")
@Severity(SeverityLevel.CRITICAL)
@Owner("bruno.zanotta")
@Story("Add new books to the catalog")
public class FunctionalAddBooksTest extends BaseTest {

    @AllureId("7")
    @Test(description = "Should successfully add a valid book to the system", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateAddProductTest(Books requestBook) {
        Books createdBook = BookService.createBook(requestBook);

        assertThat("Incorrect title", createdBook.getTitle(), equalTo(requestBook.getTitle()));
        assertThat("Incorrect author", createdBook.getAuthor(), equalTo(requestBook.getAuthor()));
        assertThat("Incorrect genre", createdBook.getGenre(), equalTo(requestBook.getGenre()));
        assertThat("Incorrect year of publication", createdBook.getYearPublished(), equalTo(requestBook.getYearPublished()));
    }

    @AllureId("8")
    @Test(description = "Should return 401 Unauthorized when trying to add books with invalid credentials", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateInvalidCredentialsforAddBooksTest(Books requestBook) {
        String expectedMessage = "Invalid or missing credentials. Make sure you add 'api-key' to the request headers with a valid value";

        Response response = BookService.createBookWithInvalidCredentials(requestBook);

        assertThat("Status code does not match", response.statusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("Error message does not match", response.jsonPath().getString("message"), equalTo(expectedMessage));
    }

    @AllureId("9")
    @Test(description = "Should validate that the book ID is never null or empty", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateBookIdIsNotNullOrEmpty(Books requestBook) {
        Books createdBook = BookService.createBook(requestBook);

        assertThat("Book ID should not be null", createdBook.getId(), notNullValue());
        assertThat("Book ID should not be empty", createdBook.getId().trim().isEmpty(), equalTo(false));
    }

    @AllureId("10")
    @Test(description = "Should validate that the book ID is never null or empty", dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateUniqueIdsForMultipleBooks(Books requestBook) {
        Books firstBook = BookService.createBook(requestBook);
        Books secondBook = BookService.createBook(requestBook);

        assertThat("First book ID should not be null", firstBook.getId(), notNullValue());
        assertThat("Second book ID should not be null", secondBook.getId(), notNullValue());
        assertThat("Book IDs should be different", firstBook.getId(), not(equalTo(secondBook.getId())));
    }

    @AllureId("11")
    @Test(description = "Should return 400 Bad Request when trying to create a book without a title", dataProvider = "booksWithInvalidTitle", dataProviderClass = AddBooksDataProvider.class)
    public void validateAddBookWithoutTitle(Books requestBook) {
        Response response = BookService.addBookBadRequestResponse(requestBook);

        assertThat("body/title must NOT have fewer than 1 characters", response.statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @AllureId("12")
    @Test(description = "Should return 400 Bad Request when title exceeds character limit", dataProvider = "booksWithTitleAtLimit", dataProviderClass = AddBooksDataProvider.class)
    public void validateTitleExceedingMaxLength(Books requestBook) {
        Response response = BookService.addBookBadRequestResponse(requestBook);

        assertThat(response.statusCode(), equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));
    }
}
