package com.itvo.studycardsclean.domain.usecase
import com.itvo.studycardsclean.domain.model.Topic
import com.itvo.studycardsclean.domain.repository.StudyRepository
import javax.inject.Inject

class GetTopicsUseCase @Inject constructor(private val repo: StudyRepository) {
    operator fun invoke(subjectId: Int) = repo.getTopicsBySubject(subjectId)
}

class DeleteTopicUseCase @Inject constructor(private val repo: StudyRepository) {
    suspend operator fun invoke(topic: Topic) = repo.deleteTopic(topic)
}

class GetTopicDetailsUseCase @Inject constructor(private val repo: StudyRepository) {
    // Necesitas agregar getTopicById en tu Repository si no lo tienes,
    // o puedes pasarlo como parámetro si ya lo tienes en memoria.
    // Por simplicidad, asumiremos que usas el ID para buscar tarjetas.
}