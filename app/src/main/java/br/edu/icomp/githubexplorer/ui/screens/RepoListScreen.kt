package br.edu.icomp.githubexplorer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.icomp.githubexplorer.data.remote.RetrofitProvider
import br.edu.icomp.githubexplorer.data.repository.GithubRepository
import br.edu.icomp.githubexplorer.ui.viewmodel.RepoListUiState
import br.edu.icomp.githubexplorer.ui.viewmodel.RepoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    onRepoClick: (owner: String, repo: String) -> Unit
) {
    var username by remember { mutableStateOf("octocat") }

    // ViewModel simples (Dia 2). Depois vai virar Hilt (Dia 6).
    val vm = remember {
        RepoListViewModel(
            repository = GithubRepository(RetrofitProvider.api)
        )
    }

    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("GitHub Explorer") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuário (ex: octocat)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { vm.search(username) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar")
            }

            when (val s = state) {
                RepoListUiState.Idle -> {
                    Text("Digite um usuário e clique em Buscar.")
                }

                RepoListUiState.Loading -> {
                    Text("Carregando...")
                }

                is RepoListUiState.Error -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(s.message)
                        Button(onClick = { vm.search(username) }) {
                            Text("Tentar novamente")
                        }
                    }
                }

                is RepoListUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(s.repos) { repo ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onRepoClick(repo.owner, repo.name) }
                                    .padding(12.dp)
                            ) {
                                Text("${repo.owner} / ${repo.name}")
                                repo.description?.let { Text(it) }
                            }
                        }
                    }
                }
            }
        }
    }
}