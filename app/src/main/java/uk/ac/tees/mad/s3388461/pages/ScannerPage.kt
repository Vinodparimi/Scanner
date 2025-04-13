package uk.ac.tees.mad.s3388461.pages

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerPage(navController: NavController) {
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var pdfFilePath by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions[Manifest.permission.CAMERA] == true
            if (granted) {
                showCamera = true
            } else {
                errorMessage = "Camera permission is required."
            }
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val cameraPermission = Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(context, cameraPermission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsLauncher.launch(arrayOf(cameraPermission))
                } else {
                    showCamera = true
                }
            }) {
                Icon(Icons.Default.Star, contentDescription = "Capture Image")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showCamera) {
                CameraCaptureView(
                    onImageCaptured = {
                        capturedImage = it
                        showCamera = false
                    },
                    modifier = Modifier.height(400.dp)
                )
            } else {
                Text("Scanner Page", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))

                capturedImage?.let { bitmap ->
                    Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.size(250.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { pdfFilePath = saveImageAsPdf(context, bitmap) }) {
                        Text("Save as PDF")
                    }
                }

                pdfFilePath?.let { path ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("PDF Saved Successfully!")
                    Button(onClick = { openPdf(context, path) }) {
                        Text("Open PDF")
                    }
                }

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun CameraCaptureView(onImageCaptured: (Bitmap) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetResolution(Size(1920, 1080))
            .build()
    }

    AndroidView(factory = { previewView }, modifier = modifier.fillMaxWidth())

    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
            Log.e("CameraX", "Camera initialization failed", e)
        }
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                val photoFile = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "captured_${System.currentTimeMillis()}.jpg"
                )
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                            onImageCaptured(bitmap)
                        }

                        override fun onError(e: ImageCaptureException) {
                            Log.e("CameraX", "Capture failed", e)
                        }
                    }
                )
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text("Take Picture")
        }
    }
}

fun saveImageAsPdf(context: Context, bitmap: Bitmap): String? {
    return try {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "scanned_image.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()
        file.absolutePath
    } catch (e: IOException) {
        Log.e("PDF", "Error saving PDF: ${e.message}")
        null
    }
}

fun openPdf(context: Context, filePath: String) {
    val file = File(filePath)
    val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
    }
    context.startActivity(intent)
}
