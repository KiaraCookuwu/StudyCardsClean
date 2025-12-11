package com.itvo.studycardsclean.domain.usecase
import com.itvo.studycardsclean.domain.repository.StudyRepository
import javax.inject.Inject

class SearchCardsUseCase @Inject constructor(private val repo: StudyRepository) {
    operator fun invoke(query: String) = repo.searchCardsGlobal(query)
}