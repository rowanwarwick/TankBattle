package com.example.tank.drawers

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.CELL_SIZE
import com.example.tank.enums.Material
import com.example.tank.models.Coordinate
import com.example.tank.models.Element
import com.example.tank.utils.getElementOrNull

class ElementDraw(val container: ConstraintLayout) {

    var enterMaterial = Material.EMPTY
    val elementsContainer = mutableListOf<Element>()

    fun onTouchContainer(x:Float, y:Float) {
        val topMargin = y.toInt() - (y.toInt() % CELL_SIZE)
        val leftMargin = x.toInt() - (x.toInt() % CELL_SIZE)
        val coordinate = Coordinate(topMargin, leftMargin)
        if (enterMaterial == Material.EMPTY) {
            deleteFromView(coordinate)
        } else {
            drawOrReplaceView(coordinate)
        }
    }

    private fun deleteFromView(coordinate: Coordinate){
        for (element in getElementsUnderBigElement(coordinate)) {
            deleteElement(element)
        }
    }

    private fun updateView(coordinate: Coordinate){
        deleteFromView(coordinate)
        drawView(coordinate)
    }

    private fun drawOrReplaceView(coordinate: Coordinate){
        val view = getElementOrNull(coordinate, elementsContainer)
        if (view == null) {
            drawView(coordinate)
        } else if (view.material != enterMaterial) {
            updateView(coordinate)
        }
    }

    private fun drawView(coordinate: Coordinate){
        val view = ImageView(container.context)
        val layoutParams = ConstraintLayout.LayoutParams(enterMaterial.width * CELL_SIZE, enterMaterial.height * CELL_SIZE)
        if (enterMaterial.count != 0) {
            val deleteElement = elementsContainer.filter { it.material == enterMaterial }
            if (deleteElement.size >= enterMaterial.count) deleteFromView(deleteElement[0].coordinate)
        }
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        layoutParams.topToTop = container.id
        layoutParams.leftToLeft = container.id
        enterMaterial.image?.let { view.setImageResource(it) }
        view.layoutParams = layoutParams
        val element = Element(material =  enterMaterial, coordinate = coordinate, width = enterMaterial.width, height = enterMaterial.height)
        view.id = element.viewId
        view.scaleType = ImageView.ScaleType.FIT_XY // растягивае изображения по ХУ
        container.addView(view)
        elementsContainer.add(element)
    }

    fun drawElementOnStartGame(elements:List<Element>?) {
        if (elements != null) {
            for (element in elements) {
                enterMaterial = element.material
                drawView(element.coordinate)
            }
        }
    }

    fun deleteElement(element:Element?) {
        if (element != null) {
            val viewDelete = container.findViewById<View>(element.viewId)
            container.removeView(viewDelete)
            elementsContainer.remove(element)
        }
    }

    fun getElementsUnderBigElement(coordinate: Coordinate):List<Element> {
        val elements = mutableListOf<Element>()
        for (element in elementsContainer) {
            for (height in 0 until enterMaterial.height) {
                for (width in 0 until enterMaterial.width) {
                    if (element.coordinate == Coordinate(
                            coordinate.top + height * CELL_SIZE,
                            coordinate.left + width * CELL_SIZE
                        )
                    )
                        elements.add(element)
                }
            }
        }
        return elements
    }
}