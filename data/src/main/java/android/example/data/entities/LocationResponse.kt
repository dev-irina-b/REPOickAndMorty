package android.example.data.entities

import android.example.domain.entities.Location
import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<Location>
)