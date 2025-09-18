package com.koral.expiry.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.koral.expiry.data.FoodItem

@Composable
fun ListScreen(onAdd: () -> Unit, vm: MainViewModel = viewModel(factory = VmFactory.default())) {
    val items by vm.items.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) { Text("+") }
        }
    ) { pad ->
        LazyColumn(Modifier.padding(pad)) {
            items(items.size) { ix ->
                val it = items[ix]
                ListRow(it = it, onDelete = { vm.delete(it) })
                Divider()
            }
        }
    }
}

@Composable
private fun ListRow(it: FoodItem, onDelete: () -> Unit) {
    val expiry = it.expiryDate?.toString() ?: "—"
    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f)) {
            Text(it.name, style = MaterialTheme.typography.titleMedium)
            Text("SKT: $expiry", style = MaterialTheme.typography.bodySmall)
        }
        TextButton(onClick = onDelete) { Text("Sil") }
    }
}
