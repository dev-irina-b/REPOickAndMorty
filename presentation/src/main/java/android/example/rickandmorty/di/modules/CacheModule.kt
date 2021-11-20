package android.example.rickandmorty.di.modules

import android.example.data.cache.database.ApplicationDatabase
import android.example.data.cache.database.cache.CharacterCache
import android.example.data.cache.database.cache.EpisodeCache
import android.example.data.cache.database.cache.LocationCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideCharacterCache (database: ApplicationDatabase): CharacterCache =
        CharacterCache(database)

    @Provides
    @Singleton
    fun provideLocationCache (database: ApplicationDatabase): LocationCache =
        LocationCache(database)

    @Provides
    @Singleton
    fun provideEpisodeCache (database: ApplicationDatabase): EpisodeCache =
        EpisodeCache(database)
}