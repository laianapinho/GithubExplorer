package br.edu.icomp.githubexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.icomp.githubexplorer.data.repository.GithubRepository
import br.edu.icomp.githubexplorer.domain.model.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RepoListUiState {
    data object Idle : RepoListUiState()
    data object Loading : RepoListUiState()
    data class Success(val repos: List<Repo>) : RepoListUiState()
    data class Error(val message: String) : RepoListUiState()
}

class RepoListViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RepoListUiState>(RepoListUiState.Idle)
    val state: StateFlow<RepoListUiState> = _state

    fun search(username: String) {
        if (username.isBlank()) {
            _state.value = RepoListUiState.Error("Digite um usuário (ex: octocat)")
            return
        }

        viewModelScope.launch {
            _state.value = RepoListUiState.Loading
            try {
                val repos = repository.listRepos(username.trim())
                _state.value = if (repos.isEmpty()) {
                    RepoListUiState.Error("Nenhum repositório encontrado.")
                } else {
                    RepoListUiState.Success(repos)
                }
            } catch (e: Exception) {
                _state.value = RepoListUiState.Error("Erro ao buscar repos. Verifique a internet.")
            }
        }
    }
}