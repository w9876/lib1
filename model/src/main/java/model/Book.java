package model;

import lombok.Data;

@Data
public class Book {
    private Integer id;
    private final String title;
    private final String author;
    private final int pageCount;
    private Borrow borrow;


}
