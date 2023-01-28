package com.example.tank.drawers

import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.enums.Material
import com.example.tank.models.Element
import com.example.tank.utils.drawElement

class EnemyDraw(val container: ConstraintLayout) {
    val MAX_ENEMY = 5
    private var countEnemy = 0
    private var checkPlaceGenerateEnemy = 0

    fun searchEnemyBase(elements:List<Element>):List<Element> {
        return elements.filter { it.material == Material.ENEMYBASE }
    }

    fun startBattle(elements: MutableList<Element>) {
        val enemyBase = searchEnemyBase(elements)
        countEnemy = 0
        Thread(Runnable {
            while (countEnemy < MAX_ENEMY) {
                drawEnemy(elements,enemyBase)
                countEnemy++
                Thread.sleep(3000)
            }
        }).start()
    }

    private fun drawEnemy(elements: MutableList<Element>, enemyBaseCord:List<Element>) {
        val tankEnemy = Element(
            material = Material.ENEMYTANK,
            coordinate = enemyBaseCord[checkPlaceGenerateEnemy % enemyBaseCord.size].coordinate,
            width = Material.ENEMYTANK.width,
            height = Material.ENEMYTANK.height,
        )
        elements.add(tankEnemy)
        drawElement(container, tankEnemy)
        checkPlaceGenerateEnemy++
    }
}