package com.example.wishlistapp

sealed class Screen(val route: String) {
    object HomeScreen: Screen("homescreen")
    object  AddScreen: Screen("addscreen")
}