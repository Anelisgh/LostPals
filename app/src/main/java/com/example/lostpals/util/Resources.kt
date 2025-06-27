package com.example.lostpals.util

// clasa folosita pentru a reprezenta rezultatul unei operatii
// care poate fi de succes sau eroare
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    // subclasa care reprezinta un rezultat de succes, contine datele returnate
    class Success<T>(data: T) : Resource<T>(data)

    // subclasa care reprezinta un rezultat de eroare
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    companion object {
        // functie pentru a crea un obiect de tip succes
        fun <T> success(data: T): Resource<T> = Success(data)

        // functie pentru a crea un obiect de tip eroare
        fun <T> error(message: String, data: T? = null): Resource<T> = Error(message, data)
    }
}
