package com.itvo.studycardsclean.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itvo.studycardsclean.domain.model.CardSearchResult
import com.itvo.studycardsclean.domain.model.Subject
import com.itvo.studycardsclean.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val addSubjectUseCase: AddSubjectUseCase,
    private val deleteSubjectUseCase: DeleteSubjectUseCase,
    private val searchCardsUseCase: SearchCardsUseCase
) : ViewModel() {

    val subjectsList: StateFlow<List<Subject>> = getSubjectsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<CardSearchResult>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList()) else searchCardsUseCase(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }

    fun saveSubject(id: Int = 0, name: String, color: String, imageUri: String?) {
        viewModelScope.launch {
            addSubjectUseCase(Subject(id, name, color, imageUri))
        }
    }

    fun deleteSubject(subject: Subject) {
        viewModelScope.launch { deleteSubjectUseCase(subject) }
    }
}