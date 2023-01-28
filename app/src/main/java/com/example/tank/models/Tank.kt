package com.example.tank.models

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.CELL_SIZE
import com.example.tank.enums.Direction
import com.example.tank.utils.getElements

class Tank(val element: Element, var direction: Direction) {
    fun move(container:ConstraintLayout, direction: Direction, elementsMaterial:List<Element>) {
        val view = container.findViewById<View>(element.viewId) ?: return
        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        val currentCoordTank = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        this.direction = direction
        view.rotation = direction.rotation
        val nextCoordTank = nextTankCord(container, view)
        if (checkPossibleMove(container, nextCoordTank, view, elementsMaterial)) {
            container.removeView(view)
            container.addView(view, 0)
            element.coordinate = nextCoordTank
        } else {
            element.coordinate = currentCoordTank
            layoutParams.topMargin = currentCoordTank.top
            layoutParams.leftMargin = currentCoordTank.left
        }
    }

    fun nextTankCord(container: ConstraintLayout, view: View):Coordinate {
        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        when (direction) {
            Direction.UP -> {
                if (layoutParams.topMargin > 0)
                    layoutParams.topMargin -= CELL_SIZE
            }
            Direction.DOWN -> {
                if (layoutParams.topMargin + view.height < container.height)
                    layoutParams.topMargin += CELL_SIZE
            }
            Direction.LEFT -> {
                if (layoutParams.leftMargin > 0)
                    layoutParams.leftMargin -= CELL_SIZE
            }
            Direction.RIGHT -> {
                if (layoutParams.leftMargin + view.width < container.width)
                    layoutParams.leftMargin += CELL_SIZE
            }
        }
        return Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
    }

    fun checkPossibleMove(container: ConstraintLayout ,coordinate: Coordinate, view: View, elementsMaterial:List<Element>):Boolean {
        var check = false
        if (coordinate.top >= 0 && coordinate.left >= 0 &&
            coordinate.top + view.height <= container.height && coordinate.left + view.width <= container.width)
            check = true
        if (check == true) {
            for (anyCord in getTankCoordinate(coordinate)){
                val elementsOrNull = getElements(anyCord, elementsMaterial)
                println("элементы в ячейке ${anyCord.top} ${anyCord.left} ${elementsOrNull}")
                if (elementsOrNull.firstOrNull{it.material.tankCanGo == false} != null) {
                    if (elementsOrNull.firstOrNull{it == element} == null) check = false
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