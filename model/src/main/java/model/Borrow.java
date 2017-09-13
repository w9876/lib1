package model;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class Borrow {
    private final int readerId;
    private final LocalDate borrowDate;
    private final LocalDate returnBy;
}
