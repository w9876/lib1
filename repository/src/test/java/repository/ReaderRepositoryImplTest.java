package repository;

import model.Reader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by SG0891718 on 7/9/2017.
 */
public class ReaderRepositoryImplTest {
    ReaderRepository repo = new ReaderRepositoryImpl();

    @Test
    public void shouldAddReader() {
        // given
        Reader reader1 = new Reader("FirstName", "LastName");

        // when

        Reader persistedReader1 = repo.add(reader1);

        // then
        assertThat(persistedReader1).isNotSameAs(reader1);
        assertThat(persistedReader1.getId()).isNotNull();
        assertThat(persistedReader1).isEqualToIgnoringGivenFields(reader1, "id");

        // when
        Reader reader2 = new Reader("FirstName2", "LastName2");
        Reader persistedReader2 = repo.add(reader2);

        // then
        assertThat(persistedReader2.getId()).isNotEqualTo(persistedReader1.getId());

    }
}
