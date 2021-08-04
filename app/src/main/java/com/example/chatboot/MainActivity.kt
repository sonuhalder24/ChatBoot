package com.example.chatboot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var count=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideTxt.setOnClickListener({
            if(count==1){
                count=0
                hideTxt.text=getString(R.string.sign_up_txt)
                signUpBtn.text="Log In"
                forgot.visibility= View.GONE
                filledConfirmPassword.visibility=View.GONE
            }
            else{
                count=1
                hideTxt.text=getString(R.string.log_in_txt)
                signUpBtn.text="Sign Up"
                forgot.visibility= View.VISIBLE
                filledConfirmPassword.visibility=View.VISIBLE
            }
        })
    }
}