package br.edu.icomp.githubexplorer.data.repository

import br.edu.icomp.githubexplorer.data.remote.GithubApi
import br.edu.icomp.githubexplorer.data.remote.toDomain
import br.edu.icomp.githubexplorer.domain.model.Repo

class GithubRepository(
    private val api: GithubApi
) {
    suspend fun listRepos(username: String): List<Repo> =
        api.listRepos(username).map { it.toDomain() }

    suspend fun getRepo(owner: String, repo: String): Repo =
        api.getRepo(owner, repo).toDomain()
}