import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val gestor = GestorPedidos()
    val catalogo = gestor.catalogoInicial()

    println("=== SISTEMA FOODEXPRESS ===")
    println("Catálogo disponible:")
    catalogo.forEachIndexed { i, p ->
        val extra = when {
            p is ProductoComida && p.premium -> " (Premium)"
            p is ProductoBebida -> " (${p.tamano})"
            else -> ""
        }
        println("${i + 1}. ${p.nombre} - $${p.precioFinal()}$extra")
    }

    print("\nSeleccione productos (números separados por coma): ")
    val seleccion = readln().split(",").map { it.trim().toInt() - 1 }
    val carrito = seleccion.map { catalogo[it] }

    print("Cliente tipo (regular/vip/premium): ")
    val tipoCliente = readln()

    println("\nProcesando pedido...")
    val estadoFinal = gestor.procesarPedidoAsync(carrito) { estado ->
        println("Estado: $estado")
    }

    val resumen = gestor.calcularTotales(carrito, tipoCliente)

    println("\n=== RESUMEN DEL PEDIDO ===")
    resumen.productos.forEach { println("- ${it.nombre}: $${it.precioFinal()}") }
    println("Subtotal: $${resumen.subtotal}")
    println("Descuento ${tipoCliente.uppercase()}: -$${resumen.descuento}")
    println("IVA (19%): $${resumen.iva}")
    println("TOTAL: $${resumen.total}")

    println("\nEstado final: $estadoFinal")
}
