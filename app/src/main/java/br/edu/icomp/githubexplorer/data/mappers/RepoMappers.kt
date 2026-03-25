package br.edu.icomp.githubexplorer.data.mappers

import br.edu.icomp.githubexplorer.data.local.RepoEntity
import br.edu.icomp.githubexplorer.domain.model.Repo

fun RepoEntity.toDomain(): Repo = Repo(
    id = id,
    owner = owner,
    name = name,
    description = description,
    stars = stars,
    forks = forks,
    language = language,
    htmlUrl = htmlUrl,
    isFavorite = isFavorite
)