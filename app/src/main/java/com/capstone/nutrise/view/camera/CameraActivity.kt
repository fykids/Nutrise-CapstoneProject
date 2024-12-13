package com.capstone.nutrise.view.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.nutrise.R
import com.capstone.nutrise.data.AnalysisResult
import com.capstone.nutrise.data.retrofit.ApiConfig
import com.capstone.nutrise.data.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CameraActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var buttonGallery: ImageButton
    private lateinit var buttonCamera: ImageButton
    private lateinit var buttonAnalyze: Button
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Initialize view components
        previewImageView = findViewById(R.id.previewImageView)
        buttonGallery = findViewById(R.id.button)
        buttonCamera = findViewById(R.id.button2)
        buttonAnalyze = findViewById(R.id.analyzeButton)

        // Set onClick listeners for gallery and camera
        buttonGallery.setOnClickListener {
            requestGalleryPermission()
        }

        buttonCamera.setOnClickListener {
            requestCameraPermission()
        }

        // Analyze image when clicked
        buttonAnalyze.setOnClickListener {
            if (imageBitmap != null) {
                analyzeImage(imageBitmap!!)
            } else {
                Toast.makeText(this, "Pilih atau ambil gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to analyze the image using Retrofit and API Service
    private fun analyzeImage(bitmap: Bitmap) {
        Toast.makeText(this, "Mengirim gambar untuk analisis...", Toast.LENGTH_SHORT).show()

        // Convert Bitmap to byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        // Create request body for the image
        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)
        val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

        // Create API service instance
        val apiService: ApiService = ApiConfig.getApiService()

        // Make the network request
        apiService.analyzeImage(imagePart).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    try {
                        // Parse the response body to a string
                        val result = response.body()?.string()

                        // Parse the result string into AnalysisResult object
                        val gson = Gson()
                        val analysisResult = gson.fromJson(result, AnalysisResult::class.java)

                        // Show analysis result as Toast
                        Toast.makeText(this@CameraActivity,
                            "Makanan: ${analysisResult.food}, Kalori: ${analysisResult.calories}, Protein: ${analysisResult.protein}",
                            Toast.LENGTH_LONG).show()

                    } catch (e: Exception) {
                        Toast.makeText(this@CameraActivity, "Gagal menganalisis gambar", Toast.LENGTH_SHORT).show()
                        Log.e("CameraActivity", "Error parsing response", e)
                    }
                } else {
                    // Handle failure in the response
                    Toast.makeText(this@CameraActivity, "Gagal menganalisis gambar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle error
                Toast.makeText(this@CameraActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("CameraActivity", "Error analyzing image", t)
            }
        })
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

    private val openGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    previewImageView.setImageBitmap(bitmap)
                    imageBitmap = bitmap
                } catch (e: Exception) {
                    Log.e("CameraActivity", "Error loading image from gallery", e)
                    Toast.makeText(this, "Gagal memuat gambar dari galeri", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val openCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                if (bitmap != null) {
                    previewImageView.setImageBitmap(bitmap)
                    imageBitmap = bitmap
                } else {
                    Toast.makeText(this, "Gagal menangkap gambar", Toast.LENGTH_SHORT).show()
                }
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

    // Luncurkan aplikasi kamera
    private fun launchCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            openCameraLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(this, "Tidak ada aplikasi kamera yang tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_CODE_GALLERY = 100
        private const val REQUEST_CODE_CAMERA = 101
    }
}
