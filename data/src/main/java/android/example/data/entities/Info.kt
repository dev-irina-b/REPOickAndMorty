package android.example.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "info")
@Parcelize
data class Info(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val count: Int = 671,
    val pages: Int = 34,
    val next: String?,
    val prev: String?
) : Parcelable