package com.example.expense_manager_app

import androidx.room.*


//data access object - Dao

@Dao
interface TransactionDoa {
    @Query("select * from Transactions ")
    fun getAll() : List<Transaction>

    @Insert
    fun insertAll(vararg  transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(transaction:Transaction)


}