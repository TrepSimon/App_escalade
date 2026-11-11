package com.example.app_escalade.point

import android.media.Image
import android.util.Log
import com.example.app_escalade.Position
import com.example.app_escalade.R

class SectionPoint(position: Position) : Point(position){
    override var imageRessource: Int = R.drawable.ic_launcher_background
    override fun onClick() {
        Log.i("Section point log", "click");
    }
}