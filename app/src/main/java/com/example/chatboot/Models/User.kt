package com.example.chatboot.Models

data class User(
    val uid: String="",
    val displayName: String?="",
    val email: String?="",
    val imageUrl: String? ="",
    val about: String=""
        )
