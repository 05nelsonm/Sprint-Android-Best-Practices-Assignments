package com.lambdaschool.daggerdemo.di

import dagger.Module
import dagger.Provides

@Module
object DateModule {

    @Provides
    fun providesDateExample(): DateExampleContract {
        return DateExampleImpl()
    }
}