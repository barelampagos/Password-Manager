# Password-Manager
Program that generates a hard to crack, easy to remember password given a website. Encrypts the user's website/password pairs to a text file of their choice using AES encryption.

## Password Generation
Users can generate:
- XKCD Algorithm: A hard to brute-force crack password based on an [xkcd](https://xkcd.com/936/) comic. Selects 4 words at random and appends them together to form the password.
- Random Password Generator: Generates a password of user specified length from random, yet valid password characters.
```abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~`!@#$%^&*()_-+=[{}]|':?<>.,```

## Features
- Website/Password pairs can be exported to/loaded from a user specified file.
- When saving or loading the password information to/from a file, the data is encrypted and decrypted using AES encryption. This ensures that the website and password pairs are not stored in plain text.
- User must remember a 16 character length master key, which is used for encryption and decryption.
- Users can generate new passwords if they do not like their previously generated password.
