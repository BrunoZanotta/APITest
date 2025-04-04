package br.com.bruno.api.dataprovider;

import br.com.bruno.api.objects.Books;
import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;

public class GetBooksDataProvider {

    static Faker faker = new Faker();

    @DataProvider(name = "getBooks")
    public static Object[][] getBooks() {
        Books requestBook = Books.builder().
                genre("fiction").
                checkedOut(false).
                build();

        return new Object[][] {
                {requestBook}
        };
    }
}
