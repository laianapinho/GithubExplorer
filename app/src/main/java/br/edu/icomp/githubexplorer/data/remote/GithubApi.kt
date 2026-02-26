package br.edu.icomp.githubexplorer.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    // Lista repos de um usuário
    @GET("users/{username}/repos")
    suspend fun listRepos(@Path("username") username: String): List<RepoDto>

    // Detalhes de um repo específico
    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(@Path("owner") owner: String, @Path("repo") repo: String): RepoDto
}