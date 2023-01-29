package com.example.tank.drawers

import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.enums.Direction
import com.example.tank.enums.Material
import com.example.tank.models.Element
import com.example.tank.models.Tank
import com.example.tank.utils.drawElement
import com.example.tank.utils.uniqId

class EnemyDraw(val container: ConstraintLayout,
                private val elements:MutableList<Element>) {
    val MAX_ENEMY = 10
    private var countEnemy = 0
    private var checkPlaceGenerateEnemy = 0
    private var start:Thread? = null
    val enemyTanks = mutableListOf<Tank>()

    fun searchEnemyBase():List<Element> {
        return elements.filter { it.material == Material.ENEMYBASE }
    }

    fun startBattle() {
        val enemyBase = searchEnemyBase()
        if (!(start != null && start!!.isAlive)) {
            countEnemy = elements.filter { it.material == Material.ENEMYTANK }.size
            start = Thread(Runnable {
                while (countEnemy < MAX_ENEMY) {
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
        elements.add(tankEnemy)
        drawElement(container, tankEnemy)
        checkPlaceGenerateEnemy++
        enemyTanks.add(Tank(tankEnemy, Direction.DOWN))
    }
    
    fun moveEnemy() {
        Thread(Runnable {
            while (true) {
                enemyTanks.removeAll(getKillTanks())
                enemyTanks.forEach { it.move(container, it.direction, elements) }
                Thread.sleep(400)
            }
        }).start()
    }

    private fun getKillTanks(): List<Tank> {
        val killedTanks = mutableListOf<Tank>()
        val allTank = elements.filter { it.material == Material.ENEMYTANK }
        enemyTanks.forEach {
            if (!allTank.contains(it.element))
                killedTanks.add(it)
        }
        return killedTanks
    }
}