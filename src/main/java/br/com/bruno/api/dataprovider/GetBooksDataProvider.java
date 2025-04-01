package br.com.bruno.api.dataprovider;

import br.com.bruno.api.objects.Books;
import org.testng.annotations.DataProvider;

public class GetBooksDataProvider {

    @DataProvider(name = "getBooksSuccess")
    public static Object[][] getBooksSuccess() {
        Books books = Books.builder().
                genre("fiction").
                checkedOut(false).
                build();

        return new Object[][] {
                {books}
        };
    }
}
