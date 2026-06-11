package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val prefsName = "BudgetPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons that open the other screens
        findViewById<Button>(R.id.btnAddIncome).setOnClickListener {
            startActivity(Intent(this, AddIncomeActivity::class.java))
        }

        findViewById<Button>(R.id.btnAddExpense).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        findViewById<Button>(R.id.btnSpendingHistory).setOnClickListener {
            startActivity(Intent(this, SpendingHistoryActivity::class.java))
        }

        findViewById<Button>(R.id.btnSpendingBreakdown).setOnClickListener {
            startActivity(Intent(this, SpendingBreakdownActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        // Updates the dashboard every time the user returns home
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        val balance = prefs.getFloat("balance", 0f)
        val lastExpense = prefs.getString("lastExpense", getString(R.string.no_expenses_yet))

        findViewById<TextView>(R.id.txtCurrentBalanceValue).text = "$%.2f".format(balance)
        findViewById<TextView>(R.id.txtLastExpenseValue).text = lastExpense
    }
}