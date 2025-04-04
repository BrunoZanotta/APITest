package br.com.bruno.api.test.functional;

import br.com.bruno.api.BaseTest;
import br.com.bruno.api.objects.Books;
import br.com.bruno.api.services.BookService;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FunctionalGetBooksTest extends BaseTest {

    @Test
    public void validateGetBooksSuccessTest() {
        Response response = BookService.getBooksResponse();
        Books[] books = response.as(Books[].class);
        Books firstBook = books[0];

        assertThat("Incorrect book ID", response.jsonPath().getString("[0].id"), equalTo(firstBook.getId()));
        assertThat("Incorrect title", response.jsonPath().getString("[0].title"), equalTo(firstBook.getTitle()));
        assertThat("Incorrect author", response.jsonPath().getString("[0].author"), equalTo(firstBook.getAuthor()));
        assertThat("Incorrect genre", response.jsonPath().getString("[0].genre"), equalTo(firstBook.getGenre()));
        assertThat("Incorrect year of publication", response.jsonPath().getInt("[0].yearPublished"), equalTo(firstBook.getYearPublished()));
        assertThat("Incorrect checked out status", response.jsonPath().getBoolean("[0].checkedOut"), equalTo(firstBook.getCheckedOut()));
        assertThat("Incorrect permanent collection status", response.jsonPath().getBoolean("[0].isPermanentCollection"), equalTo(firstBook.getIsPermanentCollection()));
        assertThat("Incorrect creation date", response.jsonPath().getString("[0].createdAt"), equalTo(firstBook.getCreatedAt()));
    }

    @Test
    public void validateGetBookNotFoundTest() {
        Response response = BookService.getBookNotFoundResponse();

        assertThat("Status code does not match", response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Error message does not match", response.jsonPath().getString("message"), equalTo("Route GET:/book not found"));
        assertThat("Error type does not match", response.jsonPath().getString("error"), equalTo("Not Found"));
        assertThat("Status code field does not match", response.jsonPath().getInt("statusCode"), equalTo(404));
    }
}
