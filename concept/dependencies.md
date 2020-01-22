# Dependencies
Here you can find all possible libraries, tools and all other dependencies the app might use.

### Database
All stuff related to the databases:
- [Room Database](https://developer.android.com/training/data-storage/room) --> The interface for storing the data in databases (SQLite)
- [SQLCipher](https://github.com/sqlcipher/android-database-sqlcipher) --> An encryption library for SQLite databases. Works with **Room**

### Encryption & Security
- [Android Keystore](https://developer.android.com/training/articles/keystore) --> The Keystore can work as the place where the encryption key for the password is saved. The database gets encrypted with the decrypted user password (or a hash of that).
