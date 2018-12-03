package pl.lukaszbilski.Library.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int user_id;
    private String login;
    private String name;
    private String surname;
    private String password;
    private String mail;
    private int phoneNumber;
    private String role;
}
