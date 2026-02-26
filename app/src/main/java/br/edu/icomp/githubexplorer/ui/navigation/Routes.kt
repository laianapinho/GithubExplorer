package br.edu.icomp.githubexplorer.ui.navigation

object Routes {
    const val REPO_LIST = "repo_list"
    const val REPO_DETAIL = "repo_detail"

    // rota com argumentos
    const val REPO_DETAIL_ROUTE = "repo_detail/{owner}/{repo}"

    fun repoDetail(owner: String, repo: String): String =
        "repo_detail/$owner/$repo"
}