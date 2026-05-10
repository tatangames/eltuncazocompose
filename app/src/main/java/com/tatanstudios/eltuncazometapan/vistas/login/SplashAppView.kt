package com.tatanstudios.eltuncazometapan.vistas.login

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.tatanstudios.eltuncazometapan.componentes.RobotoMediumFont
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import com.tatanstudios.eltuncazometapan.vistas.opciones.opciones.direcciones.MapaScreen
import com.tatanstudios.eltuncazometapan.vistas.opciones.opciones.direcciones.MisDireccionesScreen
import com.tatanstudios.eltuncazometapan.vistas.opciones.opciones.direcciones.RegistrarNuevaDireccionScreen
import com.tatanstudios.eltuncazometapan.vistas.opciones.opciones.password.ActualizarPasswordScreen
import com.tatanstudios.eltuncazometapan.vistas.opciones.opciones.perfil.PerfilScreen
import com.tatanstudios.eltuncazometapan.vistas.principal.PrincipalScreen

class SplashApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // MODO VERTICAL
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Barra de navegación con iconos oscuros
        window.decorView.post {
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightNavigationBars = true
        }

        enableEdgeToEdge()
        setContent {
            // INICIO DE APLICACION
            AppNavigation()
        }
    }
}

// *** RUTAS DE NAVEGACION ***
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.VistaSplash.route) {

        // SPLASH
        composable(Routes.VistaSplash.route) {
            SplashScreen(navController)
        }

        // LOGIN
        composable(Routes.VistaLogin.route) {
            LoginScreen(navController)
        }

        // REGISTRO
        composable(Routes.VistaRegistro.route) {
            RegistroScreen(navController)
        }

        // PRINCIPAL
        composable(
            route = Routes.VistaPrincipal.route, // "principal/{selectedScreenVar}"
            arguments = listOf(
                navArgument("selectedScreenVar") {
                    type = NavType.StringType
                    defaultValue = "menu"
                }
            )
        ) { backStackEntry ->
            val selected = backStackEntry.arguments?.getString("selectedScreenVar") ?: "menu"

            PrincipalScreen(
                navController = navController,
                selectedScreenVar = selected
            )
        }

        // VISTA OPCIONES PERFIL
        composable(Routes.VistaPerfil.route) { PerfilScreen(navController) }

        // VISTA ACTUALIZAR CONTRASENA
        composable(Routes.VistaActualizarContrasena.route) { ActualizarPasswordScreen(navController) }

        // VISTA MIS DIRECCIONES
        composable(
            route = Routes.VistaMisDirecciones.route,
            arguments = listOf(
                navArgument("estadoBoton") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val estadoBoton = backStackEntry.arguments?.getInt("estadoBoton") ?: 0

            MisDireccionesScreen(
                navController = navController,
                estadoBotonAtras = estadoBoton
            )
        }


        // VISTA MAPA
        composable(Routes.VistaMapa.route) { MapaScreen(navController) }

        // VISTA REGISTRAR NUEVA DIRECCION
        composable(Routes.VistaRegistroDireccion.route) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            val latitud = backStackEntry.arguments?.getString("latitud") ?: "0"
            val longitud = backStackEntry.arguments?.getString("longitud") ?: "0"
            val latitudrealStr = backStackEntry.arguments?.getString("latitudreal")
            val longitudrealStr = backStackEntry.arguments?.getString("longitudreal")

            val latitudreal = if (latitudrealStr == "none") null else latitudrealStr
            val longitudreal = if (longitudrealStr == "none") null else longitudrealStr

            RegistrarNuevaDireccionScreen(
                navController = navController,
                idzona = id,
                latitud = latitud,
                longitud = longitud,
                latitudreal = latitudreal,
                longitudreal = longitudreal
            )
        }

    }
}

@Composable
fun SplashScreen(navController: NavHostController) {

    val ctx = LocalContext.current
    val tokenManager = remember { TokenManager(ctx) }

    // Ejecutar la migración desde SharedPreferences solo una vez
    LaunchedEffect(Unit) {
        //tokenManager.migrateFromSharedPreferencesIfNeeded()
    }

    val idusuario by tokenManager.idUsuario.collectAsState(initial = "")

    // Evitar que el usuario vuelva al splash con el botón atrás
    DisposableEffect(Unit) {
        onDispose {
            navController.popBackStack(Routes.VistaSplash.route, true)
        }
    }

    // Control de la navegación tras un retraso
    LaunchedEffect(idusuario) {
        delay(3000)

        if (idusuario.isNotEmpty()) {
            navController.navigate(Routes.VistaPrincipal.createRoute("menu")
            ) {
                popUpTo(Routes.VistaSplash.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else {
            navController.navigate(Routes.VistaLogin.route) {
                popUpTo(Routes.VistaSplash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logoapp),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto justo debajo del logo
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 26.sp,
                color = Color.Black,
                fontFamily = RobotoMediumFont,
                letterSpacing = 1.5.sp
            )
        }
    }
}