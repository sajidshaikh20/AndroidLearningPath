package com.base.hilt.ui.mvvm_clean.data.di

import com.base.hilt.ui.mvvm_clean.data.repositoriesImpl.GetUserRepoImplement
import com.base.hilt.ui.mvvm_clean.domain.repo.getUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck

@DisableInstallInCheck
@Module
abstract class GetUserDataRepositoryModule {

    @Binds
    abstract fun getUserRepository(impl: GetUserRepoImplement): getUserRepository
}
