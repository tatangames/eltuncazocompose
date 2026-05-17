package com.tatanstudios.eltuncazometapan.model.rutas

sealed class Routes(val route: String) {

    object VistaSplash: Routes("splash")
    object VistaLogin: Routes("login")
    object VistaRegistro: Routes("registro")

    object VistaPrincipal : Routes("principal/{selectedScreenVar}") {
       fun createRoute(selectedScreenVar: String) = "principal/$selectedScreenVar"
   }

    // VISTA PERFIL OPCIONES
    object VistaPerfil: Routes("perfil")

    // VISTA ACTUALIZAR CONTRASENA
    object VistaActualizarContrasena: Routes("actualizarContrasena")

    // VISTA MIS DIRECCIONES
    object VistaMisDirecciones : Routes("vistaMisDirecciones/{estadoBoton}") {
        fun createRoute(
            estadoBoton: Int
        ) = "vistaMisDirecciones/$estadoBoton"
    }





    // VISTA REGISTRO DE DIRECCION NUEVA
    object VistaRegistroDireccion: Routes("registroDireccionNueva/{id}/{latitud}/{longitud}/{latitudreal}/{longitudreal}") {
        fun createRoute(
            id: Int,
            latitud: Double,
            longitud: Double,
            latitudreal: Double?,
            longitudreal: Double?
        ) = "registroDireccionNueva/$id/$latitud/$longitud/${latitudreal ?: "none"}/${longitudreal ?: "none"}"
    }

    // VISTA SELECCIONAR DIRECCION
    object VistaSeleccionarDireccion: Routes("seleccionarDireccion/{id}/{nombre}/{telefono}/{direccion}/{referencia}") {
        fun createRoute(
            id: Int,
            nombre: String,
            telefono: String?,
            direccion: String?,
            referencia: String?
        ) = "seleccionarDireccion/$id/$nombre/$telefono/${direccion}/${referencia}"
    }

    // VISTA LISTADO PRODUCTOS
    object VistaListadoProductos : Routes("vistaListadoProductos/{idcategoria}") {
        fun createRoute(
            idcategoria: Int
        ) = "vistaListadoProductos/$idcategoria"
    }

    // VISTA INFORMACION DE UN PRODUCTO
    object VistaInformacionProducto : Routes("vistaInformacionProducto/{idproducto}") {
        fun createRoute(
            idproducto: Int
        ) = "vistaInformacionProducto/$idproducto"
    }

    // VISTA CARRITO COMPRAS
    object VistaCarrito: Routes("carrito")


    // VISTA PARA EDITAR PRODUCTO
    object VistaEditarProducto : Routes("vistaInformacionProductoEditar/{idfilacarrito}") {
        fun createRoute(
            idfilacarrito: Int
        ) = "vistaInformacionProductoEditar/$idfilacarrito"
    }

    // VISTA PARA ENVIAR LA ORDEN
    object VistaEnviarOrden: Routes("vistaEnviarOrden")

    // VISTA PARA VER LOS ESTADOS DE UNA ORDEN
    object VistaEstadoOrden : Routes("vistaEstadoOrden/{idorden}") {
        fun createRoute(
            idorden: Int
        ) = "vistaEstadoOrden/$idorden"
    }

    // VISTA PARA VER LISTADO PRODUCTOS DE UNA ORDEN
    object VistaListaProductosDeOrden : Routes("vistaListadoProductoOrden/{idorden}") {
        fun createRoute(
            idorden: Int
        ) = "vistaListadoProductoOrden/$idorden"
    }

}