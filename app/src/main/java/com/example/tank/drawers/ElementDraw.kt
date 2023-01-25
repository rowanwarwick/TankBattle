package com.example.tank.drawers

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.R
import com.example.tank.enums.Direction
import com.example.tank.enums.Material
import com.example.tank.models.Coordinate
import com.example.tank.models.Element

class ElementDraw(val container: ConstraintLayout) {

    var enterMaterial = Material.EMPTY
    private val elementsMaterial = mutableListOf<Element>()

    fun onTouchContainer(x:Float, y:Float) {
        val topMargin = y.toInt() - (y.toInt() % 50)
        val leftMargin = x.toInt() - (x.toInt() % 50)
        val coordinate = Coordinate(topMargin, leftMargin)
        if (enterMaterial == Material.EMPTY) {
            deleteView(coordinate)
        } else {
            drawOrReplaceView(coordinate)
        }
    }

    private fun deleteView(coordinate: Coordinate){
        val view = elementsMaterial.firstOrNull { it.coordinate== coordinate }
        if (view != null) {
            val viewDelete = container.findViewById<View>(view.viewId)
            container.removeView(viewDelete)
            elementsMaterial.remove(view)
        }
    }

    private fun replaceView(coordinate: Coordinate){
        deleteView(coordinate)
        drawView(coordinate)
    }

    private fun drawOrReplaceView(coordinate: Coordinate){
        val view = elementsMaterial.firstOrNull { it.coordinate == coordinate }
        if (view == null) {
            drawView(coordinate)
        } else if (view.material != enterMaterial) {
            replaceView(coordinate)
        }
    }

    private fun drawView( coordinate: Coordinate) {
        val view = ImageView(container.context)
        val layoutParams = ConstraintLayout.LayoutParams(50, 50)
        val viewId = View.generateViewId()
        when (enterMaterial) {
            Material.EMPTY -> {}
            Material.BRICK -> view.setImageResource(R.drawable.brick)
            Material.CONCRETE -> view.setImageResource(R.drawable.concrete)
            Material.GRASS -> view.setImageResource(R.drawable.grass)
        }
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        layoutParams.topToTop = container.id
        layoutParams.leftToLeft = container.id
        view.id = viewId
        view.layoutParams = layoutParams
        container.addView(view)
        elementsMaterial.add(Element(viewId, enterMaterial, coordinate))
    }

    fun move(myTank:View, direction: Direction) {
        val layoutParams = myTank.layoutParams as ConstraintLayout.LayoutParams
        val currentCoordTank = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        when (direction) {
            Direction.LEFT -> {
                myTank.rotation = 0f
                if (layoutParams.topMargin > 0)
                    layoutParams.topMargin -= 50
            }
            Direction.RIGHT -> {
                myTank.rotation = 180f
                if (layoutParams.topMargin + myTank.height < container.height)
                    layoutParams.topMargin += 50
            }
            Direction.DOWN -> {
                myTank.rotation = 270f
                if (layoutParams.leftMargin > 0)
                    layoutParams.leftMargin -= 50
            }
            Direction.UP -> {
                myTank.rotation = 90f
                if (layoutParams.leftMargin + myTank.width < container.width)
                    layoutParams.leftMargin += 50
            }
        }
        val nextCoordTank = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        if (checkTankCanMove(nextCoordTank, myTank)) {
            container.removeView(myTank)
            container.addView(myTank, 0)
        } else {
            layoutParams.topMargin = currentCoordTank.top
            layoutParams.leftMargin = currentCoordTank.left
        }
    }

    fun checkTankCanMove(coordinate: Coordinate, myTank: View):Boolean {
        var check = false
        if(coordinate.top >= 0 && coordinate.left >=0 && coordinate.top + myTank.height <= container.height && coordinate.left + myTank.width <= container.width)
            check = true
        if (check == true) {
            getTankCoordinate(coordinate).forEach{ coord ->
                val element = elementsMaterial.firstOrNull { it.coordinate== coord }
                if (element != null && !element.material.prorerty) {
                    check = false
                }
            }
        }
        return check
    }

    private fun getTankCoordinate(topLeftCoord:Coordinate):List<Coordinate>{
        val coordinate = mutableListOf<Coordinate>()
        coordinate.add(topLeftCoord)
        coordinate.add(Coordinate(topLeftCoord.top + 50, topLeftCoord.left))
        coordinate.add(Coordinate(topLeftCoord.top, topLeftCoord.left + 50))
        coordinate.add(Coordinate(topLeftCoord.top + 50, topLeftCoord.left + 50))
        return coordinate
    }
}