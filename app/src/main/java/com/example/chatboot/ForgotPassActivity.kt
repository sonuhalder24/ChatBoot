package com.example.chatboot

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import kotlinx.android.synthetic.main.activity_main.*

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_forgot_pass)
        auth = FirebaseAuth.getInstance()

        resetBtn.setOnClickListener{
            val inputMethodManager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
            if(editResetEmail.text.toString().trim().isEmpty()){
                Toast.makeText(this,"Email is needed",Toast.LENGTH_SHORT).show()
            }
            else{
                auth.sendPasswordResetEmail(editResetEmail.text.toString())
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Reset Password link is mailed",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this,"Reset Password mail could not be sent",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}