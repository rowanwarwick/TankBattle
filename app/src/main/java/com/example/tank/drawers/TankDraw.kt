package com.example.tank.drawers

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.CELL_SIZE
import com.example.tank.enums.Direction
import com.example.tank.models.Coordinate
import com.example.tank.models.Element

class TankDraw(val container: ConstraintLayout) {

    var currectDirection = Direction.UP

    fun move(myTank: View, direction: Direction, elementsMaterial:List<Element>) {
        val layoutParams = myTank.layoutParams as ConstraintLayout.LayoutParams
        val currentCoordTank = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        currectDirection = direction
        myTank.rotation = direction.rotation
        when (direction) {
            Direction.UP -> {
                if (layoutParams.topMargin > 0)
                    layoutParams.topMargin -= CELL_SIZE
            }
            Direction.DOWN -> {
                if (layoutParams.topMargin + myTank.height < container.height)
                    layoutParams.topMargin += CELL_SIZE
            }
            Direction.LEFT -> {
                if (layoutParams.leftMargin > 0)
                    layoutParams.leftMargin -= CELL_SIZE
            }
            Direction.RIGHT -> {
                if (layoutParams.leftMargin + myTank.width < container.width)
                    layoutParams.leftMargin += CELL_SIZE
            }
        }
        val nextCoordTank = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        if (checkPossibleMove(nextCoordTank, myTank, elementsMaterial)) {
            container.removeView(myTank)
            container.addView(myTank, 0)
        } else {
            layoutParams.topMargin = currentCoordTank.top
            layoutParams.leftMargin = currentCoordTank.left
        }
    }

    fun checkPossibleMove(coordinate: Coordinate, view: View, elementsMaterial:List<Element>):Boolean {
        var check = false
        if(coordinate.top >= 0 && coordinate.left >= 0 &&
            coordinate.top + view.height <= container.height && coordinate.left + view.width <= container.width)
            check = true
        if (check == true) {
            getTankCoordinate(coordinate).forEach{ coord ->
                val element = elementsMaterial.firstOrNull { it.coordinate== coord }
                if (element != null && !element.material.tankCanGo) {
                    check = false
                }
            }
        }
        return check
    }

    private fun getTankCoordinate(topLeftCoord: Coordinate):List<Coordinate>{
        val coordinate = mutableListOf<Coordinate>()
        coordinate.add(topLeftCoord)
        coordinate.add(Coordinate(topLeftCoord.top + CELL_SIZE, topLeftCoord.left))
        coordinate.add(Coordinate(topLeftCoord.top, topLeftCoord.left + CELL_SIZE))
        coordinate.add(Coordinate(topLeftCoord.top + CELL_SIZE, topLeftCoord.left + CELL_SIZE))
        return coordinate
    }
}