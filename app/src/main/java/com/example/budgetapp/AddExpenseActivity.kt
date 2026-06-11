package com.example.budgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {

    private val prefsName = "BudgetPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val edtExpenseName = findViewById<EditText>(R.id.edtExpenseName)
        val edtExpenseAmount = findViewById<EditText>(R.id.edtExpenseAmount)
        val spnExpenseCategory = findViewById<Spinner>(R.id.spnExpenseCategory)
        val btnAddExpenseSubmit = findViewById<Button>(R.id.btnAddExpenseSubmit)
        val btnExpenseHome = findViewById<Button>(R.id.btnExpenseHome)

        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        btnAddExpenseSubmit.setOnClickListener {
            val name = edtExpenseName.text.toString()
            val amount = edtExpenseAmount.text.toString().toFloatOrNull()
            val category = spnExpenseCategory.selectedItem.toString()

            // Validates that the user entered a name and a number
            if (name.isEmpty() || amount == null) {
                Toast.makeText(this, getString(R.string.enter_expense_name_amount), Toast.LENGTH_SHORT).show()
            } else {
                // Subtracts the expense from the balance
                val currentBalance = prefs.getFloat("balance", 0f)
                val newBalance = currentBalance - amount

                // Gets today's date
                val date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())

                // Creates the transaction text with date, type, name, amount, and category
                val expenseText = "$date | $name | - $%.2f | $category".format(amount)

                saveTransaction(expenseText)

                val oldCategoryTotal = prefs.getFloat(category, 0f)
                val newCategoryTotal = oldCategoryTotal + amount

                prefs.edit()
                    .putFloat("balance", newBalance)
                    .putString("lastExpense", expenseText)
                    .putFloat(category, newCategoryTotal)
                    .apply()

                Toast.makeText(this, getString(R.string.expense_added), Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        btnExpenseHome.setOnClickListener {
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