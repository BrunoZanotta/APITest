package br.com.bruno.api.test.functional;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.dataprovider.AddBooksDataProvider;
import br.com.bruno.api.objects.Books;
import br.com.bruno.api.services.BookService;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@Epic("Books Management")
@Feature("Add Books Endpoint")
@Severity(SeverityLevel.CRITICAL)
@Owner("bruno.zanotta") // Coloque seu nome, nickname ou e-mail
@Story("Add new books to the catalog")
public class FunctionalAddBooksTest extends BaseTest {

    @AllureId("7")
    @Test(description = "Should successfully add a valid book to the system",
            dataProvider = "addBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateAddProductTest(Books requestBook) {
        Books createdBook = BookService.createBook(requestBook);

        assertThat("Incorrect title", createdBook.getTitle(), equalTo(requestBook.getTitle()));
        assertThat("Incorrect author", createdBook.getAuthor(), equalTo(requestBook.getAuthor()));
        assertThat("Incorrect genre", createdBook.getGenre(), equalTo(requestBook.getGenre()));
        assertThat("Incorrect year of publication", createdBook.getYearPublished(), equalTo(requestBook.getYearPublished()));
    }

    @AllureId("8")
    @Test(description = "Should return 401 Unauthorized when trying to add books with invalid credentials",
            dataProvider = "addBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateInvalidCredentialsforAddBooksTest(Books requestBook) {
        String expectedMessage = "Invalid or missing credentials. Make sure you add 'api-key' to the request headers with a valid value";

        Response response = BookService.createBookWithInvalidCredentials(requestBook);

        assertThat("Status code does not match", response.statusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("Error message does not match", response.jsonPath().getString("message"), equalTo(expectedMessage));
    }
}
