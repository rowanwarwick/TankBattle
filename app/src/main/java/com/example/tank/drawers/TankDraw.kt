package com.example.tank.drawers

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.CELL_SIZE
import com.example.tank.enums.Direction
import com.example.tank.models.Coordinate
import com.example.tank.models.Element

class TankDraw(val container: ConstraintLayout) {

    var currectDirection = Direction.UP

}