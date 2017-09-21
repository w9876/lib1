package repository;

import model.Reader;

public interface ReaderRepository {
    Reader addReader(Reader reader);

    Reader getReader(int id);

    boolean readerExists(int readerId);
}
