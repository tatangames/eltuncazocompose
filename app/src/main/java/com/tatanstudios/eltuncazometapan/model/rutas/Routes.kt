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



    // VISTA MAPA
    object VistaMapa: Routes("vistaMapa")

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







}