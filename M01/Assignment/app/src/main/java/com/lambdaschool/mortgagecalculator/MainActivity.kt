package com.lambdaschool.mortgagecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.pow
import java.util.concurrent.TimeUnit
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val obsPurchasePrice= edit_text_purchase_price.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)

        val obsDownPayment= edit_text_down_payment.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)

        val obsInterestRate = edit_text_interest_rate.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)

        val obsLoanLength = edit_text_loan_length.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)

        val obsCombined = Observables.combineLatest(obsPurchasePrice, obsDownPayment, obsInterestRate, obsLoanLength) { PP, DP, IR, LL ->
            //A = Pr(1+r)^n/((1+r)^n - 1)
            val P = PP.toString().toDouble() - DP.toString().toDouble()
            val r = IR.toString().toDouble() / 12
            val n = LL.toString().toDouble()

            val monthlyPayment = ( P * r * ( (1.0 + r).pow(n) ) ) / ( ( ( 1.0 + r).pow( n ) ) - 1.0 )

            "$monthlyPayment"
        }

        disposable = obsCombined.observeOn(AndroidSchedulers.mainThread())
            .subscribe { monthlyPayment -> text_view_output.text = monthlyPayment}
    }
}
