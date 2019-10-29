package com.lambdaschool.mortgagecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.activity_main.*
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
            calculateMonthlyPayment(PP, DP, IR, LL)
        }

        disposable = obsCombined.observeOn(AndroidSchedulers.mainThread())
            .subscribe { monthlyPayment -> text_view_output.text = monthlyPayment}
    }

    private fun calculateMonthlyPayment(
        purchasePrice: CharSequence,
        downPayment: CharSequence,
        interestRate: CharSequence,
        loanLength: CharSequence) : String {

        return if (
            purchasePrice.isNotEmpty() &&
            downPayment.isNotEmpty() &&
            interestRate.isNotEmpty() &&
            loanLength.isNotEmpty()
        ) {
            /**
             * A = P[r(1+r)^n/((1+r)^n - 1)]
             *
             * A = monthly payment
             *   @param monthlyPayment
             *
             * P = principal
             *   @param loanValue
             *
             * r = monthly interest rate
             *   @param monthlyInterestRate
             *
             * n = # of monthly payments to repay the loan
             *   @param numberOfMonthlyPayments
             * */
            val loanValue = purchasePrice.toString().toDouble() - downPayment.toString().toDouble()
            val monthlyInterestRate = (interestRate.toString().toDouble() / 100) / 12
            val numberOfMonthlyPayments = loanLength.toString().toInt()

            val monthlyPayment =

                loanValue *

                        ((monthlyInterestRate * (1 + monthlyInterestRate).pow(numberOfMonthlyPayments)) /

                        ((1 + monthlyInterestRate).pow(numberOfMonthlyPayments) - 1))

            val output = String.format("%.2f", monthlyPayment)

            "$$output /mo"
        } else {
            "Fields are missing input values"
        }
    }
}
