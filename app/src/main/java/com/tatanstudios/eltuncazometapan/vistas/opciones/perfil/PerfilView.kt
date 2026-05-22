package com.tatanstudios.eltuncazometapan.vistas.opciones.perfil

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.navOptions
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColor
import com.tatanstudios.eltuncazometapan.componentes.CustomModalCerrarSesion
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import com.tatanstudios.eltuncazometapan.model.rutas.Routes


sealed class Opcion(val icon: ImageVector?, open val label: String) {
    object Direcciones : Opcion(Icons.Default.FormatListNumbered, "Direcciones")
    object CambioPassword : Opcion(Icons.Default.Schedule, "Cambio de Contraseña")
    object CerrarSesion : Opcion(Icons.AutoMirrored.Default.Logout, "Cerrar Sesión")
}


@Composable
fun PerfilScreen(navController: NavHostController) {

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine
    val tokenManager = remember { TokenManager(ctx) }

    var showModalCerrarSesion by remember { mutableStateOf(false) }

    val opciones = listOf(
        Opcion.Direcciones,
        Opcion.CambioPassword,
        Opcion.CerrarSesion,
    )

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.opciones),
                colorResource(R.color.colorAppPrimary)
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(opciones) { opcion ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .let {
                            it.clickable {
                                when (opcion) {
                                    is Opcion.Direcciones -> {

                                        navController.navigate(
                                            Routes.VistaMisDirecciones.createRoute(0)
                                        ) {
                                            navOptions {
                                                launchSingleTop = true
                                            }
                                        }

                                    }
                                    is Opcion.CambioPassword -> {
                                        navController.navigate(Routes.VistaActualizarContrasena.route) {
                                            navOptions {
                                                launchSingleTop = true
                                            }
                                        }
                                    }

                                    is Opcion.CerrarSesion -> {
                                        showModalCerrarSesion = true
                                    }

                                    else -> {}
                                }
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        opcion.icon?.let { icono ->
                            Icon(
                                imageVector = icono,
                                contentDescription = opcion.label,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 16.dp),
                                tint = Color.Black
                            )
                        }

                        Text(
                            text = opcion.label,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }


        if (showModalCerrarSesion) {
            CustomModalCerrarSesion(showModalCerrarSesion,
                stringResource(R.string.cerrar_sesion),
                onDismiss = { showModalCerrarSesion = false },
                onAccept = {
                    scope.launch {
                        // Llamamos a deletePreferences de manera segura dentro de una coroutine
                        tokenManager.deletePreferences()

                        // cerrar modal
                        showModalCerrarSesion = false

                        navigateToLogin(navController)
                    }
                })
        }
    }
}


// redireccionar a vista login
private fun navigateToLogin(navController: NavHostController) {
    navController.navigate(Routes.VistaLogin.route) {
        popUpTo(Routes.VistaPrincipal.route) {
            inclusive = true // Elimina VistaPrincipal de la pila
        }
        launchSingleTop = true // Asegura que no se creen múltiples instancias de VistaLogin
    }
}
