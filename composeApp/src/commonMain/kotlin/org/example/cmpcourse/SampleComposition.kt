package org.example.cmpcourse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun SampleComposition() {
    var isShown by remember {
        mutableStateOf(false)
    }
    var counter by remember {
        mutableStateOf(Random.nextInt())
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Button(onClick = {
            isShown = !isShown
        }) {
            Text(text = if (isShown) "Hide Square" else "Show Square")
        }

        CounterText(counter)

        if (isShown) {
            Box(modifier = Modifier.size(100.dp).background(Color.Blue))
        }
    }
}

@Composable
fun CounterText(counter: Int) {
    Text(text = counter.toString())
}