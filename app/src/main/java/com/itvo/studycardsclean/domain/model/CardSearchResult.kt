package com.itvo.studycardsclean.domain.model

data class CardSearchResult(
    val id: Int,
    val term: String,
    val definition: String,
    val topicId: Int,
    val topicName: String,
    val subjectName: String,
    val subjectColor: String
)
