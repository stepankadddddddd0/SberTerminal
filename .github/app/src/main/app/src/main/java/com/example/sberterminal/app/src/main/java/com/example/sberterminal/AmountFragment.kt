package com.example.sberterminal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class AmountFragment : Fragment() {

    private lateinit var display: TextView
    private var amount = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_amount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        display = view.findViewById(R.id.amountDisplay)

        val ids = listOf(
            R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btn0, R.id.btnDot
        )

        ids.forEach { id ->
            view.findViewById<Button>(id).setOnClickListener {
                val digit = (it as Button).text.toString()
                if (amount == "0" && digit != ".") amount = digit
                else if (digit == "." && amount.contains(".")) return@setOnClickListener
                else amount += digit
                display.text = "₽ $amount"
            }
        }

        view.findViewById<Button>(R.id.btnClear).setOnClickListener {
            amount = if (amount.length > 1) amount.dropLast(1) else "0"
            display.text = "₽ $amount"
        }

        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            if (amount.toDoubleOrNull() != null && amount.toDouble() > 0) {
                val qrFragment = QRFragment().apply {
                    arguments = Bundle().apply { putString("amount", amount) }
                }
                (activity as MainActivity).loadFragment(qrFragment)
            }
        }
    }
}
