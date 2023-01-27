package com.example.tank.enums

import com.example.tank.R

enum class Material(
    val tankCanGo:Boolean,
    val bulletCanGo: Boolean,
    val canDestroy: Boolean,
    val count: Int,
    val height: Int,
    val width: Int,
    val image: Int
    )
{
    EMPTY(true , true , true , 0 , 0 , 0 , 0) ,
    BRICK(false, false, true, 0, 1, 1, R.drawable.brick),
    CONCRETE(false, false, false, 0, 1, 1, R.drawable.concrete),
    GRASS(true, true, false, 0, 1, 1, R.drawable.grass),
    EAGLE(false, false, true, 1, 2, 2, R.drawable.eagle),
    ENEMY(false, false, true, 3, 2, 2, R.drawable.enemy),
    RESPAWN(false, false, true, 1, 2, 2, R.drawable.tank)
}