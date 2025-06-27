package com.example.lostpals.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// creeaza tabela "posts"
@Entity(
    tableName = "posts", foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE // daca userul este sters, se sterg si postarile
    )], indices = [Index("ownerId"), Index("postType"), Index("location"), Index("timestamp")]
)
// atributele entitatii
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerId: Long,
    val title: String,
    val description: String,
    val location: Location = Location.UNKNOWN,
    val objectType: ObjectType = ObjectType.OTHER,
    val postType: PostType = PostType.LOST,
    val photoUri: String? = null,
    val reward: Double? = null,
    val timestamp: Long = System.currentTimeMillis()
)