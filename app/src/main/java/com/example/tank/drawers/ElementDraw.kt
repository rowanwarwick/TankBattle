package com.example.tank.drawers

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.R
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
        drawView(coordinate)
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
}