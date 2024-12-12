package com.capstone.nutrise.view.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.nutrise.R

class CameraActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var buttonGallery: ImageButton
    private lateinit var buttonCamera: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Inisialisasi view
        previewImageView = findViewById(R.id.previewImageView)
        buttonGallery = findViewById(R.id.button)
        buttonCamera = findViewById(R.id.button2)

        // Set onClick listener untuk galeri
        buttonGallery.setOnClickListener {
            requestGalleryPermission()
        }

        // Set onClick listener untuk kamera
        buttonCamera.setOnClickListener {
            requestCameraPermission()
        }
    }

    // Launch galeri
    private val openGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    previewImageView.setImageURI(it)
                } catch (e: Exception) {
                    Log.e("CameraActivity", "Error loading image from gallery", e)
                    Toast.makeText(this, "Gagal memuat gambar dari galeri", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // Minta izin untuk galeri
    private fun requestGalleryPermission() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            openGalleryLauncher.launch("image/*")
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_GALLERY)
        }
    }

    // Launch kamera
    private val openCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                if (imageBitmap != null) {
                    try {
                        previewImageView.setImageBitmap(imageBitmap)
                    } catch (e: Exception) {
                        Log.e("CameraActivity", "Error displaying camera image", e)
                        Toast.makeText(
                            this,
                            "Gagal menampilkan gambar dari kamera",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("CameraActivity", "Bitmap is null")
                    Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("CameraActivity", "Camera result not OK")
            }
        }

    // Minta izin untuk kamera
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
        }
    }

    private fun launchCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            openCameraLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(this, "Tidak ada aplikasi kamera yang tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle hasil permintaan izin
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    openGalleryLauncher.launch("image/*")
                } else {
                    Toast.makeText(this, "Izin Galeri Ditolak", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    launchCamera()
                } else {
                    Toast.makeText(this, "Izin Kamera Ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_GALLERY = 100
        private const val REQUEST_CODE_CAMERA = 101
    }
}