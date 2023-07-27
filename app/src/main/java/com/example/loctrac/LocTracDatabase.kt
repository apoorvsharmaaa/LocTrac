package com.example.loctrac

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
public abstract class LocTracDatabase:RoomDatabase(){

    abstract fun contactDao(): ContactDao




    companion object{

        @Volatile
        private var INSTANCE:LocTracDatabase ? = null


        fun getDatabase(context:Context):LocTracDatabase {

            INSTANCE?.let {
                return it
            }

            return synchronized(LocTracDatabase::class.java) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocTracDatabase::class.java,
                    "My_family_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}