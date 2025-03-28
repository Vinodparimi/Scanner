package uk.ac.tees.mad.s3388461.data

import android.content.Context
import androidx.room.Room


object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "scanner_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
