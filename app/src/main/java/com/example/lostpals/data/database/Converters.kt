package com.example.lostpals.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.lostpals.data.entity.Location
import com.example.lostpals.data.entity.ObjectType
import com.example.lostpals.data.entity.PostType

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromPostType(value: PostType): String = value.name

    @TypeConverter
    fun toPostType(value: String): PostType = PostType.valueOf(value)

    @TypeConverter
    fun fromObjectType(value: ObjectType?): String? = value?.name

    @TypeConverter
    fun toObjectType(value: String?): ObjectType? = value?.let { ObjectType.valueOf(it) }

    @TypeConverter
    fun fromLocation(value: Location?): String? = value?.name

    @TypeConverter
    fun toLocation(value: String?): Location? = value?.let { Location.valueOf(it) }
}