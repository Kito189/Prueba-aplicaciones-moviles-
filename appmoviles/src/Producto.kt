open class Producto(
    val nombre: String,
    val precioBase: Int,
    val categoria: String,
    val tiempoPrepSeg: Int
) {
    open fun precioFinal(): Int = precioBase
}

class ProductoComida(
    nombre: String,
    precioBase: Int,
    val premium: Boolean,
    tiempoPrepSeg: Int
) : Producto(nombre, precioBase, "Comida", tiempoPrepSeg) {
    override fun precioFinal(): Int {
        return if (premium) (precioBase * 1.2).toInt() else precioBase
    }
}

class ProductoBebida(
    nombre: String,
    precioBase: Int,
    val tamano: String,
    tiempoPrepSeg: Int
) : Producto(nombre, precioBase, "Bebida", tiempoPrepSeg) {
    override fun precioFinal(): Int {
        return when (tamano.uppercase()) {
            "PEQUEÑO" -> (precioBase * 0.9).toInt()
            "MEDIANO" -> (precioBase * 1.15).toInt()
            "GRANDE" -> (precioBase * 1.3).toInt()
            else -> precioBase
        }
    }
}
