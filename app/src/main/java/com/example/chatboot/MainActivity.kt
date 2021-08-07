package com.example.chatboot

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()


        progress.visibility=View.GONE
        hideTxt.text=getString(R.string.sign_up_txt)
        signUp.text="Log In"
        forgot.visibility= View.VISIBLE
        filledConfirmPassword.visibility=View.GONE
        filledName.visibility=View.GONE

        hideTxt.setOnClickListener({
            if(count==1){
                count=0
                hideTxt.text=getString(R.string.sign_up_txt)
                signUp.text="Log In"
                forgot.visibility= View.VISIBLE
                filledConfirmPassword.visibility=View.GONE
                filledName.visibility=View.GONE
                editEmail.setText("")
                editPassword.setText("")
                filledPassword.setHelperText("Required*")
            }
            else if(count==0){
                count=1
                hideTxt.text=getString(R.string.log_in_txt)
                signUp.text="Sign Up"
                forgot.visibility= View.GONE
                filledConfirmPassword.visibility=View.VISIBLE
                filledName.visibility=View.VISIBLE
                editName.setText("")
                editEmail.setText("")
                editPassword.setText("")
                editConfirmPassword.setText("")
                filledPassword.setHelperText("Required* (like:user@123)")
            }
        })

        forgot.setOnClickListener {
            startActivity(Intent(this,ForgotPassActivity::class.java))
        }

        signUp.setOnClickListener({
            if(count==1) {
                if (editName.text.toString().trim().isEmpty() || editEmail.text.toString().trim().isEmpty()
                || editPassword.text.toString().trim().isEmpty() || editConfirmPassword.text.toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please fill all credentials", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (editPassword.text.toString().trim().length < 6) {
                        filledPassword.setHelperText("Week Password")

                    } else if (editPassword.text.toString().trim().length >= 6) {
                        if (editPassword.text.toString().trim()
                                .contains('@') || editPassword.text.toString().trim()
                                .contains('&') ||
                            editPassword.text.toString().trim().contains('%')
                        ) {
                            filledPassword.setHelperText("Strong Password")

                            if (!editPassword.text.toString().trim()
                                    .equals(editConfirmPassword.text.toString().trim())
                            ) {
                                Toast.makeText(this, "Passwords are different", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                progress.visibility=View.VISIBLE
                                val email: String = editEmail.text.toString().trim { it <= ' ' }
                                val pass: String = editPassword.text.toString().trim { it <= ' ' }

                                auth.createUserWithEmailAndPassword(email, pass)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            val user = auth.currentUser
                                            user?.sendEmailVerification()
                                                ?.addOnCompleteListener{ task->
                                                    if(task.isSuccessful){
//                                                        Toast.makeText(this, "You registered successfully", Toast.LENGTH_SHORT).show()
                                                        updateUI(user)
                                                    }
                                                }

                                        } else {
                                            Toast.makeText(this,"User Alredy exists",Toast.LENGTH_SHORT).show()
                                            updateUI(null)
                                        }
                                    }
                            }
                        } else {
                            filledPassword.setHelperText("Not so strong Password")
                        }
                    }

                }
            }
            else if(count==0){
                if (editEmail.text.toString().trim().isEmpty() || editPassword.text.toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please fill all credentials", Toast.LENGTH_SHORT).show()
                }
                else{
                    progress.visibility=View.VISIBLE
                    val email: String = editEmail.text.toString().trim { it <= ' ' }
                    val pass: String = editPassword.text.toString().trim { it <= ' ' }
                        auth.signInWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(this){   task->
                                if(task.isSuccessful){
                                    val user = auth.currentUser
                                    Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()
                                    updateUI(user)
                                }
                                else{
                                    Toast.makeText(this,"Login failed",Toast.LENGTH_SHORT).show()
                                    updateUI(null)
                                }
                            }
                }
            }
        })
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user==null){
            progress.visibility=View.GONE
        }
        else{
            if(user.isEmailVerified){
                startActivity(Intent(this,ChatActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(this, "Please verify your email address", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onResume() {
        super.onResume()
            count=0
            progress.visibility=View.GONE
            hideTxt.text=getString(R.string.sign_up_txt)
            signUp.text="Log In"
            forgot.visibility= View.VISIBLE
            filledConfirmPassword.visibility=View.GONE
            filledName.visibility=View.GONE
    }
}


