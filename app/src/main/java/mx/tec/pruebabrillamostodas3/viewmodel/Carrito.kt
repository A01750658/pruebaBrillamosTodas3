package mx.tec.pruebabrillamostodas3.viewmodel

import mx.tec.pruebabrillamostodas3.model.Producto

data class Carrito(
    var productos: MutableList<Pair<Producto,Int>> = mutableListOf<Pair<Producto,Int>>()

)
