package com.example.tank.drawers

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.R
import com.example.tank.enums.Direction
import com.example.tank.models.Coordinate
import com.example.tank.models.Element
import kotlin.text.Typography.bullet

class GunDraw(val container: ConstraintLayout) {

    fun bulletMove (myTank: View, direction: Direction) {
        Thread( Runnable {
            val bullet = bullet(myTank, direction)
            while (checkOutBullet(bullet, Coordinate(bullet.top, bullet.left))) {
                when (direction) {
                    Direction.UP -> (bullet.layoutParams as ConstraintLayout.LayoutParams).topMargin -= 25
                    Direction.DOWN -> (bullet.layoutParams as ConstraintLayout.LayoutParams).topMargin += 25
                    Direction.LEFT -> (bullet.layoutParams as ConstraintLayout.LayoutParams).leftMargin -= 25
                    Direction.RIGHT -> (bullet.layoutParams as ConstraintLayout.LayoutParams).leftMargin  += 25
                }
                Thread.sleep(30)
                (container.context as Activity).runOnUiThread{
                    container.removeView(bullet)
                    container.addView(bullet)
                }
            }
            (container.context as Activity).runOnUiThread {
                container.removeView(bullet)
            }
        }).start()
    }

    fun chooseDirection(elementsConteiner:MutableList<Element>, direction: Direction, bullet: Coordinate){
        when (direction) {
            Direction.UP, Direction.DOWN -> {
                coordBlockRightOrLeft(bullet)
            }
            Direction.LEFT, Direction.RIGHT -> {
                coordBlockTopOrDown(bullet)
            }
        }
    }

    fun checkElementInConteiner(elementsConteiner:List<Coordinate>, coordBlocks:List<Coordinate>): Boolean {
        coordBlocks.forEach {
            if (elementsConteiner.contains(it)) {
                return true
            }
        }
        return false
    }

    fun removeElemInConteiner() {

    }

    fun compareCollections(elementsConteiner:MutableList<Element>, coordBlocks:List<Coordinate>) {

    }

    fun coordBlockTopOrDown(coordinate: Coordinate):List<Coordinate> {
        val leftBlock = coordinate.left - coordinate.left % 50
        val rightBlock = leftBlock + 50
        val yCoord = coordinate.top - coordinate.top % 50
        return listOf(Coordinate(yCoord, leftBlock), Coordinate(yCoord, rightBlock))
    }

    fun coordBlockRightOrLeft(coordinate: Coordinate):List<Coordinate> {
        val topBlock = coordinate.top - coordinate.top % 50
        val downBlock = topBlock + 50
        val xCoord = coordinate.left - coordinate.left % 50
        return listOf(Coordinate(topBlock, xCoord), Coordinate(downBlock, xCoord))
    }

    fun checkOutBullet(bullet: ImageView, coordinate: Coordinate):Boolean {
        var res = false
        if (coordinate.top >=0 && coordinate.left >= 0 && coordinate.top + bullet.height <= container.height && coordinate.left + bullet.width <= container.width)
            res = true
        return res
    }

    fun bullet(myTank: View, direction: Direction):ImageView{
        return ImageView(container.context).apply {
            this.setImageResource(R.drawable.bullet)
            this.layoutParams = ConstraintLayout.LayoutParams(15, 25)
            val bulletCoord = bulletCoord(this, myTank, direction)
            (this.layoutParams as ConstraintLayout.LayoutParams).topMargin = bulletCoord.top
            (this.layoutParams as ConstraintLayout.LayoutParams).leftMargin = bulletCoord.left
            (this.layoutParams as ConstraintLayout.LayoutParams).topToTop = container.id
            (this.layoutParams as ConstraintLayout.LayoutParams).leftToLeft = container.id
            this.rotation = direction.rotation
        }
    }

    fun bulletCoord(bullet:ImageView, myTank: View, direction: Direction):Coordinate {
        val tankCoord = Coordinate(myTank.top, myTank.left)
        return when (direction) {
            Direction.UP -> {
                Coordinate(top = tankCoord.top - bullet.layoutParams.height,
                    left = tankCoord.left + 50 - bullet.layoutParams.width / 2)
            }
            Direction.DOWN -> {
                Coordinate(top = tankCoord.top + myTank.height,
                    left = tankCoord.left + 50 - bullet.layoutParams.width / 2)
            }
            Direction.RIGHT-> {
                Coordinate(top = tankCoord.top + 50 - bullet.layoutParams.height / 2,
                    left = tankCoord.left + myTank.width)
            }
            Direction.LEFT -> {
                Coordinate(top = tankCoord.top + 50 - bullet.layoutParams.height / 2,
                    left = tankCoord.left - bullet.layoutParams.width)
            }
        }
    }
}