package com.example.tank

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.tank.databinding.ActivityMainBinding
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tank.drawers.ElementDraw
import com.example.tank.drawers.GridDraw
import com.example.tank.enums.Direction
import com.example.tank.enums.Material

class MainActivity : AppCompatActivity() {
    private var editMode = false
    private lateinit var binding: ActivityMainBinding
    private val grid by lazy { GridDraw(binding.container) }
    private val elementDraw by lazy { ElementDraw(binding.container) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clear.setOnClickListener { elementDraw.enterMaterial = Material.EMPTY }
        binding.brick.setOnClickListener { elementDraw.enterMaterial = Material.BRICK }
        binding.concrete.setOnClickListener { elementDraw.enterMaterial = Material.CONCRETE }
        binding.grass.setOnClickListener { elementDraw.enterMaterial = Material.GRASS }
        binding.container.setOnTouchListener { _, event ->
            println("x: ${event.x}\ny: ${event.y}")
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
            KEYCODE_DPAD_UP -> move(Direction.UP)
            KEYCODE_DPAD_DOWN -> move(Direction.DOWN)
            KEYCODE_DPAD_LEFT -> move(Direction.LEFT)
            KEYCODE_DPAD_RIGHT -> move(Direction.RIGHT)
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun move(direction: Direction) {
        val layoutParams = binding.myTank.layoutParams as ConstraintLayout.LayoutParams
        when (direction) {
            Direction.UP -> {
                binding.myTank.rotation = 0f
                if (layoutParams.topMargin > 0)
                    layoutParams.topMargin -= 50
            }
            Direction.DOWN -> {
                binding.myTank.rotation = 180f
                if (layoutParams.topMargin + binding.myTank.height < binding.container.height)
                    layoutParams.topMargin += 50
            }
            Direction.LEFT -> {
                binding.myTank.rotation = 270f
                if (layoutParams.leftMargin > 0)
                    layoutParams.leftMargin -= 50
            }
            Direction.RIGHT -> {
                binding.myTank.rotation = 90f
                if (layoutParams.leftMargin + binding.myTank.width < binding.container.width)
                    layoutParams.leftMargin += 50
            }
        }
        binding.container.removeView(binding.myTank)
        binding.container.addView(binding.myTank)
    }

}