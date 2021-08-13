package com.example.chatboot

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.example.chatboot.daos.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream


class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var name=""
    var mail=""
    var about=""
    var img=""
    private val REQUEST_CODE_GALLERY = 200
    private val REQUEST_CODE_TAKE_PIC = 300
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

        loadData()

        //image upload
        camera_icon_holder.setOnClickListener{
            val mBottomSheetDialog = RoundedBottomSheetDialog(this)
            val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_camera, null)
            mBottomSheetDialog.setContentView(sheetView)

            val removePic=mBottomSheetDialog.findViewById<ImageView>(R.id.remove)
            val galleryPic=mBottomSheetDialog.findViewById<ImageView>(R.id.gallery)
            val takePic=mBottomSheetDialog.findViewById<ImageView>(R.id.takeCam)

            removePic?.setOnClickListener {
                userCollection.document(user.uid).update(
                    "imageUrl",
                    "https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                )
                    .addOnSuccessListener {
                        Glide.with(getApplicationContext())
                            .load("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52")
                            .into(imgHolder)
                        Toast.makeText(this, "Profile pic removed", Toast.LENGTH_SHORT).show()
                        mBottomSheetDialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                    }

            }

            galleryPic?.setOnClickListener {
                val intent1 = Intent()
                intent1.action = Intent.ACTION_GET_CONTENT
                intent1.type = "image/*"
                startActivityForResult(intent1, 200)
                mBottomSheetDialog.dismiss()
            }

            takePic?.setOnClickListener {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CODE_GALLERY
                    )
                } else {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PIC)
                    mBottomSheetDialog.dismiss()
                }

            }

            mBottomSheetDialog.show()
        }

        //editName
        editName.setOnClickListener {

            val mBottomSheetDialog = RoundedBottomSheetDialog(this)
            val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_edit_name, null)
            mBottomSheetDialog.setContentView(sheetView)

            val btnCancelName = mBottomSheetDialog.findViewById<TextView>(R.id.cancelTxtName)
            val btnSaveName=mBottomSheetDialog.findViewById<TextView>(R.id.saveTxtName)
            val editNameTxt=mBottomSheetDialog.findViewById<EditText>(R.id.editNameTxt)

            if (editNameTxt != null) {
                editNameTxt.setText(name)
            }
            if (btnCancelName != null) {
                btnCancelName.setOnClickListener {
                    mBottomSheetDialog.cancel()
                }
            }
            if(btnSaveName!=null){

                btnSaveName.setOnClickListener {
                    if (editNameTxt != null) {
                        if(!editNameTxt.text.toString().trim().isEmpty()) {
                            if(editNameTxt.text.toString().trim().length<=20){
                                userCollection.document(user.uid).update(
                                    "displayName",
                                    editNameTxt.text.toString().trim()
                                )
                                    .addOnSuccessListener {
                                        editName.setText(editNameTxt.text.toString().trim())
                                        Toast.makeText(this, "Name updated", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                    }
                                mBottomSheetDialog.cancel()
                            }
                            else{
                                Toast.makeText(
                                    this,
                                    "Atmost 20 character are allowed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else{
                            Toast.makeText(this, "Empty name not allowed", Toast.LENGTH_SHORT).show()
                        }


                    }
                }
            }
            mBottomSheetDialog.show()
        }

        //editAbout
        editAbout.setOnClickListener {

            val mBottomSheetDialog=RoundedBottomSheetDialog(this)
            val sheetView2 = layoutInflater.inflate(R.layout.bottom_sheet_edit_about, null)
            mBottomSheetDialog.setContentView(sheetView2)
            val btnCancelAbout = mBottomSheetDialog.findViewById<TextView>(R.id.cancelTxtAbout)
            val btnSaveAbout=mBottomSheetDialog.findViewById<TextView>(R.id.saveTxtAbout)
            val editAboutTxt=mBottomSheetDialog.findViewById<EditText>(R.id.editAboutTxt)

            if (editAboutTxt != null) {
                editAboutTxt.setText(about)

            }
            if (btnCancelAbout != null) {
                btnCancelAbout.setOnClickListener {
                    mBottomSheetDialog.cancel()
                }
            }
            if(btnSaveAbout!=null){
                btnSaveAbout.setOnClickListener {
                    if (editAboutTxt != null) {
                        if(!editAboutTxt.text.toString().trim().isEmpty()) {
                            if(editAboutTxt.text.toString().trim().length<=30){
                                userCollection.document(user.uid).update(
                                    "about",
                                    editAboutTxt.text.toString().trim()
                                )
                                    .addOnSuccessListener {
                                        editAbout.setText(editAboutTxt.text.toString().trim())
                                        Toast.makeText(this, "About updated", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                    }
                                mBottomSheetDialog.cancel()
                            }
                            else{
                                Toast.makeText(
                                    this,
                                    "Atmost 30 character are allowed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else{
                            Toast.makeText(this, "Empty name not allowed", Toast.LENGTH_SHORT).show()
                        }
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
                    .setPositiveButton("Yes"){ dialogInterface, which->
                        auth.signOut()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
            }
                    .setNegativeButton("No"){ dialogInterface, which->
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
                .setPositiveButton("Yes"){ dialogInterface, which->
                        userCollection.document(auth.currentUser!!.uid)
                            .delete()
                            .addOnSuccessListener {

                                user.delete()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "User Account successfully deleted",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                        }
                                        else{
                                            Toast.makeText(
                                                this,
                                                "Some error occurred",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                            .addOnFailureListener{
                                Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                            }
                    }

                .setNegativeButton("No"){ dialogInterface, which->
                    dialogInterface.dismiss()
                }
            val alertDialog:AlertDialog=mBuilder.create()
            alertDialog.show()
        }
    }

    //getting data from firestore
    private fun loadData() {
        auth= FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection("users")
        userCollection.document(user.uid)
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                Toast.makeText(this, "Listen failed", Toast.LENGTH_SHORT).show()
                                return@addSnapshotListener
                            }

                            if (value != null && value.exists()) {

                                name=value.getString("displayName").toString()
                                mail=value.getString("email").toString()
                                about=value.getString("about").toString()
                                img=value.getString("imageUrl").toString()
                                editName.setText(name)
                                editEmail.setText(mail)
                                editAbout.setText(about)
                                Glide.with(getApplicationContext()).load(img).into(imgHolder)

                            } else {
                                Toast.makeText(this, "Please uninstall and reinstall your app", Toast.LENGTH_SHORT).show()
                            }
                        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection("users")
        val uID=FirebaseAuth.getInstance().currentUser!!.uid


        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TAKE_PIC && data != null){
            val capImg=data.extras?.get("data") as Bitmap
            imgHolder.setImageBitmap(capImg)
            val byteArrayOutputStream=ByteArrayOutputStream()
            capImg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val dat = byteArrayOutputStream.toByteArray()

            val storage =FirebaseStorage.getInstance().getReference("images").child(uID)
            storage.putBytes(dat)
                .addOnSuccessListener {
                    storage.downloadUrl.addOnSuccessListener {
                        val link=it.toString()
                        userCollection.document(uID).update("imageUrl", link)
                            .addOnSuccessListener {
                                Glide.with(this).load(link).into(imgHolder)
                                Toast.makeText(
                                    this,
                                    "Image Upload Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                    }

                }
                .addOnFailureListener{
                    Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                }

        }
        else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY && data != null){
            val imageUri = data.data
            imgHolder.setImageURI(imageUri)
            uploadimage(imageUri)
        }
    }

    //upload image using gallery method
    private fun uploadimage(imageUri: Uri?) {
        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection("users")
        val uID=FirebaseAuth.getInstance().currentUser!!.uid


        val storage=FirebaseStorage.getInstance().getReference("images").child(uID)
        if (imageUri != null) {
            storage.putFile(imageUri)
                .addOnSuccessListener {
                    storage.downloadUrl
                        .addOnSuccessListener{
                            val link=it.toString()
                            userCollection.document(uID).update("imageUrl", link)
                                .addOnSuccessListener {
                                    Glide.with(this).load(link).into(imgHolder)
                                    Toast.makeText(
                                        this,
                                        "Image Upload Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}