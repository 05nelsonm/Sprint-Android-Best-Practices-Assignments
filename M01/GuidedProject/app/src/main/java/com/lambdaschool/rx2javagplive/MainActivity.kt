package com.lambdaschool.rx2javagplive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var disposable: Disposable

    //private val obsJust: Observable<Int> = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    //private val obsJust: Observable<Int> = Observable.fromIterable(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

    private val obsCreate: Observable<Int> = Observable.create { emitter ->
        for (i in 1..10) {
            Thread.sleep(1000)
            emitter.onNext(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*disposable = obsJust.subscribe({ num ->
            Log.i("MainActivity", "$num")
            text_view.text = "$num"}, {}, { text_view.text = "Completed"})*/

        /*disposable = obsCreate.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ num ->
            Log.i("MainActivity", "$num")
            text_view.text = "$num"}, {}, { text_view.text = "Completed!"})*/

        val obsFirst = first_name.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { it.length > 2}
        val obsLast = last_name.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { it.length > 2}

        val obsCombined = Observables.combineLatest(obsFirst, obsLast) { first, last ->
            "$first $last"
        }

        /*disposable = obsCombined.observeOn(AndroidSchedulers.mainThread())
            .subscribe { name -> text_view.text = name }*/

        val retrofit = Retrofit.Builder().baseUrl("https://api.exchangerate-api.com/v4/latest/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val service = retrofit.create(ForexService::class.java)

        disposable = service.getRatesFor("USD")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rates ->
                exchange_rates.text = rates.rates.toString()
            }, { t ->
                Log.i("Retrofit", "$t", t)
            })
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
