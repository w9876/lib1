package rest;

import bootstrap.Application;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by SG0891718 on 6/11/2017.
 */

public class BookServiceTest {

    @Before
    public void setUp() {
        Application.main(new String[]{});
    }

    @Test
    public void shouldGetBook() {
        get("http://localhost:8080/book/1").then().body("id", equalTo(1));
        get("http://localhost:8080/book/2").then().body("id", equalTo(2));
    }


}
