package com.itvo.studycardsclean.domain.usecase
import com.itvo.studycardsclean.domain.repository.StudyRepository
import javax.inject.Inject

class GetSubjectDetailsUseCase @Inject constructor(private val repo: StudyRepository) {
    suspend operator fun invoke(id: Int) = repo.getSubjectById(id)
}