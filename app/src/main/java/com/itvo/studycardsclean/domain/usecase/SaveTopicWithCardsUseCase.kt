package com.itvo.studycardsclean.domain.usecase

import com.itvo.studycardsclean.domain.model.Card
import com.itvo.studycardsclean.domain.model.Topic
import com.itvo.studycardsclean.domain.repository.StudyRepository
import javax.inject.Inject

class SaveTopicWithCardsUseCase @Inject constructor(
    private val repository: StudyRepository
) {
    suspend operator fun invoke(
        topicId: Int,
        name: String,
        subjectId: Int,
        cards: List<Card>
    ) {
        // 1. Guardar/Actualizar el Tema
        val topic = Topic(id = topicId, name = name, subjectId = subjectId)
        val realTopicId = repository.insertTopic(topic).toInt()

        // 2. Si es edición, borramos las tarjetas viejas
        if (topicId != 0) {
            repository.deleteCardsByTopic(topicId)
        }

        // 3. Asignar el ID correcto y guardar nuevas
        val finalCards = cards.map { it.copy(topicId = realTopicId, id = 0) }
        repository.insertCards(finalCards)
    }
}