package controller;

import model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.BookRepository;

@RestController
@RequestMapping("/book/{id}")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Book book(@PathVariable String id) {
        return bookRepository.getBook(Integer.valueOf(id));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Book upsertBook(Book book) {
        return bookRepository.upsertBook(book);
    }


}