package br.edu.icomp.githubexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.icomp.githubexplorer.data.repository.GithubRepository
import br.edu.icomp.githubexplorer.domain.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RepoListUiState(
    val username: String = "",
    val isSyncing: Boolean = false,
    val errorMessage: String? = null,
    val repos: List<Repo> = emptyList(),
    val hasSearched: Boolean = false,
    val onlyFavorites: Boolean = false
)

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(RepoListUiState())
    val ui: StateFlow<RepoListUiState> = _ui

    private var currentUsername: String = ""
    private var observeJob: Job? = null

    fun onUsernameChange(text: String) {
        _ui.value = _ui.value.copy(username = text)
    }

    fun toggleOnlyFavorites() {
        val newValue = !_ui.value.onlyFavorites
        _ui.value = _ui.value.copy(onlyFavorites = newValue)
    }

    fun toggleFavorite(id: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }

    fun searchAndSync() {
        val username = _ui.value.username.trim()
        if (username.isBlank()) {
            _ui.value = _ui.value.copy(
                errorMessage = "Digite um usuário (ex: octocat)",
                hasSearched = true
            )
            return
        }

        _ui.value = _ui.value.copy(hasSearched = true)

        if (username != currentUsername) {
            currentUsername = username
            startObserving(username)
        }

        viewModelScope.launch {
            _ui.value = _ui.value.copy(isSyncing = true, errorMessage = null)
            try {
                repository.syncRepos(username)
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(errorMessage = e.message ?: "Falha ao sincronizar.")
            } finally {
                _ui.value = _ui.value.copy(isSyncing = false)
            }
        }
    }

    private fun startObserving(username: String) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            repository.observeRepos(username).collect { repos ->
                val filtered = if (_ui.value.onlyFavorites) {
                    repos.filter { it.isFavorite }
                } else {
                    repos
                }

                _ui.value = _ui.value.copy(repos = filtered)
            }
        }
    }
}