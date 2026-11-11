package com.example.app_escalade.point

import android.media.Image
import com.example.app_escalade.Position

abstract class Point(
    val position: Position,
){
    abstract var imageRessource: Int
    abstract fun onClick()
}