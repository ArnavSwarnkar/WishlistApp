package com.example.wishlistapp

import android.content.Context
import androidx.room.Room
import com.example.wishlistapp.Data.WishDao
import com.example.wishlistapp.Data.WishDatabase
import com.example.wishlistapp.Data.WishRepository

object Graph {
    lateinit var database: WishDatabase

    val wishRepository by lazy {
        WishRepository(wishDao = database.wishDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context,
            WishDatabase::class.java,
            "wishlist.db").build()
    }
}