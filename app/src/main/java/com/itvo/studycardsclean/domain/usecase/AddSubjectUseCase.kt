package com.itvo.studycardsclean.domain.usecase

import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.domain.repository.StudyRepository
import javax.inject.Inject

class AddSubjectUseCase @Inject constructor(
    private val repository: StudyRepository
) {
    // La lógica de negocio decide si es insertar o actualizar según el ID
    suspend operator fun invoke(subject: Subject) {
        if (subject.id == 0) {
            repository.insertSubject(subject)
        } else {
            repository.updateSubject(subject)
        }
    }
}