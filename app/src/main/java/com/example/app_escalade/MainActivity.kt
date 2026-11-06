package com.example.app_escalade

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.app_escalade.ui.theme.App_escaladeTheme
import kotlin.math.ln

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

fun limitMouvement(offset: Offset, scale: Float, screenSize: IntSize): Offset{
    val maxX = screenSize.width * (scale - 1) / 2
    val maxY = screenSize.height * (scale - 1) / 2

    return Offset(
        x = offset.x.coerceIn(-maxX, maxX),
        y = offset.y.coerceIn(-maxY, maxY)
    )
}

@Composable
fun ShowMap(painter: Painter, points: List<Point>, modifier: Modifier = Modifier){
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var screenSize by remember { mutableStateOf(IntSize.Zero) }


    val transformState = rememberTransformableState {
        zoomChange, panChange, _ ->

        scale = (scale * zoomChange).coerceIn(1f, 5f)
        offset = limitMouvement(
            offset + panChange,
            scale,
            screenSize
        )
    }


    Box(modifier
        .fillMaxSize()
        .clipToBounds()
        .onSizeChanged{
            screenSize = it
        }
        .transformable(state = transformState),
        contentAlignment = Alignment.Center){

        BoxWithConstraints(Modifier
            .fillMaxSize()
            .aspectRatio(1.5f)
            .graphicsLayer{
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }) {
            Image(
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            points.forEach { point ->
                val markerScale = 1f / (1f + 0.5f * ln(scale.toDouble())).toFloat()

                Box(
                    modifier = Modifier
                        .offset(
                        x = maxWidth * point.position.x,
                        y = maxHeight * point.position.y
                        )
                        .size(10.dp)
                        .graphicsLayer{
                            scaleX = markerScale
                            scaleY = markerScale
                        }
                        .clickable{
                            Log.i("APP", "Point appuyer")
                        }
                ){
                    Image(painterResource(R.drawable.ic_launcher_background), contentDescription = "")
                }
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