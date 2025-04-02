package br.com.bruno.api.dataprovider;

import br.com.bruno.api.objects.Books;
import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;

public class AddBooksDataProvider {

    static Faker faker = new Faker();

    @DataProvider(name = "addBooksSuccess")
    public static Object[][] addBooksSuccess() {
        Books books = Books.builder().
                title(faker.book().title()).
                author(faker.artist().name()).
                genre(faker.book().genre()).
                yearPublished(faker.number().numberBetween(1950, 2023)).
                build();

        return new Object[][] {
                {books}
        };
    }
}
