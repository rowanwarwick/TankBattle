package com.example.tank

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.tank.databinding.ActivityMainBinding
import com.example.tank.drawers.ElementDraw
import com.example.tank.drawers.GridDraw
import com.example.tank.drawers.GunDraw
import com.example.tank.drawers.TankDraw
import com.example.tank.enums.Direction
import com.example.tank.enums.Material

class MainActivity : AppCompatActivity() {
    private var editMode = false
    private lateinit var binding: ActivityMainBinding
    private val grid by lazy { GridDraw(binding.container) }
    private val elementDraw by lazy { ElementDraw(binding.container) }
    private val tankDraw by lazy { TankDraw(binding.container) }
    private val gunDraw by lazy { GunDraw(binding.container) }

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
        binding.container.setOnTouchListener { _, event ->
            elementDraw.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
    }

    fun switchEdit() {
        if (editMode) {
            grid.removeGrid()
            binding.material.visibility = GONE
        } else {
            grid.drawGrid()
            binding.material.visibility = VISIBLE
        }
        editMode = !editMode
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KEYCODE_DPAD_UP -> {
                tankDraw.move(binding.myTank, Direction.UP, elementDraw.elementsMaterial)
            }
            KEYCODE_DPAD_DOWN -> {
                tankDraw.move(binding.myTank, Direction.DOWN, elementDraw.elementsMaterial)
            }
            KEYCODE_DPAD_LEFT -> {
                tankDraw.move(binding.myTank, Direction.LEFT, elementDraw.elementsMaterial)
            }
            KEYCODE_DPAD_RIGHT -> {
                tankDraw.move(binding.myTank, Direction.RIGHT, elementDraw.elementsMaterial)
            }
            KEYCODE_SPACE -> gunDraw.bulletMove(binding.myTank, tankDraw.currectDirection, elementDraw.elementsMaterial)
        }
        return super.onKeyDown(keyCode, event)
    }
}