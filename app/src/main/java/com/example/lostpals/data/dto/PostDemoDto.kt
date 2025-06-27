package com.example.lostpals.data.dto

// folosit pentru testarea cererilor HTTP externe (JSONPlaceholder)
// primeste API extern
data class PostDemoDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)