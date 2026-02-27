package br.edu.icomp.githubexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RepoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}