package com.example.chatboot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!

        logOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        deleteUser.setOnClickListener {
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"User account deleted.",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                       finish()
                    }
                    else{
                        Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}