package com.example.fridgemate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.fridgemate.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScanInvertory : AppCompatActivity() {

    private lateinit var result: TextView
    private lateinit var imageView: ImageView
    private lateinit var picture: Button
    private val imageSize = 224

    private val cameraLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val image = result.data?.extras?.get("data") as? Bitmap
                image?.let {
                    val dimension = Math.min(it.width, it.height)
                    val thumbnail = ThumbnailUtils.extractThumbnail(it, dimension, dimension)
                    imageView.setImageBitmap(thumbnail)

                    val resizedImage = Bitmap.createScaledBitmap(thumbnail, imageSize, imageSize, false)
                    classifyImage(resizedImage)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_invertory) // Ensure this matches your layout file

        result = findViewById(R.id.result)
        imageView = findViewById(R.id.imageView)
        picture = findViewById(R.id.scan_inventory_button)

        picture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(cameraIntent)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }
        }

        // Setup Bottom Navigation View
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_scan -> {
                    // Open the Scan Inventory activity
                    startActivity(Intent(this, ScanInvertory::class.java))
                    true
                }
                R.id.nav_fridge -> {
                    // Open the Fridge Inventory activity
                    startActivity(Intent(this, FridgeInventory::class.java))
                    true
                }
                R.id.nav_store -> {
                    // Open the Store Locator activity
                    startActivity(Intent(this, StoreLocator::class.java))
                    true
                }
                R.id.nav_res -> {
                    // Open the Recipes activity
                    startActivity(Intent(this, Recipes::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val model = Model.newInstance(applicationContext)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val value = intValues[pixel++]
                    byteBuffer.putFloat(((value shr 16) and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat(((value shr 8) and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((value and 0xFF) * (1f / 255f))
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences = outputFeature0.floatArray
            val maxPos = confidences.indices.maxByOrNull { confidences[it] } ?: -1
            val classes = arrayOf("Apple", "Milk")

            result.text = if (maxPos != -1) classes[maxPos] else "Unknown"

            model.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(cameraIntent)
            } else {
                // Handle permission denial
                // Maybe show a message to the user
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }
}
