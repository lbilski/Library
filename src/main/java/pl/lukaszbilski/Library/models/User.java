package pl.lukaszbilski.Library.models;

public class User {
    private String login;
    private String name;
    private String surname;
    private String password;
    private String mail;
    private int phoneNumber;
    private String role;

    public User(String login, String name, String surname, String password, String mail, int phoneNumber, String role) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public User(){

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", role='" + role + '\'' +
                '}';
    }
}
