package com.example.tank

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.tank.databinding.ActivityMainBinding
import com.example.tank.drawers.*
import com.example.tank.enums.Direction
import com.example.tank.enums.Material
import com.example.tank.models.Coordinate
import com.example.tank.models.Element
import com.example.tank.models.Tank

const val CELL_SIZE = 50

class MainActivity : AppCompatActivity() {
    private var editMode = false
    private val tankPlayer by lazy {
        Tank(
            Element(material = Material.PLAYER,
                coordinate = elementDraw.elementsContainer
                    .filter { it.material == Material.OURRESPAWN }
                    .getOrNull(0)?.coordinate ?: Coordinate(0, 0)
            ), Direction.UP, GunDraw(binding.container, elementDraw.elementsContainer, enemyDraw)
        )
    }
    private lateinit var binding: ActivityMainBinding
    private val grid by lazy { GridDraw(binding.container) }
    private val elementDraw by lazy { ElementDraw(binding.container) }
    private val enemyDraw by lazy { EnemyDraw(binding.container, elementDraw.elementsContainer) }
    private val levelStorage by lazy { LevelStorage(this as Activity) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clear.setOnClickListener { elementDraw.enterMaterial = Material.EMPTY }
        binding.brick.setOnClickListener { elementDraw.enterMaterial = Material.BRICK }
        binding.concrete.setOnClickListener { elementDraw.enterMaterial = Material.CONCRETE }
        binding.grass.setOnClickListener { elementDraw.enterMaterial = Material.GRASS }
        binding.eagle.setOnClickListener { elementDraw.enterMaterial = Material.EAGLE }
        binding.enemyBase.setOnClickListener { elementDraw.enterMaterial = Material.ENEMYBASE }
        binding.ourRespawn.setOnClickListener { elementDraw.enterMaterial = Material.OURRESPAWN }
        binding.container.setOnTouchListener { _, event ->
            if (editMode) elementDraw.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
        elementDraw.drawElementOnStartGame(levelStorage.loadLevel())
        elementDraw.drawElementOnStartGame(listOf(tankPlayer.element))
        elementDraw.hideElementInGame(editMode)
    }

    fun switchEdit() {
        editMode = !editMode
        if (editMode) {
            grid.drawGrid()
            binding.material.visibility = VISIBLE
        } else {
            grid.removeGrid()
            binding.material.visibility = GONE
        }
        elementDraw.hideElementInGame(editMode)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu -> {
                switchEdit()
                return true
            }
            R.id.save -> {
                levelStorage.saveLevel(elementDraw.elementsContainer.filter { it.material != Material.PLAYER
                        && it.material != Material.ENEMYTANK })
                return true
            }
            R.id.play -> {
                startGame()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startGame() {
        if (!editMode) enemyDraw.startBattle()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KEYCODE_DPAD_UP -> {
                tankPlayer.move(binding.container, Direction.UP, elementDraw.elementsContainer)
            }
            KEYCODE_DPAD_DOWN -> {
                tankPlayer.move(binding.container, Direction.DOWN, elementDraw.elementsContainer)
            }
            KEYCODE_DPAD_LEFT -> {
                tankPlayer.move(binding.container, Direction.LEFT, elementDraw.elementsContainer)
            }
            KEYCODE_DPAD_RIGHT -> {
                tankPlayer.move(binding.container, Direction.RIGHT, elementDraw.elementsContainer)
            }
            KEYCODE_SPACE -> {
                tankPlayer.bullet.bulletMove(tankPlayer)
            }

            KEYCODE_0 -> {
                println(elementDraw.elementsContainer)
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}