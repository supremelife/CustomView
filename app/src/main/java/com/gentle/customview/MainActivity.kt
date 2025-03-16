package com.gentle.customview

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.gentle.customview.ui.theme.CustomViewTheme
import com.gentle.customview.widget.PieChartView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Button(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(40.dp),
            onClick = {
                val intent = Intent(context, PieChartActivity::class.java)
                context.startActivity(intent)
            }) {
            Text("Pie chart")
        }
        val size = with(LocalDensity.current) { 400.dp.toPx() }
        val height = with(LocalDensity.current) { 190.dp.toPx() }
        AndroidView(
            factory = { context ->
                PieChartView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        size.toInt(),
                        height.toInt()
                    )
                    angle = 120
                }
            },
            modifier = Modifier
                .background(color = Color.LightGray)
                .padding(top = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomViewTheme {
        Greeting("Android")
    }
}