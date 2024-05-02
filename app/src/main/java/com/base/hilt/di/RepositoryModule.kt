package com.base.hilt.di

import com.base.hilt.network.ApiInterface
import com.base.hilt.ui.mvvm_clean.data.remote.GetUserProfileApi
import com.base.hilt.ui.mvvm_clean.data.remote.LoginInterface
import com.base.hilt.ui.mvvm_clean.data.repositoriesImpl.GetUserRepoImplement
import com.base.hilt.ui.mvvm_clean.data.repositoriesImpl.SocialLoginRepoImplement
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
    fun provideProfileRepository(getUserapiInterface: GetUserProfileApi) = GetUserRepoImplement(getUserapiInterface)

    @Provides
    @ViewModelScoped
    fun provideLoginRepository(loginInterface: LoginInterface) = SocialLoginRepoImplement(loginInterface)


}