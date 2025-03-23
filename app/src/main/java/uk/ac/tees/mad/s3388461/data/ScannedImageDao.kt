package uk.ac.tees.mad.s3388461.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ac.tees.mad.s3388461.data.ScannedImage

@Dao
interface ScannedImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: ScannedImage)

    @Query("SELECT * FROM scanned_images ORDER BY timestamp DESC")
    suspend fun getAll(): List<ScannedImage>
}