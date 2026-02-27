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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.edu.icomp.githubexplorer.data.AppGraph
import br.edu.icomp.githubexplorer.ui.viewmodel.RepoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    onRepoClick: (owner: String, repo: String) -> Unit
) {
    val context = LocalContext.current

    val vm = remember {
        RepoListViewModel(
            repository = AppGraph.provideRepository(context)
        )
    }

    val ui by vm.ui.collectAsState()

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
                value = ui.username,
                onValueChange = vm::onUsernameChange,
                label = { Text("Usuário (ex: octocat)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { vm.searchAndSync() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !ui.isSyncing
            ) {
                Text(if (ui.isSyncing) "Sincronizando..." else "Buscar")
            }

            // ✅ Conteúdo principal (estados)
            when {
                ui.isSyncing && ui.repos.isEmpty() -> {
                    LoadingView()
                }

                ui.errorMessage != null && ui.repos.isEmpty() -> {
                    ErrorView(
                        message = ui.errorMessage!!,
                        onRetry = { vm.searchAndSync() }
                    )
                }

                ui.hasSearched && ui.repos.isEmpty() -> {
                    EmptyView()
                }

                ui.repos.isNotEmpty() -> {
                    // Se tiver erro mas tiver cache, mostra o erro como aviso
                    ui.errorMessage?.let { Text(it) }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(ui.repos) { repo ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onRepoClick(repo.owner, repo.name) }
                                    .padding(12.dp)
                            ) {
                                Text("${repo.owner} / ${repo.name}")
                                repo.description?.let { Text(it) }
                                Text("⭐ ${repo.stars}   🍴 ${repo.forks}   🧠 ${repo.language ?: "N/A"}")
                            }
                        }
                    }
                }

                else -> {
                    Text("Digite um usuário e clique em Buscar.")
                }
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(message)
        Button(onClick = onRetry) {
            Text("Tentar novamente")
        }
    }
}

@Composable
private fun EmptyView() {
    Text("Nenhum repositório encontrado.")
}