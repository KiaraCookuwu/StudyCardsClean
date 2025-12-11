package com.itvo.studycardsclean.domain.usecase

import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.domain.repository.StudyRepository
import javax.inject.Inject

class DeleteSubjectUseCase @Inject constructor(
    private val repository: StudyRepository
) {
    suspend operator fun invoke(subject: Subject) {
        repository.deleteSubject(subject)
    }
}