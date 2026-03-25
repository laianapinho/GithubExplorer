package br.edu.icomp.githubexplorer.di

import android.content.Context
import androidx.room.Room
import br.edu.icomp.githubexplorer.data.local.AppDatabase
import br.edu.icomp.githubexplorer.data.local.RepoDao
import br.edu.icomp.githubexplorer.data.remote.GithubApi
import br.edu.icomp.githubexplorer.data.remote.RetrofitProvider
import br.edu.icomp.githubexplorer.data.repository.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Retrofit
    @Provides
    @Singleton
    fun provideApi(): GithubApi = RetrofitProvider.api

    // Database
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "github_explorer.db"
        ).build()

    // DAO
    @Provides
    fun provideRepoDao(db: AppDatabase): RepoDao = db.repoDao()

    // Repository
    @Provides
    @Singleton
    fun provideRepository(
        api: GithubApi,
        dao: RepoDao
    ): GithubRepository = GithubRepository(api, dao)
}