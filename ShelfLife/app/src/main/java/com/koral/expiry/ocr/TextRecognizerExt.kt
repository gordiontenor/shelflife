package com.koral.expiry.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.tasks.await

suspend fun recognizeText(bitmap: Bitmap): String {
    val recognizer = TextRecognition.getClient()
    val image = InputImage.fromBitmap(bitmap, 0)
    val result = recognizer.process(image).await()
    return result.text
}
