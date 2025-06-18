package com.example.lostpals.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val email: String,
    var password: String,
    val photoUri: String? = null
)
