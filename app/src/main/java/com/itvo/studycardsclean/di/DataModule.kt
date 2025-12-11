package com.itvo.studycardsclean.di

import android.content.Context
import androidx.room.Room
import com.itvo.studycardsclean.data.local.StudyDatabase
import com.itvo.studycardsclean.data.local.dao.StudyDao
import com.itvo.studycardsclean.data.local.datastore.DataStoreManager
import com.itvo.studycardsclean.data.repository.StudyRepositoryImpl
import com.itvo.studycardsclean.domain.repository.StudyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Esto hace que vivan toda la vida de la app (Singleton)
object DataModule {

    // 1. Proveer la Base de Datos (Room)
    @Provides
    @Singleton
    fun provideStudyDatabase(@ApplicationContext context: Context): StudyDatabase {
        return Room.databaseBuilder(
            context,
            StudyDatabase::class.java,
            "study_database_clean" // Nombre del archivo de la BD
        )
            .fallbackToDestructiveMigration() // Borra y recrea si cambias la estructura (útil en desarrollo)
            .build()
    }

    // 2. Proveer el DAO (Lo sacamos de la base de datos)
    @Provides
    @Singleton
    fun provideStudyDao(database: StudyDatabase): StudyDao {
        return database.studyDao()
    }

    // 3. Proveer el DataStoreManager
    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    // 4. Proveer el Repositorio (La conexión clave)
    // Cuando alguien pida "StudyRepository" (interfaz), Hilt le dará "StudyRepositoryImpl"
    @Provides
    @Singleton
    fun provideStudyRepository(
        dao: StudyDao,
        dataStoreManager: DataStoreManager
    ): StudyRepository {
        return StudyRepositoryImpl(dao, dataStoreManager)
    }
}