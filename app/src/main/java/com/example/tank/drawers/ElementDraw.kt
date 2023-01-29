package com.example.tank.drawers

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.CELL_SIZE
import com.example.tank.enums.Material
import com.example.tank.models.Coordinate
import com.example.tank.models.Element
import com.example.tank.utils.drawElement
import com.example.tank.utils.getElements
import com.example.tank.utils.uniqId

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
        createDrawView(coordinate)
    }

    private fun drawOrReplaceView(coordinate: Coordinate){
        val view = getElements(coordinate, elementsContainer)
        if (view.isEmpty()) {
            createDrawView(coordinate)
        } else if (view[0].material != enterMaterial) {
            updateView(coordinate)
        }
    }

    private fun drawView(element: Element) {
        if (enterMaterial.count != 0) {
            val deleteElement = elementsContainer.filter { it.material == enterMaterial }
            if (deleteElement.size >= enterMaterial.count) deleteFromView(deleteElement[0].coordinate)
        }
        drawElement(container, element)
        elementsContainer.add(element)
    }

    private fun createDrawView(coordinate: Coordinate){
        val element = Element(material =  enterMaterial, coordinate = coordinate)
        uniqId(element, elementsContainer)
        drawView(element)
    }

    fun drawElementOnStartGame(elements:List<Element>?) {
        if (elements != null) {
            for (element in elements) {
                enterMaterial = element.material
                drawView(element)
            }
        }
        enterMaterial = Material.EMPTY
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

    fun hideElementInGame(editMode: Boolean) {
        elementsContainer.filter { !it.material.visibleInPlay }.forEach { changeElementVisibility(it.viewId, editMode) }
    }

    private fun changeElementVisibility(viewId: Int, editMode: Boolean) {
        val view = container.findViewById<View>(viewId)
        if (editMode) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}