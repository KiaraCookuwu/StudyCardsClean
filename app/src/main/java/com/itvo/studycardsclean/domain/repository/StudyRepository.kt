package com.itvo.studycardsclean.domain.repository

import com.itvo.studycardsclean.domain.model.Card
import com.itvo.studycardsclean.domain.model.CardSearchResult
import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.domain.model.Topic
import kotlinx.coroutines.flow.Flow

interface StudyRepository {
    // --- SUBJECTS ---
    fun getAllSubjects(): Flow<List<Subject>>
    suspend fun getSubjectById(id: Int): Subject?
    suspend fun insertSubject(subject: Subject)
    suspend fun updateSubject(subject: Subject)
    suspend fun deleteSubject(subject: Subject)

    // --- TOPICS ---
    fun getTopicsBySubject(subjectId: Int): Flow<List<Topic>>
    suspend fun insertTopic(topic: Topic): Long // Devuelve el ID nuevo
    suspend fun deleteTopic(topic: Topic)

    // --- CARDS ---
    fun getCardsByTopic(topicId: Int): Flow<List<Card>>
    suspend fun getCardsByTopicList(topicId: Int): List<Card> // Lista directa para editar
    suspend fun insertCards(cards: List<Card>)
    suspend fun deleteCardsByTopic(topicId: Int)

    // --- BUSQUEDA ---
    fun searchCardsGlobal(query: String): Flow<List<CardSearchResult>>
}