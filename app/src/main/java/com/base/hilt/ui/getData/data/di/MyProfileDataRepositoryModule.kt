package com.base.hilt.ui.getData.data.di

import com.base.hilt.ui.getData.data.repositoriesImpl.MyProfileRepImplement
import com.base.hilt.ui.getData.domain.repo.MyProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck

@DisableInstallInCheck
@Module
abstract class MyProfileDataRepositoryModule {

    @Binds
    abstract fun myProfileRepository(impl: MyProfileRepImplement): MyProfileRepository
}
