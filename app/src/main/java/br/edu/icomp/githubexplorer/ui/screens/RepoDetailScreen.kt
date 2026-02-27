package br.edu.icomp.githubexplorer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.edu.icomp.githubexplorer.data.AppGraph
import br.edu.icomp.githubexplorer.ui.viewmodel.RepoDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    owner: String,
    repo: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val vm = remember {
        RepoDetailViewModel(
            repository = AppGraph.provideRepository(context)
        )
    }

    val ui by vm.ui.collectAsState()

    LaunchedEffect(owner, repo) {
        vm.load(owner, repo)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalhes") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            when {
                ui.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ui.errorMessage != null && ui.repo == null -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(ui.errorMessage!!)
                        Button(onClick = { vm.load(owner, repo) }) {
                            Text("Tentar novamente")
                        }
                    }
                }

                ui.repo != null -> {
                    val r = ui.repo!!
                    Text("${r.owner} / ${r.name}")
                    r.description?.let { Text(it) }
                    Text("⭐ Stars: ${r.stars}")
                    Text("🍴 Forks: ${r.forks}")
                    Text("🧠 Linguagem: ${r.language ?: "N/A"}")
                    Text("🔗 URL: ${r.htmlUrl}")

                    Button(
                        onClick = { vm.toggleFavorite(r.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Favoritar / Desfavoritar")
                    }
                }

                else -> {
                    Text("Sem dados para exibir.")
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