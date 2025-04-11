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

    @DataProvider(name = "validBooks")
    public static Object[][] provideValidBooks() {
        return new Object[][] {
                { buildValidBook() }
        };
    }

    public static Books buildBookWithNullTitle() {
        return Books.builder()
                .title(null)
                .author(faker.book().author())
                .genre(faker.book().genre())
                .yearPublished(faker.number().numberBetween(1950, 2023))
                .build();
    }

    @DataProvider(name = "booksWithNullTitle")
    public static Object[][] provideBooksWithInvalidTitle() {
        return new Object[][] {
                { buildBookWithNullTitle() }
        };
    }

    public static Books buildBookWithEmptyTitle() {
        return Books.builder()
                .title(" ")
                .author(faker.book().author())
                .genre(faker.book().genre())
                .yearPublished(faker.number().numberBetween(1950, 2023))
                .build();
    }

    @DataProvider(name = "booksWithEmptyTitle")
    public static Object[][] provideBooksWithEmptyTitle() {
        return new Object[][] {
                { buildBookWithEmptyTitle() }
        };
    }

    public static Books buildBookWithTitleAtCharacterLimit() {
        return Books.builder()
                .title(faker.lorem().characters(500))
                .author(faker.book().author())
                .genre(faker.book().genre())
                .yearPublished(faker.number().numberBetween(1950, 2023))
                .build();
    }

    @DataProvider(name = "booksWithTitleAtLimit")
    public static Object[][] provideBooksWithTitleAtLimit() {
        return new Object[][] {
                { buildBookWithTitleAtCharacterLimit() }
        };
    }

    @DataProvider(name = "booksWithEmptyBody")
    public static Object[][] provideBooksWithEmptyBody() {
        return new Object[][] {
                { "{}", "body must have required property 'title', body must have required property 'author', body must have required property 'genre', body must have required property 'yearPublished'" }
        };
    }
}