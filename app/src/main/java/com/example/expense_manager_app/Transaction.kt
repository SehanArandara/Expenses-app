package com.example.expense_manager_app

import androidx.room.Entity
import androidx.room.PrimaryKey



// creating the entity
@Entity(tableName = "Transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id : Int ,
    val lable : String,
    val amount : Double,
    val description : String
    )
{


}