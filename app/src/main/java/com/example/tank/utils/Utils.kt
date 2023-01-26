package com.example.tank.utils

import com.example.tank.models.Coordinate
import com.example.tank.models.Element

fun getElementOrNull(coordinate: Coordinate, elementsMaterial:List<Element>):Element? {
    for (element in elementsMaterial) {
        for (height in 0 until element.height) {
            for (width in 0 until element.width) {
                val searchCord = Coordinate(
                    top = element.coordinate.top + height * 50,
                    left = element.coordinate.left + width * 50
                )
                if (searchCord == coordinate)
                    return element
            }
        }
    }
    return null
}