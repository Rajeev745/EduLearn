package com.example.learntrack.di

import android.app.Application
import androidx.room.Room
import com.example.learntrack.data.local.StudyDatabase
import com.example.learntrack.utils.StudyConstants.Database.STUDY_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStudyDatabase(application: Application): StudyDatabase {
        return Room.databaseBuilder(
            application,
            StudyDatabase::class.java,
            STUDY_DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(studyDatabase: StudyDatabase) = studyDatabase.subjectDao()

    @Provides
    @Singleton
    fun provideSessionDao(studyDatabase: StudyDatabase) = studyDatabase.sessionDao()

    @Provides
    @Singleton
    fun provideTaskDao(studyDatabase: StudyDatabase) = studyDatabase.taskDao()

}