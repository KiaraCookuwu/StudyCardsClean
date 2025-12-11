package com.itvo.studycardsclean.domain.model

data class Subject(
    val id: Int = 0,
    val name: String,
    val colorHex: String,
    val imageUri: String? = null
)