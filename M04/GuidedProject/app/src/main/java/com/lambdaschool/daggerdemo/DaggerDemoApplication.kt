package com.lambdaschool.daggerdemo

import android.app.Application
import com.lambdaschool.daggerdemo.di.DateComponent
import com.lambdaschool.daggerdemo.di.DateModule

class DaggerDemoApplication: Application() {

    lateinit var dateComponent: DateComponent

    override fun onCreate() {
        super.onCreate()

        dateComponent = DaggerDateComponent.builder()
            .dateModule(DateModule)
            .build()
    }
}