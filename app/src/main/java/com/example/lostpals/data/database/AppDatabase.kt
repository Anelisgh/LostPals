package com.example.lostpals.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lostpals.data.dao.MessageDao
import com.example.lostpals.data.dao.PostDao
import com.example.lostpals.data.dao.UserDao
import com.example.lostpals.data.entity.Message
import com.example.lostpals.data.entity.Post
import com.example.lostpals.data.entity.User

// spune ca aceasta clasa este o baza de date Room
// defineste entitatile
@Database(entities = [User::class, Post::class, Message::class], version = 2, exportSchema = false)

// ajuta la transformare (ex enum, data, location, posttype)
@TypeConverters(Converters::class)
// declarare metode pentru accesarea DAO-urilor
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun messageDao(): MessageDao

    // singleton
    // o singura instanta de bd care este folosita in aplicatie
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // crearea bazei de date
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) { // daca exista deja, o returneaza
                val instance = Room.databaseBuilder( // creeaza bd SQLite cu numele "lostpals_database"
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lostpals_database"
                )
                    .addTypeConverter(Converters()) // conecteaza bd cu metodele de conversie

                    // se apeleaza doar la prima creare a bd
                    // introduce un post cu id 0
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            db.execSQL(
                                """
                                INSERT INTO posts
                                    (id, title, description, photoUri, ownerId, timestamp, location, objectType, postType)
                                VALUES (0, 'general', '', NULL, 0, 0, 'UNKNOWN', 'OTHER', 'LOST')
                                """.trimIndent()
                            )
                        }
                    })

                    // daca se schimba bd fara sa se defineasca manual cum se face migrarea => aceasta se recreeaza sau se sterge
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}