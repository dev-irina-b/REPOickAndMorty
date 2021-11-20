package android.example.data.entities

import android.example.domain.entities.Episode
import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<Episode>
)