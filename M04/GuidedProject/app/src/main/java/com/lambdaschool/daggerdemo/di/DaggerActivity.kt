package com.lambdaschool.daggerdemo.di

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lambdaschool.daggerdemo.DaggerDemoApplication
import com.lambdaschool.daggerdemo.R
import kotlinx.android.synthetic.main.activity_dagger.*
import java.util.*
import javax.inject.Inject

class DaggerActivity : AppCompatActivity() {

    // Don't really know what a `DateExampleContract` is, but it's anything that
    // we can implement using the interface; such as the classes created below
    @Inject
    lateinit var dateExample: DateExampleContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagger)

        (application as DaggerDemoApplication).dateComponent
            .inject(this)

        text_view.text = "${dateExample.getDate()}"
    }
}

// Would not normally put this here...
interface DateExampleContract {
    fun getDate(): Long
}

class DateExampleImpl: DateExampleContract {

    private val date: Long = Date().time

    override fun getDate(): Long {
        return date
    }
}

class DateExampleTest: DateExampleContract {
    override fun getDate(): Long {
        return 0L
    }
}