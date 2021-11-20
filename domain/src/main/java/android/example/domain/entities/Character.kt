package android.example.domain.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "characters")
data class Character(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @Embedded(prefix = "orig_")
    val origin: Origin,
    @Embedded(prefix = "loc_")
     val location: Origin,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
    var next: String?,
    var prev: String?
) : Parcelable