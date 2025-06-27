package com.example.lostpals.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lostpals.data.entity.User
@Dao
interface UserDao {

    // insereaza un user nou in baza de date (inregistrare)
    @Insert
    suspend fun insert(user: User): Long

    // adauga mai multi utilizatori deodata
    @Insert
    suspend fun insertAll(users: List<User>)

    // afiseaza toti utilizatorii
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>

    // gaseste un utilizator dupa id
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): User?

    // gaseste un utilizator dupa username
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    // logarea
    // verifica daca exista un user care are emailul si parola corecta
    @Query("SELECT * FROM users WHERE email = :email AND password = :passwordHash LIMIT 1")
    suspend fun loginUser(email: String, passwordHash: String): User?

    // verifica daca emailul exista
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun emailExists(email: String): Int

    // verifica daca usernameul exista
    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun usernameExists(username: String): Int

    // returneaza 1 daca emailul este folosit de altcineva (folosit la editare profil)
    @Query("SELECT COUNT(*) FROM users WHERE email = :email AND id != :excludeUserId")
    suspend fun emailExistsExcludingUser(email: String, excludeUserId: Long): Int

    // folosit la editareusername
    // verifica daca username-ul apartine altui user, nu celui curent
    @Query("SELECT COUNT(*) FROM users WHERE username = :username AND id != :excludeUserId")
    suspend fun usernameExistsExcludingUser(username: String, excludeUserId: Long): Int

    // efitarea datelor unui utilizator
    @Update
    suspend fun updateUser(user: User)

    // stergere cont
    // prin obiectul user
    @Delete
    suspend fun deleteUser(user: User)

    // stergere cont
    // prin id-ul userului
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
