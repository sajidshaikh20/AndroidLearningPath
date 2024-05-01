package com.base.hilt.ui.mvvm_clean.data.di

import com.base.hilt.ui.mvvm_clean.data.repositoriesImpl.SocialLoginRepoImplement
import com.base.hilt.ui.mvvm_clean.domain.repo.SocialLoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck



@DisableInstallInCheck
@Module
abstract class LoginUserRepositoryModule {


    @Binds
    abstract fun getLoginRepository(implement: SocialLoginRepoImplement): SocialLoginRepository
}
