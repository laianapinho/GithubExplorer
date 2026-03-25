package br.edu.icomp.githubexplorer.domain.model

data class Repo(
    val id: Long,
    val owner: String,
    val name: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val htmlUrl: String,
    val isFavorite: Boolean = false
)