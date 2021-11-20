package android.example.data.network

class Network(private val api: Api) {

    suspend fun getCharacters(
        page: Int?,
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ) = api.getCharacters(page, name, status, species, type, gender)

    suspend fun getLocations(
        page: Int? = null,
        name: String? = null,
        type: String? = null,
        dimension: String? = null,
    ) = api.getLocations(page, name, type, dimension)

    suspend fun getEpisodes(
        page: Int? = null,
        name: String? = null,
        episode: String? = null,
    ) = api.getEpisodes(page, name, episode)

    suspend fun getCharacter(id: Int) = api.getCharacter(id)

    suspend fun getLocation(id: Int) = api.getLocation(id)

    suspend fun getEpisode(id: Int) = api.getEpisode(id)

    suspend fun getMultipleEpisodes(arrayIds: List<Int>) =
        api.getMultipleEpisodes(arrayIds)

    suspend fun getMultipleCharacters(arrayIds: List<Int>) =
        api.getMultipleCharacters(arrayIds)
}