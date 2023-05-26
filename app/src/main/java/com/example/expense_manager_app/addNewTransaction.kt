package com.example.expense_manager_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class addNewTransaction : AppCompatActivity() {
    private lateinit var addTransaction :Button
    private lateinit var lableInput : EditText
    private lateinit var amountInput : EditText
    private lateinit var descriptionInput : EditText
    private lateinit var lableLayout: TextInputLayout
    private lateinit var amountLayout: TextInputLayout
    private lateinit var closeBtn :ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_transaction)

        // setting id
        addTransaction = findViewById(R.id.addTransaction)
        lableInput = findViewById(R.id.lableInput)
        amountInput= findViewById(R.id.amountInput)
        lableLayout = findViewById(R.id.lableLayout)
        amountLayout = findViewById(R.id.lableAmount)
        closeBtn  =  findViewById(R.id.closeBtn)
        descriptionInput =  findViewById(R.id.descriptionInput)

        lableInput.addTextChangedListener {
            if(it!!.count()>0){
                lableLayout.error = null
            }
        }
        amountInput.addTextChangedListener {
            if(it!!.count()>0){
                amountLayout.error = null
            }
        }

        closeBtn.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        addTransaction.setOnClickListener {
            val lable = lableInput.text.toString()
            val amount = amountInput.text.toString()
            val description = descriptionInput.text.toString()

            if(lable.isEmpty()){
                lableLayout.error = "Please enter valid lable"
            }
            else if(amount == null){
                amountLayout.error = "Please enter valid amount"
            }
            else{
                val transaction = Transaction(0,lable,amount.toDouble(),description)
                insert(transaction)
            }
        }
    }

    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDataBase::class.java,
            "Transactions.db"
        ).build()

        GlobalScope.launch {
            db.transactionDoa().insertAll(transaction)
            finish()
        }
    }


}