package br.edu.icomp.githubexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {

    // ✅ Favoritos primeiro
    @Query("""
    SELECT * FROM repos 
    WHERE username = :username 
    ORDER BY isFavorite DESC, stars DESC
""")
    fun observeRepos(username: String): Flow<List<RepoEntity>>

    @Query("SELECT * FROM repos WHERE owner = :owner AND name = :repo LIMIT 1")
    fun observeRepo(owner: String, repo: String): Flow<RepoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<RepoEntity>)

    @Query("DELETE FROM repos WHERE username = :username")
    suspend fun deleteByUsername(username: String)

    // ✅ Pegar favoritos atuais pra preservar na sincronização
    @Query("SELECT id FROM repos WHERE username = :username AND isFavorite = 1")
    suspend fun getFavoriteIds(username: String): List<Long>

    @Query("""
        UPDATE repos 
        SET isFavorite = CASE WHEN isFavorite = 1 THEN 0 ELSE 1 END
        WHERE id = :id
    """)
    suspend fun toggleFavorite(id: Long)


}