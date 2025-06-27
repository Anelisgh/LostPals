package com.example.lostpals.data.dto

import com.example.lostpals.data.entity.Location
import com.example.lostpals.data.entity.ObjectType

// transmite filtrele alese de utilizator
// folosit pentru filtrarea postarilor din homepage
data class PostFilterDto(
    val location: Location? = null,
    val objectType: ObjectType? = null
)
