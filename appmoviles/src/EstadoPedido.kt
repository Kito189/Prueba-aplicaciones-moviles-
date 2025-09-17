sealed class EstadoPedido {
    object Pendiente : EstadoPedido() {
        override fun toString() = "Pendiente"
    }

    object EnPreparacion : EstadoPedido() {
        override fun toString() = "En Preparación"
    }

    object Listo : EstadoPedido() {
        override fun toString() = "Listo"
    }

    data class Error(val mensaje: String) : EstadoPedido() {
        override fun toString() = "Error: $mensaje"
    }
}
