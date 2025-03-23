package uk.ac.tees.mad.s3388461.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanned_images")
data class ScannedImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val timestamp: Long
)
