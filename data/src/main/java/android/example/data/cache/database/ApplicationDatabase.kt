package android.example.data.cache.database

import android.example.data.cache.database.dao.*
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.example.data.entities.Info
import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import android.example.domain.entities.Location

@TypeConverters(TypeConverter::class)
@Database(entities = [Character::class, Info::class, Location::class,
    Episode::class], version = 12)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterInfoDao(): CharacterInfoDao
    abstract fun locationDao(): LocationDao
    abstract fun locationInfoDao(): LocationInfoDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun episodeInfoDao(): EpisodeInfoDao
}