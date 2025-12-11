package com.itvo.studycardsclean.data.repository

import com.itvo.studycardsclean.data.local.dao.StudyDao
import com.itvo.studycardsclean.data.local.datastore.DataStoreManager
import com.itvo.studycardsclean.data.mapper.toDomain
import com.itvo.studycardsclean.data.mapper.toEntity
import com.itvo.studycardsclean.domain.model.Card
import com.itvo.studycardsclean.domain.model.CardSearchResult
import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.domain.model.Topic
import com.itvo.studycardsclean.domain.repository.StudyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val dao: StudyDao,
    private val dataStoreManager: DataStoreManager
) : StudyRepository {

    // --- SUBJECTS ---
    override fun getAllSubjects(): Flow<List<Subject>> {
        // Mapeamos de Entity a Domain
        return dao.getAllSubjects().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getSubjectById(id: Int): Subject? {
        return dao.getSubjectById(id)?.toDomain()
    }

    override suspend fun insertSubject(subject: Subject) {
        dao.insertSubject(subject.toEntity())
    }

    override suspend fun updateSubject(subject: Subject) {
        dao.updateSubject(subject.toEntity())
    }

    override suspend fun deleteSubject(subject: Subject) {
        dao.deleteSubject(subject.toEntity())
    }

    // --- TOPICS ---
    override fun getTopicsBySubject(subjectId: Int): Flow<List<Topic>> {
        return dao.getTopicsBySubject(subjectId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertTopic(topic: Topic): Long {
        return dao.insertTopic(topic.toEntity())
    }

    override suspend fun deleteTopic(topic: Topic) {
        dao.deleteTopic(topic.toEntity())
    }

    // --- CARDS ---
    override fun getCardsByTopic(topicId: Int): Flow<List<Card>> {
        return dao.getCardsByTopic(topicId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getCardsByTopicList(topicId: Int): List<Card> {
        return dao.getCardsByTopicList(topicId).map { it.toDomain() }
    }

    override suspend fun insertCards(cards: List<Card>) {
        // Convertimos la lista de Domain a Entity
        val entities = cards.map { it.toEntity() }
        dao.insertCards(entities)
    }

    override suspend fun deleteCardsByTopic(topicId: Int) {
        dao.deleteCardsByTopic(topicId)
    }

    // --- BUSQUEDA ---
    override fun searchCardsGlobal(query: String): Flow<List<CardSearchResult>> {
        return dao.searchCardsGlobal(query)
    }
}