package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddIncomeActivity : AppCompatActivity() {

    private val prefsName = "BudgetPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)

        val edtIncomeSource = findViewById<EditText>(R.id.edtIncomeSource)
        val edtIncomeAmount = findViewById<EditText>(R.id.edtIncomeAmount)
        val btnAddIncomeSubmit = findViewById<Button>(R.id.btnAddIncomeSubmit)
        val btnIncomeHome = findViewById<Button>(R.id.btnIncomeHome)

        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        btnAddIncomeSubmit.setOnClickListener {
            val source = edtIncomeSource.text.toString()
            val amount = edtIncomeAmount.text.toString().toFloatOrNull()

            // Validates that the user entered a source and a number
            if (source.isEmpty() || amount == null) {
                Toast.makeText(this, getString(R.string.enter_income_source_amount), Toast.LENGTH_SHORT).show()
            } else {
                // Adds income to the saved balance
                val currentBalance = prefs.getFloat("balance", 0f)
                val newBalance = currentBalance + amount

                // Gets today's date
                val date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())

                // Creates the transaction text shown in spending history
                val transactionText = "$date | $source | + $%.2f".format(amount)

                saveTransaction(transactionText)

                prefs.edit()
                    .putFloat("balance", newBalance)
                    .apply()

                Toast.makeText(this, getString(R.string.income_added), Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        btnIncomeHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun saveTransaction(transactionText: String) {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val editor = prefs.edit()

        // Moves older transactions down one spot
        for (i in 20 downTo 2) {
            val previous = prefs.getString("transaction${i - 1}", "")
            editor.putString("transaction$i", previous)
        }

        // Newest transaction is always transaction1
        editor.putString("transaction1", transactionText)
        editor.apply()
    }
}