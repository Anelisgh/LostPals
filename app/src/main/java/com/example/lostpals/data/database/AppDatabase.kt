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

@Database(entities = [User::class, Post::class, Message::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun messageDao(): MessageDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lostpals_database"
                )
                    .addTypeConverter(Converters())

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

                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}