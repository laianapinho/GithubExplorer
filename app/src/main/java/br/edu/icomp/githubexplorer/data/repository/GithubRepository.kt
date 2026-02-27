package br.edu.icomp.githubexplorer.data.repository

import br.edu.icomp.githubexplorer.data.local.RepoDao
import br.edu.icomp.githubexplorer.data.mappers.toDomain
import br.edu.icomp.githubexplorer.data.mappers.toEntity
import br.edu.icomp.githubexplorer.data.remote.GithubApi
import br.edu.icomp.githubexplorer.domain.model.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class GithubRepository(
    private val api: GithubApi,
    private val dao: RepoDao
) {
    // ✅ Single source of truth: UI observa o Room
    fun observeRepos(username: String): Flow<List<Repo>> =
        dao.observeRepos(username).map { list -> list.map { it.toDomain() } }

    fun observeRepo(owner: String, repo: String): Flow<Repo?> =
        dao.observeRepo(owner, repo).map { it?.toDomain() }

    // ✅ Sincroniza API -> Room (com erros bem definidos)
    suspend fun syncRepos(username: String) {
        try {
            val remote = api.listRepos(username)
            val entities = remote.map { it.toEntity(username) }

            // estratégia simples: limpa e salva
            dao.deleteByUsername(username)
            dao.upsertAll(entities)
        } catch (e: IOException) {
            // sem internet / timeout / falha de rede
            throw Exception("Sem conexão com a internet.")
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> throw Exception("Usuário não encontrado.")
                403 -> throw Exception("Limite da API do GitHub atingido. Tente mais tarde.")
                else -> throw Exception("Erro do servidor (${e.code()}).")
            }
        }
    }

    suspend fun toggleFavorite(id: Long) {
        dao.toggleFavorite(id)
    }
}