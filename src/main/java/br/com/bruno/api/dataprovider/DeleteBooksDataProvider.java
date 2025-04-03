package br.com.bruno.api.dataprovider;

import br.com.bruno.api.objects.Books;
import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;

public class DeleteBooksDataProvider {

    static Faker faker = new Faker();

    @DataProvider(name = "deleteBooks")
    public static Object[][] deleteBooks() {
        Books books = Books.builder().
                id(faker.internet().uuid()).
                build();

        return new Object[][] {
                {books}
        };
    }
}
