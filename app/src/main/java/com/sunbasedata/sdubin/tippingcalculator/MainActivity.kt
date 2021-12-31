package com.sunbasedata.sdubin.tippingcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENTAGE = 20
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIAL_TIP_PERCENTAGE
        tvTipPercentage.text = "$INITIAL_TIP_PERCENTAGE%"
        updateTipDescription(INITIAL_TIP_PERCENTAGE.toInt())

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged on the Seek Bar")
                tvTipPercentage.text = "$progress%"

                calculateTip()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBase.addTextChangedListener(object:TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(billText: Editable?) {
                Log.i(TAG, "afterTextChanged $billText")
                calculateTip()
            }
        })

    }

    private fun updateTipDescription(tipPercentage: Int) {
        tvTipLevel.text = "Amazing"
        val tipResult = when (tipPercentage) {
            in 0..9 -> "Bad"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Amazing"
            else -> "Fantastic!"
        }
        tvTipLevel.text = tipResult

        val color = ArgbEvaluator().evaluate(
            tipPercentage.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this, R.color.BadTipColor),
            ContextCompat.getColor(this, R.color.FantasticTipColor)
        ) as Int
        tvTipLevel.setTextColor(color)
    }

    private fun calculateTip() {
        // Check for invalid value
        if (etBase.text.isEmpty()) {
            tvTipAmt.text = ""
            tvTotalAmt.text = ""
            return
        }

        // Retrieve input from user on total bill & tip percentage
        val baseBillAmt = etBase.text.toString().toDouble()
        val tipPercentage = seekBarTip.progress

        // Calculate tip and total bill
        val tipAmount = (baseBillAmt * tipPercentage)/100
        val totalBillAmt = baseBillAmt + tipAmount

        // Update the UI with calculated values
        tvTipAmt.text = "%.2f".format(tipAmount)
        tvTotalAmt.text = "%.2f".format(totalBillAmt)

    }
}