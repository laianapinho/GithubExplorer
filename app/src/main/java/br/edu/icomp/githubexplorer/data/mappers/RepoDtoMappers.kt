package br.edu.icomp.githubexplorer.data.mappers

import br.edu.icomp.githubexplorer.data.local.RepoEntity
import br.edu.icomp.githubexplorer.data.remote.RepoDto

fun RepoDto.toEntity(username: String): RepoEntity = RepoEntity(
    id = id,
    username = username,
    owner = owner.login,
    name = name,
    description = description,
    stars = stargazersCount,
    forks = forksCount,
    language = language,
    htmlUrl = htmlUrl
)