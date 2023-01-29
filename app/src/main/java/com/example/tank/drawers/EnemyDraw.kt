package com.example.tank.drawers

import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.enums.Direction
import com.example.tank.enums.Material
import com.example.tank.models.Element
import com.example.tank.models.Tank
import com.example.tank.utils.drawElement
import com.example.tank.utils.randomizer
import com.example.tank.utils.uniqId

class EnemyDraw(val container: ConstraintLayout,
                private val elements:MutableList<Element>) {
    val maxEnemy = 10
    private var countEnemy = 0
    private var checkPlaceGenerateEnemy = 0
    private var start:Thread? = null
    private var moveAllTanksThread:Thread? = null
    val enemyTanks = mutableListOf<Tank>()

    fun searchEnemyBase():List<Element> {
        return elements.filter { it.material == Material.ENEMYBASE }
    }

    fun startBattle() {
        val enemyBase = searchEnemyBase()
        if (!(start != null && start!!.isAlive)) {
            countEnemy = elements.filter { it.material == Material.ENEMYTANK }.size
            start = Thread(Runnable {
                while (countEnemy < maxEnemy) {
                    drawEnemy(enemyBase)
                    countEnemy++
                    Thread.sleep(3000)
                }
            })
            start!!.start()
            moveEnemy()
        }
    }

    private fun drawEnemy(enemyBaseCord:List<Element>) {
        val tankEnemy = Element(
            material = Material.ENEMYTANK,
            coordinate = enemyBaseCord[checkPlaceGenerateEnemy % enemyBaseCord.size].coordinate
        )
        uniqId(tankEnemy, elements)
        drawElement(container, tankEnemy)
        checkPlaceGenerateEnemy++
        enemyTanks.add(Tank(tankEnemy, Direction.DOWN, GunDraw(container, elements, this)))
    }
    
    fun moveEnemy() {
        Thread(Runnable {
            while (true) { // нужно условие для закрытия потока
                goThroughAllTanks()
                Thread.sleep(400)
            }
        }).start()
    }

    fun goThroughAllTanks() {
        moveAllTanksThread = Thread(Runnable {
            enemyTanks.forEach {
                it.move(container, it.direction, elements)
                /*if (randomizer(10))*/ it.bullet.bulletMove(it)
            }
        })
        moveAllTanksThread?.start()
    }

    fun removeTank(tankIndex: Int) {
        if (tankIndex >= 0) {
            moveAllTanksThread?.join()
            enemyTanks.removeAt(tankIndex)
        }
    }
}