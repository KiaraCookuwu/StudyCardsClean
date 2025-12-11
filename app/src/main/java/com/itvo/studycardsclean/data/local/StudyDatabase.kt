package com.itvo.studycardsclean.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itvo.studycardsclean.data.local.dao.StudyDao
import com.itvo.studycardsclean.data.local.entity.CardEntity
import com.itvo.studycardsclean.data.local.entity.SubjectEntity
import com.itvo.studycardsclean.data.local.entity.TopicEntity

@Database(
    entities = [SubjectEntity::class, TopicEntity::class, CardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StudyDatabase : RoomDatabase() {
    abstract fun studyDao(): StudyDao
}