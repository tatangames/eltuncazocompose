package com.tatanstudios.eltuncazometapan.vistas.opciones.opciones.direcciones


import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navOptions
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColor
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.ui.theme.ColorBlanco
import com.tatanstudios.eltuncazometapan.ui.theme.ColorGris
import com.tatanstudios.eltuncazometapan.viewmodel.ListadoDireccionesViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.ListadoPoligonosViewModel
import com.tatanstudios.eltuncazometapan.vistas.opciones.menu.redireccionarAjustes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
@Composable
fun MapaScreen(
    navController: NavHostController,
    viewModel: ListadoPoligonosViewModel = viewModel()
) {
    val ctx = LocalContext.current

    val resultado by viewModel.resultado.observeAsState()
    val poligonos = viewModel.poligonosUI // ✅ Correcto
    val cameraPositionState = rememberCameraPositionState()
    var popPermisoGPS by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(ctx)
    }

    LaunchedEffect(Unit) {
        viewModel.listadoPoligonosRetrofit()

        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude), 16f
                        )
                    )
                }
            } catch (e: Exception) {
                Toast.makeText(ctx, "No se pudo obtener ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.mapa),
                colorResource(R.color.colorAppPrimary),
            )
        }
    ) { innerPadding ->

        Box(Modifier.fillMaxSize().padding(innerPadding)) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(myLocationButtonEnabled = true)
            ) {
                poligonos.forEach { poligono ->
                    Polygon(
                        points = poligono.puntos, // List<LatLng>
                        fillColor = Color(0x3300FF00),
                        strokeColor = Color(0xFF1976D2),
                        strokeWidth = 2f
                    )
                }
            }

            // Ícono en el centro (PIN FIJO)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .offset(y = (-24).dp) // eleva el pin
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pin_mapa),
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Botón de selección
            Card (
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.colorAppPrimary)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                TextButton(
                    onClick = {
                        val center = cameraPositionState.position.target
                        val zonaEncontrada = poligonos.firstOrNull {
                            PolyUtil.containsLocation(center, it.puntos, false)
                        }

                        if (zonaEncontrada != null) {
                            // Zona válida, ahora sí obtenemos la ubicación real del usuario
                            if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                            ) {
                                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)

                                CoroutineScope(Dispatchers.Main).launch {
                                    try {
                                        val location = fusedLocationClient.lastLocation.await()
                                        if (location != null) {
                                            val latitudReal = location.latitude
                                            val longitudReal = location.longitude
                                            val id = zonaEncontrada.id
                                            val latitud = center.latitude
                                            val longitud = center.longitude

                                            navController.navigate(
                                                Routes.VistaRegistroDireccion.createRoute(
                                                    id,
                                                    latitud,
                                                    longitud,
                                                    latitudReal,
                                                    longitudReal
                                                ),
                                                navOptions {
                                                    launchSingleTop = true
                                                }
                                            )
                                        } else {
                                            Toast.makeText(ctx, "No se pudo obtener la ubicación real", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(ctx, "Error al obtener ubicación real", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                popPermisoGPS = true
                            }
                        } else {
                            Toast.makeText(ctx, "Fuera de zona de entrega", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                ) {
                    Text("Seleccionar ubicación", color = Color.White)
                }
            }
        }

        if (isLoading) {
            LoadingModal(isLoading = true)
        }

        if(popPermisoGPS){
            AlertDialog(
                onDismissRequest = { popPermisoGPS = false },
                title = { Text(stringResource(R.string.permiso_gps_requerido)) },
                text = { Text(stringResource(R.string.para_usar_esta_funcion_gps)) },
                confirmButton = {
                    Button(
                        onClick = {
                            popPermisoGPS = false
                            redireccionarAjustes(ctx)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.colorAzul),
                            contentColor = colorResource(R.color.colorBlanco)
                        )
                    ) {
                        Text(stringResource(R.string.ir_a_ajustes))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            popPermisoGPS = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorGris,
                            contentColor = ColorBlanco
                        )
                    ) {
                        Text(stringResource(R.string.cancelar))
                    }
                }
            )
        }
    }

    // Manejo de resultado
    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {

            }
            else -> CustomToasty(ctx, "Error al cargar zonas", ToastType.ERROR)
        }
    }
}