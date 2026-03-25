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

data class RepoDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val repo: Repo? = null
)

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(RepoDetailUiState())
    val ui: StateFlow<RepoDetailUiState> = _ui

    private var observeJob: Job? = null

    fun load(owner: String, repo: String) {
        observeJob?.cancel()
        _ui.value = RepoDetailUiState(isLoading = true)

        observeJob = viewModelScope.launch {
            repository.observeRepo(owner, repo).collect { repoFromDb ->
                _ui.value = _ui.value.copy(
                    repo = repoFromDb,
                    isLoading = false,
                    errorMessage = if (repoFromDb == null) {
                        "Sem dados no cache para este repositório."
                    } else {
                        null
                    }
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