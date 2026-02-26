package br.edu.icomp.githubexplorer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.icomp.githubexplorer.data.remote.RetrofitProvider
import br.edu.icomp.githubexplorer.data.repository.GithubRepository
import br.edu.icomp.githubexplorer.ui.viewmodel.RepoDetailUiState
import br.edu.icomp.githubexplorer.ui.viewmodel.RepoDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    owner: String,
    repo: String,
    onBack: () -> Unit
) {
    // ViewModel simples (Dia 2). Depois vira Hilt (Dia 6).
    val vm = remember {
        RepoDetailViewModel(
            repository = GithubRepository(RetrofitProvider.api)
        )
    }

    val state by vm.state.collectAsState()

    // Dispara a carga quando entrar na tela ou mudar owner/repo
    LaunchedEffect(owner, repo) {
        vm.load(owner, repo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (val s = state) {
                RepoDetailUiState.Loading -> {
                    Text("Carregando detalhes...")
                }

                is RepoDetailUiState.Error -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(s.message)
                        Button(onClick = { vm.load(owner, repo) }) {
                            Text("Tentar novamente")
                        }
                    }
                }

                is RepoDetailUiState.Success -> {
                    val r = s.repo
                    Text("${r.owner} / ${r.name}")
                    r.description?.let { Text(it) }
                    Text("⭐ Stars: ${r.stars}")
                    Text("🍴 Forks: ${r.forks}")
                    Text("🧠 Linguagem: ${r.language ?: "N/A"}")
                    Text("🔗 URL: ${r.htmlUrl}")
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar")
            }
        }
    }
}