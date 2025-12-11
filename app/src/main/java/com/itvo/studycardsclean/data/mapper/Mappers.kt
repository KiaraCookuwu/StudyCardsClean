package com.itvo.studycardsclean.data.mapper

import com.itvo.studycardsclean.data.local.entity.CardEntity
import com.itvo.studycardsclean.data.local.entity.SubjectEntity
import com.itvo.studycardsclean.data.local.entity.TopicEntity
import com.itvo.studycardsclean.domain.model.Card
import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.domain.model.Topic

// --- SUBJECT ---
fun SubjectEntity.toDomain() = Subject(id, name, colorHex, imageUri)
fun Subject.toEntity() = SubjectEntity(id, name, colorHex, imageUri)

// --- TOPIC ---
fun TopicEntity.toDomain() = Topic(id, name, subjectId)
fun Topic.toEntity() = TopicEntity(id, name, subjectId)

// --- CARD ---
fun CardEntity.toDomain() = Card(id, term, definition, imageUri, topicId, styleJson)
fun Card.toEntity() = CardEntity(id, term, definition, imageUri, topicId, styleJson)