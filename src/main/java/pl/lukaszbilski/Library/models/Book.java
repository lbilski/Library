package pl.lukaszbilski.Library.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;
    private String title;
    private String author;
    private String gendre;
    private int publishment;
    private int sheets;
    private int quantity;
}
