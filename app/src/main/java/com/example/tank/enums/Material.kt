package com.example.tank.enums

enum class Material(val tankCanGo:Boolean, val bulletCanGo: Boolean, val canDestroy: Boolean) {
    EMPTY(true, true, true),
    BRICK(false, false, true),
    CONCRETE(false, false, false),
    GRASS(true, true, false),
    EAGLE(false, false, true)
}