package com.tatanstudios.eltuncazometapan.vistas.opciones.direcciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColor
import com.tatanstudios.eltuncazometapan.componentes.BloqueEntradaGeneral
import com.tatanstudios.eltuncazometapan.componentes.CardMisDirecciones
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1Boton
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloDireccionesArray
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.ui.theme.ColorBlanco
import com.tatanstudios.eltuncazometapan.ui.theme.ColorGris
import com.tatanstudios.eltuncazometapan.viewmodel.ListadoDireccionesViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.RegistroNuevaDireccionViewModel
import com.tatanstudios.eltuncazometapan.vistas.opciones.menu.redireccionarAjustes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisDireccionesScreen(
    navController: NavHostController,
    estadoBotonAtras: Int = 0,
    viewModel: ListadoDireccionesViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // ── Listado de direcciones ─────────────────────────────────────
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val resultado by viewModel.resultado.observeAsState()
    var boolDatosCargados by remember { mutableStateOf(false) }
    var modeloListaDireccionesArray by remember { mutableStateOf(listOf<ModeloDireccionesArray>()) }

    // ── Token / usuario ────────────────────────────────────────────
    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    // ── GPS dialog ─────────────────────────────────────────────────
    var popPermisoGPS by remember { mutableStateOf(false) }

    // ── ViewModel de registro ──────────────────────────────────────
    val registroViewModel: RegistroNuevaDireccionViewModel = viewModel()
    val isLoadingRegistro by registroViewModel.isLoading.observeAsState(false)
    val resultadoRegistro by registroViewModel.resultado.observeAsState()

    val nombre by registroViewModel.nombre.observeAsState("")
    val telefono by registroViewModel.telefono.observeAsState("")
    val direccion by registroViewModel.direccion.observeAsState("")
    val puntoReferencia by registroViewModel.puntoReferencia.observeAsState("")

    // ── BottomSheet ────────────────────────────────────────────────
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ── Modales ────────────────────────────────────────────────────
    var modalMensajeString by remember { mutableStateOf("") }
    var showModal1Boton by remember { mutableStateOf(false) }

    // ── Strings fuera de lambdas ───────────────────────────────────
    val texto8Digitos        = stringResource(R.string.telefono_son_8_digitos)
    val stringNombreReq      = stringResource(R.string.nombre_es_requerido)
    val stringTelefonoReq    = stringResource(R.string.telefono_es_requerido)
    val stringDireccionReq   = stringResource(R.string.direccion_es_requerido)
    val stringDireccionOk    = stringResource(R.string.direccion_registrada)
    val stringErrorReintentar = stringResource(R.string.error_reintentar_de_nuevo)

    // ── Cargar datos al entrar ─────────────────────────────────────
    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
            viewModel.listadoDireccionesRetrofit(idusuario)
        }
    }
    var recargarListado by remember { mutableStateOf(false) }

    // ── Resultado registro ─────────────────────────────────────────
    resultadoRegistro?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                CustomToasty(ctx, stringDireccionOk, ToastType.SUCCESS)
                showBottomSheet = false
                recargarListado = true // 👈 dispara el LaunchedEffect
                LaunchedEffect(recargarListado) {
                    if (recargarListado) {
                        viewModel.listadoDireccionesRetrofit(idusuario)
                        recargarListado = false
                    }
                }
            }
            else -> {
                CustomToasty(ctx, stringErrorReintentar, ToastType.ERROR)
            }
        }
    }

    // ── Resultado listado ──────────────────────────────────────────
    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                boolDatosCargados = true
                modeloListaDireccionesArray = result.lista
            }
            else -> {
                boolDatosCargados = true
            }
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.mis_direcciones),
                colorResource(R.color.colorAppPrimary),
                estadoBotonAtras = estadoBotonAtras
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = colorResource(R.color.colorRojo),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar dirección"
                )
            }
        }
    ) { innerPadding ->

        // ── Contenido principal ────────────────────────────────────
        if (modeloListaDireccionesArray.isEmpty() && boolDatosCargados) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.map),
                        contentDescription = stringResource(R.string.mis_direcciones),
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.no_hay_direccion_registrada),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }
            }

        } else {

            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(modeloListaDireccionesArray) { tipoDato ->
                    CardMisDirecciones(
                        nombre       = tipoDato.nombre ?: "",
                        seleccionado = tipoDato.seleccionado,
                        minimoCompra = tipoDato.minimocompra ?: "",
                        direccion    = tipoDato.direccion ?: "",
                        onClick = {
                            navController.navigate(
                                Routes.VistaSeleccionarDireccion.createRoute(
                                    tipoDato.id,
                                    tipoDato.nombre ?: "",
                                    tipoDato.telefono,
                                    tipoDato.direccion,
                                    tipoDato.punto_referencia
                                ),
                                navOptions { launchSingleTop = true }
                            )
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }

        // ── Loading listado ────────────────────────────────────────
        if (isLoading) {
            LoadingModal(isLoading = true)
        }

        // ── Loading registro ───────────────────────────────────────
        if (isLoadingRegistro) {
            LoadingModal(isLoading = true)
        }

        // ── Modal 1 botón (validaciones) ───────────────────────────
        if (showModal1Boton) {
            CustomModal1Boton(
                showDialog = showModal1Boton,
                message    = modalMensajeString,
                onDismiss  = { showModal1Boton = false }
            )
        }

        // ── Dialog permiso GPS ─────────────────────────────────────
        if (popPermisoGPS) {
            AlertDialog(
                onDismissRequest = { popPermisoGPS = false },
                title   = { Text(stringResource(R.string.permiso_gps_requerido)) },
                text    = { Text(stringResource(R.string.para_usar_esta_funcion_gps)) },
                confirmButton = {
                    Button(
                        onClick = {
                            popPermisoGPS = false
                            redireccionarAjustes(ctx)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.colorAzul),
                            contentColor   = colorResource(R.color.colorBlanco)
                        )
                    ) { Text(stringResource(R.string.ir_a_ajustes)) }
                },
                dismissButton = {
                    Button(
                        onClick = { popPermisoGPS = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorGris,
                            contentColor   = ColorBlanco
                        )
                    ) { Text(stringResource(R.string.cancelar)) }
                }
            )
        }

        // ── BottomSheet nueva dirección ────────────────────────────
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState       = sheetState,

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Text(
                        text      = stringResource(R.string.nueva_direccion),
                        fontWeight = FontWeight.Bold,
                        fontSize  = 20.sp,
                        color     = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    // Nombre
                    Text(
                        text       = stringResource(R.string.nombre),
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = Color.Black,
                        modifier   = Modifier.padding(bottom = 4.dp)
                    )
                    BloqueEntradaGeneral(
                        text           = nombre,
                        onTextChanged  = { registroViewModel.setNombre(it) },
                        maxLength      = 100,
                        placeholderResId = R.string.nombre,
                        icon           = Icons.Filled.Person
                    )

                    Spacer(Modifier.height(12.dp))

                    // Teléfono
                    Text(
                        text       = stringResource(R.string.telefono),
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = Color.Black,
                        modifier   = Modifier.padding(bottom = 4.dp)
                    )
                    BloqueEntradaGeneral(
                        text           = telefono,
                        onTextChanged  = { registroViewModel.setTelefono(it) },
                        maxLength      = 8,
                        placeholderResId = R.string.telefono,
                        icon           = Icons.Filled.Numbers,
                        keyboardType   = KeyboardType.Phone
                    )

                    Spacer(Modifier.height(12.dp))

                    // Dirección
                    Text(
                        text       = stringResource(R.string.direccion),
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = Color.Black,
                        modifier   = Modifier.padding(bottom = 4.dp)
                    )
                    BloqueEntradaGeneral(
                        text           = direccion,
                        onTextChanged  = { registroViewModel.setDireccion(it) },
                        maxLength      = 400,
                        placeholderResId = R.string.direccion,
                        icon           = Icons.Filled.Map
                    )

                    Spacer(Modifier.height(12.dp))

                    // Punto de referencia (opcional)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text       = stringResource(R.string.punto_referencia),
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp,
                            color      = Color.Black
                        )
                        Spacer(Modifier.size(6.dp))
                        Text(
                            text     = stringResource(R.string.opcional),
                            fontSize = 12.sp,
                            color    = Color.Gray
                        )
                    }
                    BloqueEntradaGeneral(
                        text           = puntoReferencia ?: "",
                        onTextChanged  = { registroViewModel.setPuntoReferencia(it) },
                        maxLength      = 400,
                        placeholderResId = R.string.punto_referencia,
                        icon           = Icons.Filled.House
                    )

                    Spacer(Modifier.height(24.dp))

                    // Botón guardar
                    Button(
                        onClick = {
                            when {
                                nombre.isBlank() -> {
                                    modalMensajeString = stringNombreReq
                                    showModal1Boton = true
                                }
                                telefono.isBlank() -> {
                                    modalMensajeString = stringTelefonoReq
                                    showModal1Boton = true
                                }
                                telefono.length < 8 -> {
                                    modalMensajeString = texto8Digitos
                                    showModal1Boton = true
                                }
                                direccion.isBlank() -> {
                                    modalMensajeString = stringDireccionReq
                                    showModal1Boton = true
                                }
                                else -> {
                                    scope.launch {
                                        registroViewModel.registrarNuevaDireccionRetrofit(
                                            idusuario,
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape  = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.colorAppPrimary),
                            contentColor   = Color.White
                        )
                    ) {
                        Text(
                            text       = stringResource(R.string.guardar).uppercase(),
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }
        }

    } // end Scaffold
}