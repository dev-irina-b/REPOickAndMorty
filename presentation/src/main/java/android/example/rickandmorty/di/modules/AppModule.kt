package android.example.rickandmorty.di.modules

import android.example.data.cache.repository.CharacterRepository
import android.example.data.cache.repository.EpisodeRepository
import android.example.data.cache.repository.LocationRepository
import android.example.domain.interactors.*
import android.example.domain.repository.ICharacterRepository
import android.example.domain.repository.IEpisodeRepository
import android.example.domain.repository.ILocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [AppModule.InnerModule::class])
@InstallIn(SingletonComponent::class)
object AppModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface InnerModule {

        @Binds
        fun provideCharacterRepository(repository: CharacterRepository): ICharacterRepository

        @Binds
        fun provideLocationRepository(repository: LocationRepository): ILocationRepository

        @Binds
        fun provideEpisodeRepository(repository: EpisodeRepository): IEpisodeRepository

        @Binds
        fun provideCharactersInteractor(charactersInteractor: CharactersInteractor): ICharactersInteractor

        @Binds
        fun provideLocationsInteractor(locationsInteractor: LocationsInteractor): ILocationsInteractor

        @Binds
        fun provideEpisodesInteractor(episodesInteractor: EpisodesInteractor): IEpisodesInteractor
    }
}