import kotlinx.coroutines.delay

data class Calculo(
    val productos: List<Producto>,
    val subtotal: Int,
    val descuento: Int,
    val iva: Int,
    val total: Int
)

class GestorPedidos(private val ivaPorc: Double = 0.19) {

    private val pedidosRealizados = mutableListOf<Calculo>() // **Agregado**

    fun catalogoInicial(): List<Producto> = listOf(
        ProductoComida("Hamburguesa Clásica", 8990, false, 3),
        ProductoComida("Salmón Grillado", 15990, true, 5),
        ProductoBebida("Coca Cola", 1990, "MEDIANO", 2),
        ProductoBebida("Jugo Natural", 2990, "GRANDE", 2)
    )

    fun validarPedido(items: List<Producto>) {
        require(items.isNotEmpty()) { "El pedido está vacío" }
        require(items.size <= 10) { "Máximo 10 productos por pedido" }
    }

    suspend fun procesarPedidoAsync(
        items: List<Producto>,
        onEstado: (EstadoPedido) -> Unit = {}
    ): EstadoPedido {
        return try {
            validarPedido(items)
            onEstado(EstadoPedido.Pendiente)
            delay(500)
            onEstado(EstadoPedido.EnPreparacion)
            delay(items.sumOf { it.tiempoPrepSeg } * 1000L)
            onEstado(EstadoPedido.Listo)
            EstadoPedido.Listo
        } catch (e: Exception) {
            val err = EstadoPedido.Error(e.message ?: "Error desconocido")
            onEstado(err)
            err
        }
    }

    fun calcularTotales(items: List<Producto>, tipoCliente: String): Calculo {
        val subtotal = items.sumOf { it.precioFinal() }
        val descuento = when (tipoCliente.lowercase()) {
            "regular" -> (subtotal * 0.05).toInt()
            "vip" -> (subtotal * 0.10).toInt()
            "premium" -> (subtotal * 0.15).toInt()
            else -> 0
        }
        val base = subtotal - descuento
        val iva = (base * ivaPorc).toInt()
        val total = base + iva

        val calculo = Calculo(items, subtotal, descuento, iva, total)
        pedidosRealizados.add(calculo) // **Agregado**
        return calculo
    }


    fun reporteVentas() {
        println("\n=== REPORTE DE VENTAS ===")
        if (pedidosRealizados.isEmpty()) {
            println("No se han registrado ventas todavía.")
        } else {
            pedidosRealizados.forEachIndexed { i, c ->
                println("Pedido ${i + 1}: Total = $${c.total}")
            }
        }
    }
}
