package pl.lukaszbilski.Library.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentedBook{
    public int rented_id;
    public int id_ksiazki;
    public Date data_wypozyczenia;
    public Date data_zwrotu;
    public int ilosc;
    public String tytuł;
    public String autor;
    public String gatunek;
}
