package br.edu.icomp.githubexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.icomp.githubexplorer.data.repository.GithubRepository
import br.edu.icomp.githubexplorer.domain.model.Repo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RepoListUiState(
    val username: String = "",
    val isSyncing: Boolean = false,
    val errorMessage: String? = null,
    val repos: List<Repo> = emptyList(),
    val hasSearched: Boolean = false
)

class RepoListViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(RepoListUiState())
    val ui: StateFlow<RepoListUiState> = _ui

    private var currentUsername: String = ""
    private var observeJob: Job? = null

    fun onUsernameChange(text: String) {
        _ui.value = _ui.value.copy(username = text)
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

        // ✅ marca que já tentou buscar (pra EmptyView aparecer)
        _ui.value = _ui.value.copy(hasSearched = true)

        // ✅ Trocar usuário = trocar observação do Room (cancela a anterior)
        if (username != currentUsername) {
            currentUsername = username
            startObserving(username)
        }

        // ✅ Sincroniza API -> Room (Room atualiza UI sozinho)
        viewModelScope.launch {
            _ui.value = _ui.value.copy(isSyncing = true, errorMessage = null)
            try {
                repository.syncRepos(username)
            } catch (e: Exception) {
                // se tiver cache, a lista ainda aparece; se não tiver, aparece erro/empty
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
                _ui.value = _ui.value.copy(repos = repos)
            }
        }
    }
}