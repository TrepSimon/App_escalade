package com.example.app_escalade

import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.scrollableArea
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_escalade.ui.theme.App_escaladeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_escaladeTheme {
                App()
            }
        }
    }
}

@Composable
fun App(){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        GeneralMap(Modifier.padding(innerPadding))
    }
}

@Composable
fun ShowMap(painter: Painter, points: List<Point>, modifier: Modifier = Modifier){
    BoxWithConstraints(modifier) {
        Image(
            painter = painter,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        points.forEach { point ->
            Box(
                modifier = Modifier
                    .offset(
                    x = maxWidth * point.position.x,
                    y = maxHeight * point.position.y
                    )
                    .size(40.dp)
                    .clickable{
                        Log.i("APP", "Point appuyer")
                    }
            ){
                Image(painterResource(R.drawable.ic_launcher_background), contentDescription = "")
            }
        }
    }
}

@Composable
fun GeneralMap(modifier: Modifier = Modifier){
    val points = mutableListOf<Point>()

    val point = Point(Position(0.5f,0.5f))
    points.add(point);
    ShowMap(painterResource(R.drawable.screenshot_2026_11_05_085348), points, modifier = modifier);
}