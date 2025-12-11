package com.itvo.studycardsclean.data.local.dao

import androidx.room.*
import com.itvo.studycardsclean.data.local.entity.CardEntity
import com.itvo.studycardsclean.data.local.entity.SubjectEntity
import com.itvo.studycardsclean.data.local.entity.TopicEntity
import com.itvo.studycardsclean.domain.model.CardSearchResult
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    // --- SUBJECTS ---
    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun getSubjectById(id: Int): SubjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Update
    suspend fun updateSubject(subject: SubjectEntity)

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)

    // --- TOPICS ---
    @Query("SELECT * FROM topics WHERE subjectId = :subjectId")
    fun getTopicsBySubject(subjectId: Int): Flow<List<TopicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity): Long

    @Delete
    suspend fun deleteTopic(topic: TopicEntity)

    // --- CARDS ---
    @Query("SELECT * FROM cards WHERE topicId = :topicId")
    fun getCardsByTopic(topicId: Int): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE topicId = :topicId")
    suspend fun getCardsByTopicList(topicId: Int): List<CardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>)

    @Query("DELETE FROM cards WHERE topicId = :topicId")
    suspend fun deleteCardsByTopic(topicId: Int)

    // --- BUSQUEDA GLOBAL ---
    // Room es inteligente y puede mapear el resultado directo a la clase del Dominio
    // si los nombres de columnas coinciden.
    @Query("""
        SELECT c.id, c.term, c.definition, c.topicId, 
               t.name as topicName, s.name as subjectName, s.colorHex as subjectColor
        FROM cards c
        INNER JOIN topics t ON c.topicId = t.id
        INNER JOIN subjects s ON t.subjectId = s.id
        WHERE c.term LIKE '%' || :query || '%' 
           OR c.definition LIKE '%' || :query || '%'
    """)
    fun searchCardsGlobal(query: String): Flow<List<CardSearchResult>>
}