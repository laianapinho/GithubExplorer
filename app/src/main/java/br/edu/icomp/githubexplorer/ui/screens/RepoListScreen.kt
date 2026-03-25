package br.edu.icomp.githubexplorer.ui.screens
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import br.edu.icomp.githubexplorer.ui.components.EmptyView
import br.edu.icomp.githubexplorer.ui.components.ErrorView
import br.edu.icomp.githubexplorer.ui.components.LoadingView
import br.edu.icomp.githubexplorer.ui.viewmodel.RepoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    onRepoClick: (owner: String, repo: String) -> Unit
) {
    val context = LocalContext.current

    val vm: RepoListViewModel = hiltViewModel()

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { vm.searchAndSync() },
                    enabled = !ui.isSyncing,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (ui.isSyncing) "Sincronizando..." else "Buscar")
                }

                Button(
                    onClick = { vm.toggleOnlyFavorites() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (ui.onlyFavorites) "Favoritos" else "Todos")
                }
            }

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
                    // Se tiver erro mas tiver cache, mostra como aviso
                    ui.errorMessage?.let { Text(it) }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(ui.repos) { repo ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { onRepoClick(repo.owner, repo.name) }
                                    ) {
                                        Text("${repo.owner} / ${repo.name}")
                                        repo.description?.let { Text(it) }
                                        Text("⭐ ${repo.stars}   🍴 ${repo.forks}   🧠 ${repo.language ?: "N/A"}")
                                    }

                                    IconButton(onClick = { vm.toggleFavorite(repo.id) }) {
                                        Icon(
                                            imageVector = if (repo.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                            contentDescription = "Favoritar"
                                        )
                                    }
                                }
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