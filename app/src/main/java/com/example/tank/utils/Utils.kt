package com.example.tank.utils

import com.example.tank.CELL_SIZE
import com.example.tank.models.Coordinate
import com.example.tank.models.Element

fun getElementOrNull(coordinate: Coordinate, elementsMaterial:List<Element>):Element? {
    for (element in elementsMaterial) {
        for (height in 0 until element.height) {
            for (width in 0 until element.width) {
                val searchCord = Coordinate(
                    top = element.coordinate.top + height * CELL_SIZE,
                    left = element.coordinate.left + width * CELL_SIZE
                )
                if (searchCord == coordinate)
                    return element
            }
        }
    }
    return null
}