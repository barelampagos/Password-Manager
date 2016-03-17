# Password-Manager
Program that generates a hard to crack, easy to remember password given a website. Encrypts the user's website/password pairs to a text file of their choice using AES encryption.

## Features
- Generates a hard to brute-force crack password based on an [xkcd](https://xkcd.com/936/) comic.
- User is able to encrypt/decrypt the website and password pairs to a specified location by providing a master password. This master password must be 16 characters in order for the AES encryption/decryption to be valid.
- When website/password pairs are exported to a file, they are not stored in plain text.
- Users can generate new passwords if they do not like their previously generated password.
