package br.com.bruno.api.dataprovider;

import br.com.bruno.api.objects.Books;
import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;

public class AddBooksDataProvider {

    private static final Faker faker = new Faker();

    public static Books buildValidBook() {
        return Books.builder()
            .title(faker.book().title())
            .author(faker.book().author())
            .genre(faker.book().genre())
            .yearPublished(faker.number().numberBetween(1950, 2023))
            .build();
    }

    public static Books buildBookWithNullTitle() {
        return Books.builder()
                .title(null)
                .author(faker.book().author())
                .genre(faker.book().genre())
                .yearPublished(faker.number().numberBetween(1950, 2023))
                .build();
    }

    public static Books buildBookWithTitleAtCharacterLimit() {
        return Books.builder()
                .title(faker.lorem().characters(500))
                .author(faker.book().author())
                .genre(faker.book().genre())
                .yearPublished(faker.number().numberBetween(1950, 2023))
                .build();
    }

    @DataProvider(name = "validBooks")
    public static Object[][] provideValidBooks() {
        return new Object[][] {
                { buildValidBook() }
        };
    }

    @DataProvider(name = "booksWithInvalidTitle")
    public static Object[][] provideBooksWithInvalidTitle() {
        return new Object[][] {
                { buildBookWithNullTitle() }
        };
    }

    @DataProvider(name = "booksWithTitleAtLimit")
    public static Object[][] provideBooksWithTitleAtLimit() {
        return new Object[][] {
                { buildBookWithTitleAtCharacterLimit() }
        };
    }
}