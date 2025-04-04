package com.example.learntrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.learntrack.data.local.converters.ColorListConverter
import com.example.learntrack.data.local.dao.SessionDao
import com.example.learntrack.data.local.dao.SubjectDao
import com.example.learntrack.data.local.dao.TaskDao
import com.example.learntrack.data.local.entity.SessionEntity
import com.example.learntrack.data.local.entity.SubjectEntity
import com.example.learntrack.data.local.entity.TaskEntity

@Database(entities = [SubjectEntity::class, SessionEntity::class, TaskEntity::class], version = 1)
@TypeConverters(ColorListConverter::class)
abstract class StudyDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
}