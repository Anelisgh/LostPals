package com.example.lostpals.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.lostpals.data.entity.Location
import com.example.lostpals.data.entity.ObjectType
import com.example.lostpals.data.entity.PostType

// clasa care ajuta Room sa transforme tipuri speciale (enum, date) in string, long, etc
// ca acestea sa poata fi salvate si citite din bd
@ProvidedTypeConverter
class Converters {

    // posttype -> string
    @TypeConverter
    fun fromPostType(value: PostType): String = value.name

    // string -> posttype
    @TypeConverter
    fun toPostType(value: String): PostType = PostType.valueOf(value)

    // objecttype -> string
    @TypeConverter
    fun fromObjectType(value: ObjectType?): String? = value?.name

    // string -> objecttype
    @TypeConverter
    fun toObjectType(value: String?): ObjectType? = value?.let { ObjectType.valueOf(it) }

    // location -> string
    @TypeConverter
    fun fromLocation(value: Location?): String? = value?.name

    // string -> location
    @TypeConverter
    fun toLocation(value: String?): Location? = value?.let { Location.valueOf(it) }
}