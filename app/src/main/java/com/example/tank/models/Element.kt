package com.example.tank.models

import android.view.View
import com.example.tank.enums.Material

data class Element(val viewId: Int = View.generateViewId(), val material: Material, val coordinate: Coordinate, val width:Int, val height:Int)