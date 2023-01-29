package com.example.tank.drawers

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.CELL_SIZE
import com.example.tank.R
import com.example.tank.enums.Direction
import com.example.tank.enums.Material
import com.example.tank.models.Coordinate
import com.example.tank.models.Element
import com.example.tank.models.Tank
import com.example.tank.utils.getElements
import com.example.tank.utils.getTanks
import kotlin.text.Typography.bullet

class GunDraw(val container: ConstraintLayout, val elementsConteiner: MutableList<Element>, val enemyDraw: EnemyDraw) {

    private var workThread = true
    private lateinit var tank: Tank

    fun bulletMove (tank: Tank) {
        this.tank = tank
        val direction = tank.direction
        val bulletThread = Thread( Runnable {
            val myTank = container.findViewById<View>(this.tank.element.viewId) ?: return@Runnable
            val bullet = bullet(myTank, direction)
            workThread = true
            while (checkOutBullet(bullet, Coordinate(bullet.top, bullet.left)) && workThread) {
                when (direction) {
                    Direction.UP -> (bullet.layoutParams as ConstraintLayout.LayoutParams).topMargin -= 25
                    Direction.DOWN -> (bullet.layoutParams as ConstraintLayout.LayoutParams).topMargin += 25
                    Direction.LEFT -> (bullet.layoutParams as ConstraintLayout.LayoutParams).leftMargin -= 25
                    Direction.RIGHT -> (bullet.layoutParams as ConstraintLayout.LayoutParams).leftMargin  += 25
                }
                Thread.sleep(30 )
                chooseDirection(direction, Coordinate((bullet.layoutParams as ConstraintLayout.LayoutParams).topMargin,
                    (bullet.layoutParams as ConstraintLayout.LayoutParams).leftMargin ))
                (container.context as Activity).runOnUiThread{
                    container.removeView(bullet)
                    container.addView(bullet, 0)
                }
            }
            (container.context as Activity).runOnUiThread {
                container.removeView(bullet)
            }
        })
        bulletThread.start()
    }

    fun coordBlockTopOrDown(coordinate: Coordinate):List<Coordinate> {
        val leftBlock = coordinate.left - coordinate.left % CELL_SIZE
        val rightBlock = leftBlock + CELL_SIZE
        val yCoord = coordinate.top - coordinate.top % CELL_SIZE
        return listOf(Coordinate(yCoord, leftBlock), Coordinate(yCoord, rightBlock))
    }

    fun coordBlockRightOrLeft(coordinate: Coordinate):List<Coordinate> {
        val topBlock = coordinate.top - coordinate.top % CELL_SIZE
        val downBlock = topBlock + CELL_SIZE
        val xCoord = coordinate.left - coordinate.left % CELL_SIZE
        return listOf(Coordinate(topBlock, xCoord), Coordinate(downBlock, xCoord))
    }

    fun removeElemInConteiner(element: Element) {
        if (!element.material.bulletCanGo) {
            if (!(tank.element.material == Material.ENEMYTANK && element.material == Material.ENEMYTANK))
                if (element.material.canDestroy) {
                    val activity = container.context as Activity
                    activity.runOnUiThread {
                        container.removeView(activity.findViewById(element.viewId))
                    }
                    elementsConteiner.remove(element)
                    removeTank(element)
                }
            workThread = false
        }
    }

    private fun removeTank(element: Element) {
        val tanksElement = enemyDraw.enemyTanks.map { it.element }
        val tankIndex = tanksElement.indexOf(element)
        enemyDraw.removeTank(tankIndex)
    }

    fun compareCollections(cordBlocks:List<Coordinate>) {
        for (coordinate in cordBlocks) {
            var element = getElements(coordinate, elementsConteiner)
            if (element.isEmpty())
                element = getTanks(coordinate, enemyDraw.enemyTanks)
            element.forEach { if (it != tank.element) removeElemInConteiner(it) }
        }
    }

    fun chooseDirection(direction: Direction, bullet: Coordinate){
        when (direction) {
            Direction.UP, Direction.DOWN -> {
                compareCollections(coordBlockTopOrDown(bullet))
            }
            Direction.LEFT, Direction.RIGHT -> {
                compareCollections(coordBlockRightOrLeft(bullet))
            }
        }
    }

    fun checkOutBullet(bullet: ImageView, coordinate: Coordinate):Boolean {
        var res = false
        if (coordinate.top >= 0 && coordinate.left >= 0 &&
            coordinate.top + bullet.height <= container.height && coordinate.left + bullet.width <= container.width)
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
                    left = tankCoord.left + CELL_SIZE - bullet.layoutParams.width / 2)
            }
            Direction.DOWN -> {
                Coordinate(top = tankCoord.top + myTank.height,
                    left = tankCoord.left + CELL_SIZE - bullet.layoutParams.width / 2)
            }
            Direction.RIGHT-> {
                Coordinate(top = tankCoord.top + CELL_SIZE - bullet.layoutParams.height / 2,
                    left = tankCoord.left + myTank.width)
            }
            Direction.LEFT -> {
                Coordinate(top = tankCoord.top + CELL_SIZE - bullet.layoutParams.height / 2,
                    left = tankCoord.left - bullet.layoutParams.width)
            }
        }
    }
}