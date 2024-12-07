package com.example.apichecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apichecker.ui.theme.ApiCheckerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserSpinnerScreen()
        }
    }
}


@Composable
fun UserSpinnerScreen() {
    val firstNames = remember { mutableStateOf(listOf<String>()) }
    val lastNames = remember { mutableStateOf(listOf<String>()) }

    // Trigger the AsyncTask to fetch users
    LaunchedEffect(Unit) {
        FetchUsersTask { users ->
            firstNames.value = users.map { it.firstName }
            lastNames.value = users.map { it.lastName }
        }.execute()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // First Name Spinner
        Text(text = "First Names:")
        Spinner(
            items = firstNames.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Last Name Spinner
        Text(text = "Last Names:")
        Spinner(
            items = lastNames.value,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Spinner(items: List<String>, modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf(items.firstOrNull() ?: "") }
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Trigger dropdown visibility on click
        Text(
            text = selectedItem,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { isExpanded = !isExpanded }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selectedItem = item
                        isExpanded = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}

