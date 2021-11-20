package android.example.data.network

import android.example.data.entities.CharacterResponse
import android.example.data.entities.EpisodeResponse
import android.example.data.entities.LocationResponse
import android.example.domain.entities.Episode
import android.example.domain.entities.Location
import android.example.domain.entities.Character
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null
    ): Response<CharacterResponse>

    @GET("location")
    suspend fun getLocations(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("dimension") dimension: String? = null,
    ): Response<LocationResponse>

    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("episode") episode: String? = null,
    ): Response<EpisodeResponse>

    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): Response<Character>

    @GET("location/{id}")
    suspend fun getLocation(
        @Path("id") id: Int
    ): Response<Location>

    @GET("episode/{id}")
    suspend fun getEpisode(
        @Path("id") id: Int
    ): Response<Episode>

    @GET("episode/{array}")
    suspend fun getMultipleEpisodes(
        @Path("array") arrayIds: List<Int>
    ): Response<List<Episode>>

    @GET("character/{array}")
    suspend fun getMultipleCharacters(
        @Path("array") arrayIds: List<Int>
    ): Response<List<Character>>
}