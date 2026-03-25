package br.edu.icomp.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.edu.icomp.githubexplorer.ui.navigation.AppNavGraph
import br.edu.icomp.githubexplorer.ui.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubExplorerTheme {
                AppNavGraph()
            }
        }
    }
}