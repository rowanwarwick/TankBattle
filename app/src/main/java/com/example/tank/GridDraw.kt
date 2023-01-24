package com.example.tank

import android.app.Activity
import android.content.Context
import android.view.View

class GridDraw(private val contex: Context) {
    fun drawGrid() {
        val box = (contex as Activity).findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.container)
        horizontal(box)
        vertical(box)
    }

    private fun horizontal(container: androidx.constraintlayout.widget.ConstraintLayout) {
        var topMargin = 0
        while (topMargin <= container.height) {
            val line = View(contex)
            val params = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT, 1)
            topMargin += 50
            params.topMargin = topMargin
            params.topToTop = container.id
            line.layoutParams = params
            line.setBackgroundColor(contex.resources.getColor(android.R.color.white))
            container.addView(line)
        }
    }

    private fun vertical(container: androidx.constraintlayout.widget.ConstraintLayout) {
        var leftMargin = 0
        while (leftMargin <= container.width) {
            val line = View(contex)
            val params = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(1, androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT)
            leftMargin += 50
            params.leftMargin = leftMargin
            params.leftToLeft = container.id
            line.layoutParams = params
            line.setBackgroundColor(contex.resources.getColor(android.R.color.white))
            container.addView(line)
        }
    }
}