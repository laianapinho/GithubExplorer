package br.edu.icomp.githubexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.icomp.githubexplorer.data.repository.GithubRepository
import br.edu.icomp.githubexplorer.domain.model.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RepoDetailUiState {
    data object Loading : RepoDetailUiState()
    data class Success(val repo: Repo) : RepoDetailUiState()
    data class Error(val message: String) : RepoDetailUiState()
}

class RepoDetailViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RepoDetailUiState>(RepoDetailUiState.Loading)
    val state: StateFlow<RepoDetailUiState> = _state

    fun load(owner: String, repo: String) {
        viewModelScope.launch {
            _state.value = RepoDetailUiState.Loading
            try {
                _state.value = RepoDetailUiState.Success(repository.getRepo(owner, repo))
            } catch (e: Exception) {
                _state.value = RepoDetailUiState.Error("Erro ao carregar detalhes.")
            }
        }
    }
}