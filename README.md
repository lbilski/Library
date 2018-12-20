## Library
### General info:

Application allows to manage library with help two types of users: admin and standard user.
Above of them can explore available books and borrow them if they need it. 
All data is kept in database and they status is updated in accordance with the user's actions
The database works on the mariadb engine and was created on a VPS.

### What was used:

* MariaDB
* Java 8
* JavaFX
* JavaFX Scene Builder 2.0

### Description:

Login window check if login is exist in database and if entered password 
equals password from database.

![login window](https://drive.google.com/uc?export=view&id=15F0dcJHc8qcx9H3-T1iCs5HlgsgcSGw5)

Registry window allows to register a new user with validation of the information entered

![registry window](https://drive.google.com/uc?export=view&id=1UKNYPiYHFbX-GiZJSJ51Q6JiDiwe4gHd)

Library [Biblioteka] tab was created to display all books with a short description. 
This TableView updating automatically after user intervention like a new rent. 
If you don't have a borrowed book, you can book it at your chosen date.

![registry window](https://drive.google.com/uc?export=view&id=1sMIw2DrE5461XPmjGgxm4u0VMxJU3nml)

Rented [Wypożyczone] tab display list of books we borrowed and color "Data zwrotu" 
reminds you of the date of handing over the book. 

![registry window](https://drive.google.com/uc?export=view&id=196JYfZyOMSv-dVEMAFa3QmEpvpTuUV7C)

Number of available items changes automatically after you borrow and return the book.
My account [Moje konto] allows to logout, change password or edit details like a e-mail address or phone number.

### To-Dos:

* Editing of books by the admin
* Contact form from user to admin
* History of renting


