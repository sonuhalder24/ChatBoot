package com.example.chatboot

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

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
            }
            else{
                count=1
                hideTxt.text=getString(R.string.log_in_txt)
                signUp.text="Sign Up"
                forgot.visibility= View.GONE
                filledConfirmPassword.visibility=View.VISIBLE
                filledName.visibility=View.VISIBLE
            }
        })

        signUp.setOnClickListener({
            if(editName.text.toString().trim().isEmpty()|| editEmail.text.toString().trim().isEmpty()
                || editPassword.text.toString().trim().isEmpty() || editConfirmPassword.text.toString().trim().isEmpty())
            {
                Toast.makeText(this,"Please fill all credentials",Toast.LENGTH_SHORT).show()
            }
            else{
                if(editPassword.text.toString().trim().length<6){
                    filledPassword.setHelperText("Week Password")

                }
                else if(editPassword.text.toString().trim().length>=6){
                   if(editPassword.text.toString().trim().contains('@')||editPassword.text.toString().trim().contains('&')||
                       editPassword.text.toString().trim().contains('%')){
                       filledPassword.setHelperText("Strong Password")

                       if(!editPassword.text.toString().trim().equals(editConfirmPassword.text.toString().trim())){
                           Toast.makeText(this,"Passwords are different",Toast.LENGTH_SHORT).show()
                       }
                       else{
                           val email: String=editEmail.text.toString().trim{it <=' '}
                           val pass: String=editPassword.text.toString().trim{it <= ' '}

                           auth.createUserWithEmailAndPassword(email,pass)
                               .addOnCompleteListener(this) {    task ->
                                   if(task.isSuccessful){
//                               val user:FirebaseUser?= auth.currentUser
                                       Toast.makeText(this,"You registered successfully",Toast.LENGTH_SHORT).show()
                                   }
                                   else{
                                       Toast.makeText(this,"Something wrong",Toast.LENGTH_SHORT).show()
                                   }
                               }
                       }
                   }
                    else{
                       filledPassword.setHelperText("Not so strong Password")
                   }
                }

            }
        })
    }

}


