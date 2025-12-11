package com.itvo.studycardsclean.domain.usecase
import com.itvo.studycardsclean.domain.repository.StudyRepository
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(private val repo: StudyRepository) {
    operator fun invoke(topicId: Int) = repo.getCardsByTopic(topicId)
}

class GetCardsForEditUseCase @Inject constructor(private val repo: StudyRepository) {
    suspend operator fun invoke(topicId: Int) = repo.getCardsByTopicList(topicId)
}