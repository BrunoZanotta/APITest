package br.com.bruno.api.dataprovider;

import br.com.bruno.api.objects.Books;
import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;

public class AddBooksDataProvider {

    private static final Faker faker = new Faker();

    @DataProvider(name = "addBooks")
    public Object[][] provideBooks() {
        return new Object[][] {
                { Books.builder()
                        .title(faker.book().title())
                        .author(faker.book().author())
                        .genre(faker.book().genre())
                        .yearPublished(faker.number().numberBetween(1950, 2023))
                        .build() },
                { Books.builder()
                        .title(faker.book().title())
                        .author(faker.book().author())
                        .genre(faker.book().genre())
                        .yearPublished(faker.number().numberBetween(2020, 2050))
                        .build()
                }
        };
    }
}