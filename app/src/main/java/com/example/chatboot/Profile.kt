package com.example.chatboot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chatboot.daos.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var actionBar=supportActionBar
        actionBar!!.title = "Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection("users")
        val uid= user.uid

        logOutTxt.setOnClickListener {
            val mBuilder=AlertDialog.Builder(this)
            mBuilder.setTitle("Log Out Alert")
                    .setMessage("Do you really log out from this account?")
                    .setPositiveButton("Yes"){dialogInterface,which->
                        auth.signOut()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
            }
                    .setNegativeButton("No"){dialogInterface,which->
                dialogInterface.dismiss()
            }
            val alertDialog:AlertDialog=mBuilder.create()
            alertDialog.show()
        }
        deleteTxt.setOnClickListener {
            val mBuilder=AlertDialog.Builder(this)
            mBuilder.setTitle("Delete Account Alert")
                .setMessage("Do you really delete this account?")
                .setPositiveButton("Yes"){dialogInterface,which->
                        userCollection.document(auth.currentUser!!.uid)
                            .delete()
                            .addOnSuccessListener {
//                                Toast.makeText(this,"document deleted",Toast.LENGTH_SHORT).show()
                                user.delete()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this,"User Account successfully deleted",Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this,MainActivity::class.java))
                                            finish()
                                        }
                                        else{
                                            Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                            .addOnFailureListener{
                                Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()
                            }
                    }

                .setNegativeButton("No"){dialogInterface,which->
                    dialogInterface.dismiss()
                }
            val alertDialog:AlertDialog=mBuilder.create()
            alertDialog.show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}