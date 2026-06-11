package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpendingBreakdownActivity : AppCompatActivity() {

    private val prefsName = "BudgetPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_breakdown)

        val txtCategoryTotals = findViewById<TextView>(R.id.txtCategoryTotals)
        val btnBreakdownHome = findViewById<Button>(R.id.btnBreakdownHome)

        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        val categories = resources.getStringArray(R.array.expense_categories)
        val displayText = StringBuilder()

        for (category in categories) {
            val total = prefs.getFloat(category, 0f)
            displayText.append("$category: $%.2f\n\n".format(total))
        }
        txtCategoryTotals.text = displayText.toString()

        btnBreakdownHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}