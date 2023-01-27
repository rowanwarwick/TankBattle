package com.example.tank.enums

import com.example.tank.R

enum class Material(
    val tankCanGo:Boolean,
    val bulletCanGo: Boolean,
    val canDestroy: Boolean,
    val onlyOne: Boolean,
    val height: Int,
    val width: Int,
    val image: Int
    )
{
    EMPTY(true , true , true , false , 0 , 0 , 0) ,
    BRICK(false, false, true, false, 1, 1, R.drawable.brick),
    CONCRETE(false, false, false, false, 1, 1, R.drawable.concrete),
    GRASS(true, true, false, false, 1, 1, R.drawable.grass),
    EAGLE(false, false, true, true, 2, 2, R.drawable.eagle)
}