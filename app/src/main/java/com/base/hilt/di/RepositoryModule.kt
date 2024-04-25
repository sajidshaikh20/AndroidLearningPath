package com.base.hilt.di

import com.base.hilt.network.ApiInterface
import com.base.hilt.ui.getData.domain.repository.GetDataRepository
import com.base.hilt.ui.getData.presentation.GetDataRepo
import com.base.hilt.ui.home.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideHomeRepository(apiInterface: ApiInterface) = HomeRepository(apiInterface)

    @Provides
    @ViewModelScoped
    fun provideUserDataRepository(apiInterface: ApiInterface) = GetDataRepo(apiInterface)
}