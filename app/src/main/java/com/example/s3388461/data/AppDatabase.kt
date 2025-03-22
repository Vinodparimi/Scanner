package com.example.s3388461.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScannedImage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scannedImageDao(): ScannedImageDao
}