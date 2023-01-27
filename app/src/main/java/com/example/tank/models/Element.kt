package com.example.tank.models

import com.example.tank.enums.Material

data class Element(val viewId: Int, val material: Material, val coordinate: Coordinate, val width:Int, val height:Int)