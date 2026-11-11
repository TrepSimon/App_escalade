package com.example.app_escalade.point

import android.media.Image
import android.util.Log
import com.example.app_escalade.Position

class SectionPoint(position: Position, override var imageRessource: Int) : Point(position){
    override fun onClick() {
        Log.i("Section point log", "click");
    }
}