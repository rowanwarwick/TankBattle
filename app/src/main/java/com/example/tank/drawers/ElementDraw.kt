package com.example.tank.drawers

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.R
import com.example.tank.enums.Material
import com.example.tank.models.Coordinate
import com.example.tank.models.Element
import com.example.tank.utils.getElementOrNull

class ElementDraw(val container: ConstraintLayout) {

    var enterMaterial = Material.EMPTY
    val elementsMaterial = mutableListOf<Element>()

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
        val view = getElementOrNull(coordinate, elementsMaterial)
//        val view = elementsMaterial.firstOrNull { it.coordinate== coordinate }
        if (view != null) {
            val viewDelete = container.findViewById<View>(view.viewId)
            container.removeView(viewDelete)
            elementsMaterial.remove(view)
        }
    }

    private fun replaceView(coordinate: Coordinate){
        deleteView(coordinate)
        selectMaterial(coordinate)
    }

    private fun drawOrReplaceView(coordinate: Coordinate){
        val view = getElementOrNull(coordinate, elementsMaterial)
//        val view = elementsMaterial.firstOrNull { it.coordinate == coordinate }
        if (view == null) {
            selectMaterial(coordinate)
        } else if (view.material != enterMaterial) {
            replaceView(coordinate)
        }
    }

    private fun selectMaterial(coordinate: Coordinate) {
        when (enterMaterial) {
            Material.EAGLE -> {
                elementsMaterial.firstOrNull { it.material == Material.EAGLE }?.coordinate?.let { deleteView(it) }
                drawView(R.drawable.eagle, coordinate, 4, 4)
            }
            Material.BRICK -> drawView(R.drawable.brick, coordinate)
            Material.CONCRETE -> drawView(R.drawable.concrete, coordinate)
            Material.GRASS -> drawView(R.drawable.grass, coordinate)
            Material.EMPTY -> {}
        }
    }

    private fun drawView(image:Int, coordinate: Coordinate, width: Int = 1, height: Int = 1){
        val view = ImageView(container.context)
        val layoutParams = ConstraintLayout.LayoutParams(width * 50, height * 50)
        val viewId = View.generateViewId()
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        layoutParams.topToTop = container.id
        layoutParams.leftToLeft = container.id
        view.setImageResource(image)
        view.id = viewId
        view.layoutParams = layoutParams
        container.addView(view)
        elementsMaterial.add(Element(viewId, enterMaterial, coordinate, width, height))
    }
}