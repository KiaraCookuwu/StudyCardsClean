package com.itvo.studycardsclean.domain.model

data class Card(
    val id: Int = 0,
    val term: String,
    val definition: String,
    val imageUri: String? = null,
    val topicId: Int,
    val styleJson: String = ""
)