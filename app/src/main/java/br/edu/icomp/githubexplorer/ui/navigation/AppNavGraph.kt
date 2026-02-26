package br.edu.icomp.githubexplorer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.icomp.githubexplorer.ui.screens.RepoDetailScreen
import br.edu.icomp.githubexplorer.ui.screens.RepoListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.REPO_LIST
    ) {
        composable(Routes.REPO_LIST) {
            RepoListScreen(
                onRepoClick = { owner, repo ->
                    navController.navigate(Routes.repoDetail(owner, repo))
                }
            )
        }

        composable(
            route = Routes.REPO_DETAIL_ROUTE,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner").orEmpty()
            val repo = backStackEntry.arguments?.getString("repo").orEmpty()

            RepoDetailScreen(
                owner = owner,
                repo = repo,
                onBack = { navController.popBackStack() }
            )
        }
    }
}