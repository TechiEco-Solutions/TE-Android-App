package com.techieco.teandroid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.techieco.teandroid.databinding.ActivityEditProfileBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonChooseImage.setOnClickListener {
            openFileChooser()
        }

        binding.buttonUpload.setOnClickListener {
            uploadData()
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.imageViewProfile.setImageURI(imageUri)
        }
    }

    private fun uploadData() {
        val name = binding.editTextName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val mobile = binding.editTextMobile.text.toString().trim()
        val location = binding.editTextLocation.text.toString().trim()

        if (imageUri != null) {
            val storageReference = FirebaseStorage.getInstance().getReference("profile_images/${UUID.randomUUID()}.jpg")
            storageReference.putFile(imageUri!!).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    val databaseReference = FirebaseDatabase.getInstance().getReference("users")
                    val userId = databaseReference.push().key!!
                    val user = User(userId, name, email, mobile, location, imageUrl)
                    databaseReference.child(userId).setValue(user)
                }
            }
        } else {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users")
            val userId = databaseReference.push().key!!
            val user = User(userId, name, email, mobile, location, null)
            databaseReference.child(userId).setValue(user)
        }
    }

    data class User(
        val userId: String,
        val name: String,
        val email: String,
        val mobile: String,
        val location: String,
        val profileImageUrl: String?
    )

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
