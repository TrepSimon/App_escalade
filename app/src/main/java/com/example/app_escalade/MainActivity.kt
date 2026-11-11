package com.example.app_escalade

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.app_escalade.point.Point
import com.example.app_escalade.point.SectionPoint
import com.example.app_escalade.ui.theme.App_escaladeTheme
import kotlin.collections.emptyList
import kotlin.collections.mutableListOf
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
fun ShowPoint(point: Point, scale: Float, maxWidth: Dp, maxHeight: Dp){
    val markerScale = 1f / (1f + 0.5f * ln(scale.toDouble())).toFloat()

    Box(
        modifier = Modifier
            .offset(
                x = maxWidth * point.position.x,
                y = maxHeight * point.position.y
            )
            .size(10.dp)
            .graphicsLayer {
                scaleX = markerScale
                scaleY = markerScale
            }
            .clickable {
                point.onClick()
            }
    ){
        Image(painterResource(point.imageRessource), contentDescription = "")
    }
}


fun AddPoint(position: Position): Point{
    return SectionPoint(position, R.drawable.ic_launcher_background)
}
@Composable
fun ShowMap(painter: Painter, points: List<Point>, modifier: Modifier = Modifier, onPointAdded: (Point) -> Unit){
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
        .onSizeChanged {
            screenSize = it
        }
        .transformable(state = transformState),
        contentAlignment = Alignment.Center){

        BoxWithConstraints(Modifier
            .fillMaxSize()
            .aspectRatio(1.5f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }) {
            Image(
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { offset ->
                                onPointAdded(AddPoint(Position(offset.x/maxWidth.toPx(), offset.y/maxHeight.toPx())))
                            }
                        )
                    }
            )

            points.forEach { point ->
                ShowPoint(point, scale, maxWidth, maxHeight)
            }
        }
    }
}

@Composable
fun GeneralMap(modifier: Modifier = Modifier){
    val points = remember { mutableStateListOf<Point>() }

    LaunchedEffect(Unit) {
        val point = SectionPoint(Position(0.5f, 0.5f), R.drawable.ic_launcher_background)
        points.add(point);
    }

    ShowMap(painterResource(R.drawable.screenshot_2026_11_05_085348), points, modifier = modifier, onPointAdded = {
        points.add(it)
        Log.i("Ajout", "point ajouter a ${it.position}///${points.size}")
    });
}