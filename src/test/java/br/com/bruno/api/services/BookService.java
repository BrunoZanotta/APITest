package br.com.bruno.api.services;

import br.com.bruno.api.ConfigLoader;
import br.com.bruno.api.objects.Books;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static br.com.bruno.api.BaseTest.spec;
import static io.restassured.RestAssured.given;

public class BookService {

    public static Books createBook(Books requestBook) {
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

    public static Response createBookAndReturnResponse(Books requestBook) {
        return given()
            .spec(spec)
            .header("api-key", ConfigLoader.getProperty("api-key"))
            .body(requestBook)
        .when()
            .post("books")
        .then()
            .extract().response();
    }

    public static Response addBookBadRequestResponse(Books requestBook) {
        return given()
            .spec(spec)
            .header("api-key", ConfigLoader.getProperty("api-key"))
            .body(requestBook)
        .when()
            .post("books")
        .then()
            .extract().response();
    }

    public static Response createBookWithInvalidCredentials(Books requestBook) {
        return given()
            .spec(spec)
            .body(requestBook)
        .when()
            .post("books")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .extract().response();
    }

    public static Response deleteBookById(String bookId, int expectedStatusCode) {
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

    public static Response getBooksResponse() {
        return given()
            .spec(spec)
        .when()
            .get("books")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().response();
    }

    public static Response getBookNotFoundResponse() {
        return given()
            .spec(spec)
        .when()
            .get("book")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .extract().response();
    }
}
