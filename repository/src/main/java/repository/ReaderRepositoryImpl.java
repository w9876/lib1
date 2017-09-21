package repository;

import model.Reader;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReaderRepositoryImpl implements ReaderRepository {
    private final AtomicInteger idCounter = new AtomicInteger(1_000_000);
    private final ConcurrentHashMap<Integer, Reader> readers = new ConcurrentHashMap<>();
    private final Serializer serializer = new Serializer();

    @Override
    public Reader addReader(Reader reader) {
        Reader clone = getClone(reader);
        clone.setId(idCounter.incrementAndGet());
        readers.put(clone.getId(), clone);
        return getReader(clone.getId());
    }

    private Reader getClone(Reader reader) {
        return serializer.fromJson(serializer.toJson(reader), Reader.class);
    }

    @Override
    public Reader getReader(int id) {
        return getClone(readers.get(id));
    }

    @Override
    public boolean readerExists(int readerId) {
        return readers.get(readerId) != null;
    }
}
