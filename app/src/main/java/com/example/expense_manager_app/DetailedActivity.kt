package com.example.expense_manager_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {
    private lateinit var updateBtn : Button
    private lateinit var lableInput : EditText
    private lateinit var amountInput : EditText
    private lateinit var descriptionInput : EditText
    private lateinit var lableLayout: TextInputLayout
    private lateinit var amountLayout: TextInputLayout
    private lateinit var desLayout: TextInputLayout
    private lateinit var closeBtn : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        // setting id
        updateBtn = findViewById(R.id.updateBtn)
        lableInput = findViewById(R.id.lableInput)
        amountInput= findViewById(R.id.amountInput)
        lableLayout = findViewById(R.id.lableLayout)
        amountLayout = findViewById(R.id.lableAmount)
        desLayout =  findViewById(R.id.lableDescription)
        closeBtn  =  findViewById(R.id.closeBtn)
        descriptionInput =  findViewById(R.id.descriptionInput)
        var rootView : View = findViewById(R.id.rootView)

        //getting intent details
        var intent_lable =  intent.getStringExtra("lable")
        var intent_amount = intent.getStringExtra("amount")
        var intent_des = intent.getStringExtra("des")
        var intent_id = intent.getStringExtra("Id")

        lableInput.setText(intent_lable)
        amountInput.setText(intent_amount)
        descriptionInput.setText(intent_des)
        var ID = intent_id.toString().toInt()


        rootView.setOnClickListener {
            this.window.decorView.clearFocus()
             val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken,0 )
        }

        lableInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
            if(it!!.count()>0){
                lableLayout.error = null
            }
        }
        amountInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
            if(it!!.count()>0){
                amountLayout.error = null
            }
        }

        descriptionInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
            if(it!!.count()>0){
                desLayout.error = null
            }
        }

        closeBtn.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        updateBtn.setOnClickListener {
            val lable = lableInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()
            val description = descriptionInput.text.toString()


            if(lable.isEmpty()){
                lableLayout.error = "Please enter valid lable"
            }
            else if(amount == null){
                amountLayout.error = "Please enter valid amount"
            }
            else{
                val transaction = Transaction(ID,lable,amount.toDouble(),description)
                update(transaction)
            }
        }
    }

    private fun update(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDataBase::class.java,
            "Transactions.db"
        ).build()

        GlobalScope.launch {
            db.transactionDoa().update(transaction)
            finish()
        }
    }


}
