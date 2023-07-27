package com.example.loctrac

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactModel: ContactModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contactModelList: List<ContactModel>)

    @Query("SELECT * FROM contactmodel")
    fun getAllContacts(): LiveData<List<ContactModel>>


}