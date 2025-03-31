package br.com.bruno.api.test.functional;

import br.com.bruno.api.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class FunctionalGetBooksTest extends BaseTest {

    @Test
    public void validateGetBooksSuccessTest() {
        Response books =
            given().
                spec(spec).
            when().
                get("books").
            then().
                extract().response();

        List<Map<String, Object>> responseData = books.jsonPath().getList("");
        Map<String, Object> firstBook = responseData.get(0);

        String id = (String) firstBook.get("id");
        String title = (String) firstBook.get("title");
        String author = (String) firstBook.get("author");
        String genre = (String) firstBook.get("genre");
        Integer yearPublished = (Integer) firstBook.get("yearPublished");
        Boolean checkedOut = (boolean) firstBook.get("checkedOut");
        Boolean isPermanentCollection = (boolean) firstBook.get("isPermanentCollection");
        String createdAt = (String) firstBook.get("createdAt");

        given().
            spec(spec).
        when().
            get("books").
        then().
            statusCode(200).
                body("[0].id", is(id),
                    "[0].title", is(title),
                    "[0].author", is(author),
                    "[0].genre", is(genre),
                    "[0].yearPublished", is(yearPublished),
                    "[0].checkedOut", is(checkedOut),
                    "[0].isPermanentCollection", is(isPermanentCollection),
                    "[0].createdAt", is(createdAt));
    }

    @Test
    public void validateGetBookNotFoundTest() {
        given().
            spec(spec).
        when().
            get("book").
        then().
            statusCode(404).
                body(    "message", is("Route GET:/book not found"),
                "error", is("Not Found"),
                "statusCode", is(404));
    }
}
