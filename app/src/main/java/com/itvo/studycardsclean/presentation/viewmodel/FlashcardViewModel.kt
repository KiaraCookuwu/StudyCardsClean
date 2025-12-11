package com.itvo.studycardsclean.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itvo.studycardsclean.domain.model.Card
import com.itvo.studycardsclean.domain.usecase.GetCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCardsUseCase: GetCardsUseCase
) : ViewModel() {

    private val topicId: Int = checkNotNull(savedStateHandle["topicId"])

    val uiState: StateFlow<FlashcardUiState> = getCardsUseCase(topicId)
        .map { FlashcardUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FlashcardUiState())
}

data class FlashcardUiState(val cards: List<Card> = emptyList())