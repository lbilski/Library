package pl.lukaszbilski.Library.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String name;
    private String surname;
    private String password;
    private String mail;
    private int phoneNumber;
    private String role;
}
