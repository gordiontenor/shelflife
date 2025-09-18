package com.koral.expiry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koral.expiry.di.Di
import com.koral.expiry.notify.Notif
import com.koral.expiry.ui.AddScreen
import com.koral.expiry.ui.ListScreen
import com.koral.expiry.worker.Scheduler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Di.init(applicationContext)
        Notif.ensureChannel(this)
        Scheduler.scheduleDaily(this)

        setContent {
            val nav = rememberNavController()
            NavHost(navController = nav, startDestination = "list") {
                composable("list") { ListScreen(onAdd = { nav.navigate("add") }) }
                composable("add")  { AddScreen(onDone = { nav.popBackStack() }) }
            }
        }
    }
}
