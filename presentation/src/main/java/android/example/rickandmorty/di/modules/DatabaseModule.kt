package android.example.rickandmorty.di.modules

import android.app.Application
import android.example.data.cache.database.ApplicationDatabase
import androidx.room.Room
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    private const val DATABASE_NAME = "DATABASE"

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ApplicationDatabase =
        Room.databaseBuilder(app, ApplicationDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
}
