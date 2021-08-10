package com.example.chatboot

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.example.chatboot.daos.UserDao
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_name.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val actionBar=supportActionBar
        actionBar!!.title = "Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection("users")
        //image upload
        camera_icon_holder.setOnClickListener{
            val mBottomSheetDialog = RoundedBottomSheetDialog(this)
            val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_camera, null)
            mBottomSheetDialog.setContentView(sheetView)
            mBottomSheetDialog.show()
        }
        //editName
        editName.setOnClickListener {
            editName.setText("Dipa Halder")
            val mBottomSheetDialog = RoundedBottomSheetDialog(this)
            val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_edit_name, null)
            mBottomSheetDialog.setContentView(sheetView)
            val btnCancelName = mBottomSheetDialog.findViewById<TextView>(R.id.cancelTxtName)
            val btnSaveName=mBottomSheetDialog.findViewById<TextView>(R.id.saveTxtName)
            val editNameTxt=mBottomSheetDialog.findViewById<EditText>(R.id.editNameTxt)
            if (btnCancelName != null) {
                btnCancelName.setOnClickListener {
                    mBottomSheetDialog.cancel()
                }
            }
            if(btnSaveName!=null){
                btnSaveName.setOnClickListener {
                    if (editNameTxt != null) {
                        editName.setText(editNameTxt.text.toString().trim())
                        mBottomSheetDialog.cancel()
                    }
                }
            }
            mBottomSheetDialog.show()
        }
        //editAbout
        editAbout.setOnClickListener {
//            editAbout.setText("Here using ChatBoot :))")
            val mBottomSheetDialog=RoundedBottomSheetDialog(this)
            val sheetView2 = layoutInflater.inflate(R.layout.bottom_sheet_edit_about,null)
            mBottomSheetDialog.setContentView(sheetView2)
            val btnCancelAbout = mBottomSheetDialog.findViewById<TextView>(R.id.cancelTxtAbout)
            val btnSaveAbout=mBottomSheetDialog.findViewById<TextView>(R.id.saveTxtAbout)
            val editAboutTxt=mBottomSheetDialog.findViewById<EditText>(R.id.editAboutTxt)

            if (btnCancelAbout != null) {
                btnCancelAbout.setOnClickListener {
                    mBottomSheetDialog.cancel()
                }
            }
            if(btnSaveAbout!=null){
                btnSaveAbout.setOnClickListener {
                    if (editAboutTxt != null) {
                        editAbout.setText(editAboutTxt.text.toString().trim())
                        mBottomSheetDialog.cancel()
                    }
                }
            }
            mBottomSheetDialog.show()
        }
        //logOut user
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

        //delete user
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