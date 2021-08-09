package com.example.chatboot.daos

import com.example.chatboot.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
    private val uid= FirebaseAuth.getInstance().currentUser?.uid

    fun addUser(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.uid).set(it)
            }
        }
    }
}