package com.koral.expiry.ui

import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.koral.expiry.ocr.OcrParser
import com.koral.expiry.ocr.recognizeText
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun AddScreen(onDone: () -> Unit, vm: MainViewModel = viewModel(factory = VmFactory.default())) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf<LocalDate?>(null) }
    var lastText by remember { mutableStateOf("") }

    val controller = remember { LifecycleCameraController(ctx) }

    Scaffold(topBar = { TopAppBar(title = { Text("Yeni Ürün") }) },
        bottomBar = {
            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    vm.add(name.ifBlank { "Ürün" }, expiry, null)
                    onDone()
                }) { Text("Kaydet") }
                OutlinedButton(onClick = onDone) { Text("Vazgeç") }
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            AndroidView(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                factory = {
                    val pv = PreviewView(it)
                    controller.bindToLifecycle(it as androidx.lifecycle.LifecycleOwner)
                    pv.controller = controller
                    pv
                }
            )
            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    scope.launch {
                        // NOTE: Demo amaçlı "bitmap alma" kısayolu.
                        val bitmap = Bitmap.createBitmap(1280, 720, Bitmap.Config.ARGB_8888)
                        val text = recognizeText(bitmap)
                        lastText = text
                        val dates = OcrParser.extractCandidateDates(text)
                        val first = dates.firstOrNull()?.let(OcrParser::normalizeDate)
                        if (first != null) expiry = first
                        if (name.isBlank()) name = OcrParser.guessName(text)
                    }
                }) { Text("Foto’dan Oku") }

                TextButton(onClick = { if (lastText.isNotBlank()) {
                    val candidates = OcrParser.extractCandidateDates(lastText).mapNotNull(OcrParser::normalizeDate)
                    if (candidates.size >= 2) expiry = candidates[1]
                }}) { Text("Farklı Tarih") }
            }

            OutlinedTextField(value = name, onValueChange = { name = it },
                label = { Text("Ürün adı") }, modifier = Modifier.fillMaxWidth().padding(12.dp))
            OutlinedTextField(value = expiry?.toString() ?: "",
                onValueChange = { runCatching { expiry = LocalDate.parse(it) } },
                label = { Text("SKT (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp))
        }
    }
}
