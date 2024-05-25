package com.yash.teapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.yash.teapp.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yash.teapp.R
import java.util.UUID

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextGender: EditText
    private lateinit var editTextCity: EditText
    private lateinit var editTextState: EditText
    private lateinit var editTextCountry: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonChoosePicture: Button
    private lateinit var imageView: ImageView
    private lateinit var buttonUpdate: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editTextName = findViewById(R.id.editTextName)
        editTextGender = findViewById(R.id.editTextEmail)
        editTextCity = findViewById(R.id.editTextCity)
        editTextState = findViewById(R.id.editTextState)
        editTextCountry = findViewById(R.id.editTextCountry)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonChoosePicture = findViewById(R.id.buttonChoosePicture)
        imageView = findViewById(R.id.imageView)
        buttonUpdate = findViewById(R.id.buttonUpdate)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference.child("profile_pictures")

        buttonChoosePicture.setOnClickListener {
            openGallery()
        }

        buttonUpdate.setOnClickListener {
            updateProfile()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun updateProfile() {
        val user = auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            val userRef = database.child("users").child(userId)
            val userInfo = HashMap<String, Any>()
            userInfo["name"] = editTextName.text.toString().trim()
            userInfo["gender"] = editTextGender.text.toString().trim()
            userInfo["city"] = editTextCity.text.toString().trim()
            userInfo["state"] = editTextState.text.toString().trim()
            userInfo["country"] = editTextCountry.text.toString().trim()
            userInfo["phone"] = editTextPhone.text.toString().trim()

            userRef.updateChildren(userInfo)
                .addOnSuccessListener {
                    if (imageUri != null) {
                        val imageRef = storageRef.child(userId)
                        imageRef.putFile(imageUri!!)
                            .continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    task.exception?.let {
                                        throw it
                                    }
                                }
                                imageRef.downloadUrl
                            }
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val downloadUri = task.result
                                    val imageInfo = HashMap<String, Any>()
                                    imageInfo["profileImageUrl"] = downloadUri.toString()
                                    userRef.updateChildren(imageInfo)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this, "Failed to update profile: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    Toast.makeText(this, "Failed to upload image: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update profile: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(imageView)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}