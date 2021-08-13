package com.example.chatboot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatboot.Models.User
import com.example.chatboot.daos.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var count=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
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
                                                        val userFirebase=User(user.uid,editName.text.toString().trim(),user.email,
                                                            "https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                                                            ,"Here using ChatBoot :))")
                                                        val userDao=UserDao()
                                                        userDao.addUser(userFirebase)
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
            return
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

    override fun onBackPressed() {
        val intent1=Intent(Intent.ACTION_MAIN)
        intent1.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent1)
    }
}


