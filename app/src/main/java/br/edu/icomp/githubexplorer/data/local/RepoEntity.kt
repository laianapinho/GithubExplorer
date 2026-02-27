package br.edu.icomp.githubexplorer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class RepoEntity(
    @PrimaryKey val id: Long,
    val username: String,          // o usuário que foi buscado (octocat)
    val owner: String,
    val name: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val htmlUrl: String,
    val isFavorite: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)