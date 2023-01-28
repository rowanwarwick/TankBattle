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

const val CELL_SIZE = 50

class MainActivity : AppCompatActivity() {
    private var editMode = false
    private lateinit var binding: ActivityMainBinding
    private val grid by lazy { GridDraw(binding.container) }
    private val elementDraw by lazy { ElementDraw(binding.container) }
    private val tankDraw by lazy { TankDraw(binding.container) }
    private val gunDraw by lazy { GunDraw(binding.container) }
    private val enemyDraw by lazy { EnemyDraw(binding.container) }
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
            elementDraw.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
        elementDraw.drawElementOnStartGame(levelStorage.loadLevel())
        elementDraw.hideElementInGame(editMode)
        //println(enemyDraw.searchEnemyBase(elementDraw.elementsContainer).joinToString(" "))
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
                levelStorage.saveLevel(elementDraw.elementsContainer)
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
        if (!editMode) enemyDraw.startBattle(elementDraw.elementsContainer)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KEYCODE_DPAD_UP -> {
                tankDraw.move(binding.myTank, Direction.UP, elementDraw.elementsContainer)
            }
            KEYCODE_DPAD_DOWN -> {
                tankDraw.move(binding.myTank, Direction.DOWN, elementDraw.elementsContainer)
            }
            KEYCODE_DPAD_LEFT -> {
                tankDraw.move(binding.myTank, Direction.LEFT, elementDraw.elementsContainer)
            }
            KEYCODE_DPAD_RIGHT -> {
                tankDraw.move(binding.myTank, Direction.RIGHT, elementDraw.elementsContainer)
            }
            KEYCODE_SPACE -> gunDraw.bulletMove(binding.myTank, tankDraw.currectDirection, elementDraw.elementsContainer)
        }
        return super.onKeyDown(keyCode, event)
    }
}