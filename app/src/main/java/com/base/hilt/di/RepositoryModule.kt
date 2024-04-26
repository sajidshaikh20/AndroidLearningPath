package com.base.hilt.di

import com.base.hilt.network.ApiInterface
import com.base.hilt.ui.getData.data.repositoriesImpl.MyProfileRepImplement
import com.base.hilt.ui.getData.domain.repo.MyProfileRepository
import com.base.hilt.ui.home.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideHomeRepository(apiInterface: ApiInterface) = HomeRepository(apiInterface)

    @Provides
    @ViewModelScoped
    fun provideProfileRepository(apiInterface: ApiInterface) = MyProfileRepImplement(apiInterface)

}