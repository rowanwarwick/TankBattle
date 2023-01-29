package com.example.tank.models

import android.view.View
import com.example.tank.enums.Material

data class Element(
    var viewId: Int = View.generateViewId(),
    val material: Material,
    var coordinate: Coordinate,
    val width:Int = material.width,
    val height:Int = material.height)