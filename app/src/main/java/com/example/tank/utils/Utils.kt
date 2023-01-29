package com.example.tank.utils

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.CELL_SIZE
import com.example.tank.models.Coordinate
import com.example.tank.models.Element

fun getElements(coordinate: Coordinate, elementsMaterial:List<Element>):List<Element> {
    val list = mutableListOf<Element>()
    for (element in elementsMaterial) {
        loop@ for (height in 0 until element.height) {
            for (width in 0 until element.width) {
                val searchCord = Coordinate(
                    top = element.coordinate.top + height * CELL_SIZE,
                    left = element.coordinate.left + width * CELL_SIZE
                )
                if (searchCord == coordinate){
                    list.add(element)
                    break@loop
                }
            }
        }
    }
    return list
}

fun drawElement(container: ConstraintLayout, element: Element){
    val view = ImageView(container.context)
    val layoutParams = ConstraintLayout.LayoutParams(element.width * CELL_SIZE, element.height * CELL_SIZE)
    layoutParams.topMargin = element.coordinate.top
    layoutParams.leftMargin = element.coordinate.left
    layoutParams.topToTop = container.id
    layoutParams.leftToLeft = container.id
    element.material.image?.let { view.setImageResource(it) }
    view.layoutParams = layoutParams
    view.id = element.viewId
    view.scaleType = ImageView.ScaleType.FIT_XY // растягивае изображения по ХУ
    (container.context as Activity).runOnUiThread {
        container.addView(view)
    }
}

fun uniqId(element: Element, elementsContainer: List<Element>) {
    while (elementsContainer.map { it.viewId }.contains(element.viewId))
        element.viewId = View.generateViewId()
}