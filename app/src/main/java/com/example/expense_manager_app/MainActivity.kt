package com.example.expense_manager_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.expense_manager_app.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var transactions : List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private  lateinit var budget : TextView
    private lateinit var expense : TextView
    private lateinit var balance : TextView
    private lateinit var deletedTransaction : Transaction
    private lateinit var oldTransaction: List<Transaction>

    //creating variable for data base
    private lateinit var db : AppDataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        budget = findViewById(R.id.budget)
        expense = findViewById(R.id.expense)
        balance = findViewById(R.id.balance)


        // create the data base builder
        db = Room.databaseBuilder(this,
            AppDataBase::class.java,
            "Transactions.db"
            ).build()


        transactions = arrayListOf(

        )

        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutManager = LinearLayoutManager(this)


        binding.reCycleView.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager
        }



        binding.addNew.setOnClickListener{
            var intent = Intent(this,addNewTransaction::class.java)
            startActivity(intent)
        }

        // swipe data
        val itemHolder = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }

        }

        val swipHelper = ItemTouchHelper(itemHolder)
        swipHelper.attachToRecyclerView(binding.reCycleView)




    }

    private  fun fetchAlll () {
        GlobalScope.launch {
            transactions =  db.transactionDoa().getAll()

            runOnUiThread {
                updateDashBord()
                transactionAdapter.setData(transactions)
            }
        }


    }
    private fun updateDashBord (){
        val totalAmount : Double = transactions.map { it.amount }.sum()
        val budgetAmount : Double = transactions.filter { it.amount>0 }.map { it.amount }.sum()
        val expenseAmout : Double = totalAmount - budgetAmount

        balance.text ="$ %.2f".format(totalAmount)
        budget.text = "$ %.2f".format(budgetAmount)
        expense.text = "$ %.2f".format(expenseAmout)
    }

    private fun deleteTransaction(transaction: Transaction){
        deletedTransaction = transaction
        oldTransaction = transactions

        GlobalScope.launch {
            db.transactionDoa().delete(transaction)

            transactions =  transactions.filter { it.id != transaction.id }

            runOnUiThread{
                updateDashBord()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        fetchAlll()
    }


}