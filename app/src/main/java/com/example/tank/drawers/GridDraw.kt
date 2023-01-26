package com.example.tank.drawers

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.CELL_SIZE

class GridDraw(private val container: ConstraintLayout) {

    private val lines = mutableListOf<View>()

    fun drawGrid() {
        horizontal()
        vertical()
    }

    fun removeGrid() {
        lines.forEach {
            container.removeView(it)
        }
    }

    private fun horizontal() {
        var topMargin = 0
        while (topMargin <= container.height) {
            val line = View(container.context)
            val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 1)
            topMargin += CELL_SIZE
            params.topMargin = topMargin
            params.topToTop = container.id
            line.layoutParams = params
            line.setBackgroundColor(container.context.resources.getColor(android.R.color.white))
            container.addView(line)
            lines.add(line)
        }
    }

    private fun vertical() {
        var leftMargin = 0
        while (leftMargin <= container.width) {
            val line = View(container.context)
            val params = ConstraintLayout.LayoutParams(1, ConstraintLayout.LayoutParams.MATCH_PARENT)
            leftMargin += CELL_SIZE
            params.leftMargin = leftMargin
            params.leftToLeft = container.id
            line.layoutParams = params
            line.setBackgroundColor(container.context.resources.getColor(android.R.color.white))
            container.addView(line)
            lines.add(line)
        }
    }
}