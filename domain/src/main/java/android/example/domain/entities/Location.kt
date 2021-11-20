package android.example.domain.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "locations")
data class Location(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    var next: String?,
    var prev: String?,
    val residents: List<String>,
    ) : Parcelable