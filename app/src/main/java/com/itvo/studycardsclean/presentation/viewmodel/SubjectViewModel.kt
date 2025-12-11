package com.itvo.studycardsclean.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itvo.studycardsclean.domain.model.Card
import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.domain.model.Topic
import com.itvo.studycardsclean.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getSubjectDetailsUseCase: GetSubjectDetailsUseCase,
    private val saveTopicWithCardsUseCase: SaveTopicWithCardsUseCase,
    private val deleteTopicUseCase: DeleteTopicUseCase,
    private val getCardsForEditUseCase: GetCardsForEditUseCase
) : ViewModel() {

    private val subjectId: Int = checkNotNull(savedStateHandle["subjectId"])

    // CORRECCIÓN 1: Esta variable privada va PRIMERO para evitar el error de "must be initialized"
    private val _currentSubject = MutableStateFlow<Subject?>(null)

    // CORRECCIÓN 2: Luego va el uiState que depende de la de arriba
    val uiState: StateFlow<SubjectUiState> = combine(
        getTopicsUseCase(subjectId),
        _currentSubject
    ) { topics, subject ->
        SubjectUiState(subject, topics)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SubjectUiState())

    init {
        viewModelScope.launch {
            _currentSubject.value = getSubjectDetailsUseCase(subjectId)
        }
    }

    // CORRECCIÓN 3: El parámetro se llama 'cards' para coincidir con la pantalla
    fun saveTopicWithCards(topicId: Int, topicName: String, cards: List<Card>) {
        viewModelScope.launch {
            saveTopicWithCardsUseCase(topicId, topicName, subjectId, cards)
        }
    }

    // CORRECCIÓN 4: Agregamos la función getTopicDetails que faltaba
    fun getTopicDetails(topicId: Int, onResult: (List<Card>) -> Unit) {
        viewModelScope.launch {
            val cards = getCardsForEditUseCase(topicId)
            onResult(cards)
        }
    }

    fun deleteTopic(topic: Topic) {
        viewModelScope.launch { deleteTopicUseCase(topic) }
    }
}

data class SubjectUiState(val currentSubject: Subject? = null, val topics: List<Topic> = emptyList())