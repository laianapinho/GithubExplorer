package br.edu.icomp.githubexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.icomp.githubexplorer.data.repository.GithubRepository
import br.edu.icomp.githubexplorer.domain.model.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RepoDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val repo: Repo? = null
)

class RepoDetailViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(RepoDetailUiState())
    val ui: StateFlow<RepoDetailUiState> = _ui

    fun load(owner: String, repo: String) {
        viewModelScope.launch {

            // 1️⃣ Observa o Room (Single Source of Truth)
            repository.observeRepo(owner, repo).collect { repoFromDb ->
                _ui.value = _ui.value.copy(
                    repo = repoFromDb,
                    isLoading = false
                )
            }
        }
    }

    fun toggleFavorite(id: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }
}