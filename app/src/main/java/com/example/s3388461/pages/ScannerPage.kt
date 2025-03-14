package com.example.s3388461.pages

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun ScannerPage(navController: NavController) {
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Camera launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            if (bitmap != null) {
                capturedBitmap = bitmap
            }
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { capturedBitmap?.let { convertImageToPDF(it) } },
                shape = RoundedCornerShape(50),
                content = { Icon(Icons.Default.Star, contentDescription = "Convert to PDF") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Scanner Page", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            capturedBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = { launcher.launch() }) {
                Text("Capture Image")
            }
        }
    }
}

// ✅ Function to Convert Image to PDF
fun convertImageToPDF(bitmap: Bitmap) {
    try {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        // Draw the image onto the PDF
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        // Define the file path
        val pdfFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "scanned_image.pdf"
        )

        // Write the PDF file
        val outputStream = FileOutputStream(pdfFile)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()

        Log.d("PDFConverter", "PDF saved at: ${pdfFile.absolutePath}")
    } catch (e: IOException) {
        Log.e("PDFConverter", "Error converting image to PDF: ${e.localizedMessage}")
    }
}
