package br.edu.icomp.githubexplorer.data.remote

import com.google.gson.annotations.SerializedName
import br.edu.icomp.githubexplorer.domain.model.Repo

data class RepoDto(
    val id: Long,
    val name: String,
    val description: String?,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("forks_count") val forksCount: Int,
    val language: String?,
    @SerializedName("html_url") val htmlUrl: String,
    val owner: OwnerDto
)

data class OwnerDto(
    val login: String
)

fun RepoDto.toDomain(): Repo = Repo(
    id = id,
    owner = owner.login,
    name = name,
    description = description,
    stars = stargazersCount,
    forks = forksCount,
    language = language,
    htmlUrl = htmlUrl
)