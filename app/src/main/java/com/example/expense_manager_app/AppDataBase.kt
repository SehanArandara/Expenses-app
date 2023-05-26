package com.example.expense_manager_app

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Transaction::class), version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract  fun transactionDoa() : TransactionDoa
}