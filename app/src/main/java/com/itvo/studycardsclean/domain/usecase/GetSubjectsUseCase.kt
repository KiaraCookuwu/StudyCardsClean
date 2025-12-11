package com.itvo.studycardsclean.domain.usecase

import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.domain.repository.StudyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubjectsUseCase @Inject constructor(
    private val repository: StudyRepository
) {
    operator fun invoke(): Flow<List<Subject>> {
        return repository.getAllSubjects()
    }
}