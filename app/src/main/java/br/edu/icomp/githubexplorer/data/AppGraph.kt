package br.edu.icomp.githubexplorer.data

import android.content.Context
import androidx.room.Room
import br.edu.icomp.githubexplorer.data.local.AppDatabase
import br.edu.icomp.githubexplorer.data.remote.RetrofitProvider
import br.edu.icomp.githubexplorer.data.repository.GithubRepository

object AppGraph {

    private lateinit var db: AppDatabase

    fun provideRepository(context: Context): GithubRepository {
        if (!::db.isInitialized) {
            db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "github_explorer.db"
            ).build()
        }

        return GithubRepository(
            api = RetrofitProvider.api,
            dao = db.repoDao()
        )
    }
}