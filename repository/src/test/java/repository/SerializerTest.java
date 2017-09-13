package repository;

import model.Book;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class SerializerTest {
    Book book1 = new Book("Title1", "Author1", 22);
    Serializer serializer =  new Serializer();

    @Test
    public void toAndFromJson() throws Exception {
        String json = serializer.toJson(book1);
        Book book2 = serializer.fromJson(json, Book.class);

        assertThat(book2).isEqualTo(book1);

    }

}