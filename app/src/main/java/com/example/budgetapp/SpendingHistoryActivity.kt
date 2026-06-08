package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpendingHistoryActivity : AppCompatActivity() {

    private val prefsName = "BudgetPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_history)

        // List of the 5 transaction TextViews from the layout
        val transactionViews = listOf(
            findViewById<TextView>(R.id.txtTransaction1),
            findViewById<TextView>(R.id.txtTransaction2),
            findViewById<TextView>(R.id.txtTransaction3),
            findViewById<TextView>(R.id.txtTransaction4),
            findViewById<TextView>(R.id.txtTransaction5)
        )

        val btnHistoryHome = findViewById<Button>(R.id.btnHistoryHome)
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        val transactions = mutableListOf<String>()

        // Only displays the latest 5 transactions
        for (i in 1..5) {
            val transaction = prefs.getString("transaction$i", "") ?: ""

            if (transaction.isNotEmpty()) {
                transactions.add(transaction)
            }
        }

        if (transactions.isEmpty()) {
            transactionViews[0].text = getString(R.string.no_recent_transactions)
            transactionViews[0].visibility = View.VISIBLE

            for (i in 1 until transactionViews.size) {
                transactionViews[i].visibility = View.GONE
            }
        } else {
            for (i in transactionViews.indices) {
                if (i < transactions.size) {
                    transactionViews[i].text = transactions[i]
                    transactionViews[i].visibility = View.VISIBLE
                } else {
                    transactionViews[i].visibility = View.GONE
                }
            }
        }

        btnHistoryHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}