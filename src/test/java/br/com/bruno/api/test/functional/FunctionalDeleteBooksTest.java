package br.com.bruno.api.test.functional;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.dataprovider.AddBooksDataProvider;
import br.com.bruno.api.dataprovider.DeleteBooksDataProvider;
import br.com.bruno.api.objects.Books;

import br.com.bruno.api.services.BookService;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FunctionalDeleteBooksTest extends BaseTest {

    @Test(dataProvider = "validBooks", dataProviderClass = AddBooksDataProvider.class)
    public void validateDeleteBooksTest(Books requestBook) {
        Books createdBook = BookService.createBook(requestBook);

        BookService.deleteBookById(createdBook.getId(), HttpStatus.SC_NO_CONTENT);
    }

    @Test(dataProvider = "deleteBooks", dataProviderClass = DeleteBooksDataProvider.class)
    public void validateBookNotFoundError(Books requestBook) {
        String expectedMessage = "Book with id '" + requestBook.getId() + "' not found";

        Response response = BookService.deleteBookById(requestBook.getId(), HttpStatus.SC_NOT_FOUND);

        assertThat("Status code does not match", response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Error message does not match", response.jsonPath().getString("message"), equalTo(expectedMessage));
    }
}