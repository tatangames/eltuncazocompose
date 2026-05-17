package com.tatanstudios.eltuncazometapan.model.modelos

import com.google.gson.annotations.SerializedName

data class ModeloDatosBasicos(
    @SerializedName("success") val success: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("titulo") val titulo: String? = null,
    @SerializedName("mensaje") val mensaje: String? = null,
    @SerializedName("usuario") val usuario: String? = null,
    @SerializedName("correo") val correo: String? = null,
    @SerializedName("resta") val resta: String? = null,
    @SerializedName("idorden") val idorden: Int = 0,
)



data class ModeloMenuPrincipal(
    @SerializedName("success") val success: Int,
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("mensaje") val mensaje: String?,
    @SerializedName("activo") val activo: Int,
    @SerializedName("servicios") val arrayCategorias: List<ModeloMenuPrincipalCategoriasArray>,
)

data class ModeloMenuPrincipalSliderArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_producto") val idProducto: Int,
    @SerializedName("id_servicios") val idServicios: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("redireccionamiento") val redireccionamiento: Int,
    @SerializedName("usa_horario") val usaHorario: Int,
    @SerializedName("hora_abre") val horaAbre: String?,
    @SerializedName("hora_cierra") val horaCierra: String?,
    @SerializedName("activo") val activo: Int,
)

data class ModeloMenuPrincipalCategoriasArray(
    @SerializedName("id") val id: Int,
    @SerializedName("tiposervicio_id") val tiposervicio_id: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("activo") val activo: Int,
)





data class ModeloHistorialOrdenes(
    @SerializedName("success") val success: Int,
    @SerializedName("hayordenes") val hayordenes: Int,
    @SerializedName("ordenes") val lista: List<ModeloHistorialOrdenesArray>
)


data class ModeloHistorialOrdenesArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_cliente") val idcliente: Int,
    @SerializedName("id_servicio") val idservicio: Int,
    @SerializedName("id_zona") val idzona: Int,
    @SerializedName("nota_orden") val notaOrden: String?,
    @SerializedName("totalformat") val totalFormat: String?,
    @SerializedName("estado") val estado: String?,
    @SerializedName("fecha_orden") val fechaOrden: String?,
    @SerializedName("fecha_cancelada") val fechaCancelada: String?,
    @SerializedName("haycupon") val haycupon: Int,
    @SerializedName("cliente") val cliente: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("referencia") val referencia: String?,
    @SerializedName("haypremio") val haypremio: Int,
    @SerializedName("textopremio") val textopremio: String?,
    @SerializedName("mensaje_cupon") val mensajeCupon: String?,
)





data class ModeloProductoHistorialOrdenes(
    @SerializedName("success") val success: Int,
    @SerializedName("productos") val lista: List<ModeloProductoHistorialOrdenesArray>
)

data class ModeloProductoHistorialOrdenesArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_ordenes") val idordenes: Int,
    @SerializedName("id_producto") val idproducto: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("precio") val precio: String?,
    @SerializedName("nombreproducto") val nombreProducto: String?,
    @SerializedName("idordendescrip") val idOrdenDescrip: Int,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("multiplicado") val multiplicado: String?,
)




data class ModeloInfoProducto(
    @SerializedName("success") val success: Int,
    @SerializedName("productos") val lista: List<ModeloInfoProductoArray>
)

data class ModeloInfoProductoArray(
    @SerializedName("id") val success: Int,
    @SerializedName("id_ordenes") val idordenes: Int,
    @SerializedName("id_producto") val idproducto: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("precio") val precio: String?,
    @SerializedName("nombreproducto") val nombreProducto: String?,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("multiplicado") val multiplicado: String?,
)

data class ModeloHorario(
    @SerializedName("success") val success: Int,
    @SerializedName("restaurante") val restaurante: String,
    @SerializedName("horario") val lista: List<ModeloHorarioArray>
)


data class ModeloHorarioArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_servicios") val idServicio: Int,
    @SerializedName("hora1") val hora1: String?,
    @SerializedName("hora2") val hora2: String?,
    @SerializedName("dia") val dia: Int,
    @SerializedName("cerrado") val cerrado: Int,
    @SerializedName("horario") val horario: String?,
    @SerializedName("fechaformat") val fechaformat: String?,
)




data class ModeloPremios(
    @SerializedName("success") val success: Int,
    @SerializedName("conteo") val conteo: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("puntos") val puntos: String?,
    @SerializedName("listado") val lista: List<ModeloPremiosArray>
)


data class ModeloPremiosArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_servicio") val idServicio: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("puntos") val puntos: Int,
    @SerializedName("activo") val activo: Int,
    @SerializedName("seleccionado") var seleccionado: Int,
)




data class ModeloDirecciones(
    @SerializedName("success") val success: Int,
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("mensaje") val mensaje: String?,
    @SerializedName("direcciones") val lista: List<ModeloDireccionesArray>
)

data class ModeloDireccionesArray(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("numero_casa") val numero_casa: String?,
    @SerializedName("punto_referencia") val punto_referencia: String?,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("seleccionado") var seleccionado: Int,
    @SerializedName("minimocompra") val minimocompra: String?,
)



data class ModeloPoligonos(
    @SerializedName("success") val success: Int,
    @SerializedName("poligono") val lista: List<ModeloPoligonosArray>
)

data class ModeloPoligonosArray(
    @SerializedName("id") val id: Int,
    @SerializedName("nombreZona") val nombre: String?,
    @SerializedName("poligonos") val listado: List<ModeloPoligonosLatitudLongitudArray>
)

data class ModeloPoligonosLatitudLongitudArray(
    @SerializedName("latitudPoligono") val latitud: String,
    @SerializedName("longitudPoligono") val longitud: String,
)



data class ModeloMenuPrincipalPopularesArray(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int,
    @SerializedName("precio") val precio: String?,
)





data class ModeloProductos(
    @SerializedName("success") val success: Int,
    @SerializedName("productos") val listaProductoCategoria: List<ModeloProductosArray>
)


data class ModeloProductosArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_categorias") val idCategorias: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("productos") val listaProductoCategoriaTercera: List<ModeloProductosTerceraArray>
)

data class ModeloProductosTerceraArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_subcategorias") val idSubCategoria: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio") val precio: String?,
    @SerializedName("utiliza_nota") val utilizaNota: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int
)


data class ModeloInformacionProducto(
    @SerializedName("success") val success: Int,
    @SerializedName("producto") val informacionProducto: List<ModeloInformacionProductoArray>
)

data class ModeloInformacionProductoArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_subcategorias") val idSubCategoria: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio") val precio: String?,
    @SerializedName("activo") val activo: Int,
    @SerializedName("utiliza_nota") val utilizaNota: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int,
    )



data class ModeloCarrito(
    @SerializedName("success") val success: Int,
    @SerializedName("subtotal") val subTotal: String?,
    @SerializedName("estadoProductoGlobal") val estadoProductoGlobal: Int,
    @SerializedName("producto") val listadoCarritoTemporal: List<ModeloCarritoTemporal>,
    @SerializedName("hayDireccionRegistrada") val hayDireccionRegistrada: Int,
)

data class ModeloCarritoTemporal(
    @SerializedName("productoID") val id: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("precio") val precio: String?,

    @SerializedName("activo") val activo: Int,
    @SerializedName("carritoid") val carritoid: Int,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int,
    @SerializedName("id_subcategorias") val idSubCategorias: Int,
    @SerializedName("estadoLocal") val estadoLocal: Int,

    @SerializedName("titulo") val titulo: String?,
    @SerializedName("mensaje") val mensaje: String?,
    @SerializedName("precioformat") val precioformat: String?,
)



data class ModeloInformacionProductoEditar(
    @SerializedName("success") val success: Int,
    @SerializedName("producto") val producto: ModeloInformacionProductoEditarArray?
)

data class ModeloInformacionProductoEditarArray(
    @SerializedName("productoID") val productoID: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("nota_producto") val notaProducto: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("precio") val precio: String?,         // viene como String ("10.25")
    @SerializedName("utiliza_nota") val utilizaNota: Int,
    @SerializedName("nota") val nota: String?,             // está en tu JSON de ejemplo
    @SerializedName("utiliza_imagen") val utilizaImagen: Int
)


data class ModeloInformacionOrdenParaEnviar(
    @SerializedName("success") val success: Int,
    @SerializedName("total") val total: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("cliente") val cliente: String?,
    @SerializedName("minimo") val minimo: Int,
    @SerializedName("mensaje") val mensaje: String?,
    @SerializedName("usacupon") val usacupon: Int,
    @SerializedName("usapremio") val usapremio: Int,
    @SerializedName("textopremio") val textopremio: String?,
)




data class ModeloOrdenes(
    @SerializedName("success") val success: Int,
    @SerializedName("ordenes") val ordenes: List<ModeloOrdenesArray>
)

data class ModeloOrdenesArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_cliente") val idCliente: Int,
    @SerializedName("id_servicio") val idServicio: Int,
    @SerializedName("id_zona") val idZona: Int,
    @SerializedName("nota") val notaOrden: String?,
    @SerializedName("total") val total: String?,
    @SerializedName("fecha_orden") val fechaOrden: String?,
    @SerializedName("fecha_estimada") val fechaEstimada: String?,
    @SerializedName("estado_iniciada") val estadoIniciada: Int,
    @SerializedName("fecha_iniciada") val fechaIniciada: String?,
    @SerializedName("estado_preparada") val estadoPreparada: Int,
    @SerializedName("fecha_preparada") val fechaPreparada: String?,
    @SerializedName("estado_camino") val estadoCamino: Int,
    @SerializedName("fecha_camino") val fechaCamino: String?,
    @SerializedName("estado_entregada") val estadoEntregada: Int,
    @SerializedName("fecha_entregada") val fechaEntregada: String?,
    @SerializedName("estado_cancelada") val estadoCancelada: Int,
    @SerializedName("fecha_cancelada") val fechaCancelada: String?,
    @SerializedName("mensaje_cancelada") val mensajeCancelado: String?,
    @SerializedName("id_cupones") val idCupones: Int?,
    @SerializedName("total_cupon") val totalCupon: String?,
    @SerializedName("mensaje_cupon") val mensajeCupon: String?,
    @SerializedName("visible") val visible: Int,
    @SerializedName("id_cupones_copia") val idCuponesCopia: Int?,
    @SerializedName("cancelado_por") val canceladoPor: Int,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("totalformat") val totalFormat: String?,
    @SerializedName("haycupon") val hayCupon: Int,
    @SerializedName("estado") val estado: String?,
    @SerializedName("haypremio") val hayPremio: Int,
    @SerializedName("textopremio") val textoPremio: String?
)




data class ModeloOrdenesIndividual(
    @SerializedName("success") val success: Int,
    @SerializedName("ordenes") val ordenes: List<ModeloOrdenesIndividualArray>
)

data class ModeloOrdenesIndividualArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_cliente") val idCliente: Int,
    @SerializedName("id_servicio") val idServicio: Int,
    @SerializedName("id_zona") val idZona: Int,
    @SerializedName("nota_orden") val notaOrden: String?,
    @SerializedName("total_orden") val totalOrden: String?,
    @SerializedName("fecha_orden") val fechaOrden: String?,
    @SerializedName("fecha_estimada") val fechaEstimada: String?,
    @SerializedName("estado_iniciada") val estadoIniciada: Int,
    @SerializedName("fecha_iniciada") val fechaIniciada: String?,
    @SerializedName("estado_preparada") val estadoPreparada: Int,
    @SerializedName("fecha_preparada") val fechaPreparada: String?,
    @SerializedName("estado_camino") val estadoCamino: Int,
    @SerializedName("fecha_camino") val fechaCamino: String?,
    @SerializedName("estado_entregada") val estadoEntregada: Int,
    @SerializedName("fecha_entregada") val fechaEntregada: String?,
    @SerializedName("estado_cancelada") val estadoCancelada: Int,
    @SerializedName("fecha_cancelada") val fechaCancelada: String?,
    @SerializedName("nota_cancelada") val notaCancelada: String?,
    @SerializedName("id_cupones") val idCupones: Int?,
    @SerializedName("total_cupon") val totalCupon: String?,
    @SerializedName("mensaje_cupon") val mensajeCupon: String?,
    @SerializedName("visible") val visible: Int,
    @SerializedName("id_cupones_copia") val idCuponesCopia: Int?,
    @SerializedName("cancelado_por") val canceladoPor: Int,
    @SerializedName("textoiniciada") val textoIniciada: String?,
    @SerializedName("fechaestimada") val fechaEstimadaTxt: String?,
    @SerializedName("textocamino") val textoCamino: String?,
    @SerializedName("fechacamino") val fechaCaminoTxt: String?,
    @SerializedName("textoentregada") val textoEntregada: String?,
    @SerializedName("fechaentregada") val fechaEntregadaTxt: String?
)



data class ModeloProductosDeOrden(
    @SerializedName("success") val success: Int,
    @SerializedName("productos") val productos: List<ModeloProductosDeOrdenArray>
)

data class ModeloProductosDeOrdenArray(
    @SerializedName("id") val id: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("precio") val precio: String?,
    @SerializedName("nombreproducto") val nombreproducto: String?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("idordendescrip") val idordendescrip: Int,
    @SerializedName("utiliza_imagen") val utiliza_imagen: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("multiplicado") val multiplicado: String?,
)
