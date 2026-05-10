package com.tatanstudios.eltuncazometapan.vistas.opciones.opciones.direcciones

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.BloqueEntradaGeneral
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1Boton
import com.tatanstudios.eltuncazometapan.componentes.CustomModal2Botones
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.viewmodel.RegistroNuevaDireccionViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun RegistrarNuevaDireccionScreen(
    navController: NavHostController,
    idzona: Int,
    latitud: String,
    longitud: String,
    latitudreal: String?,
    longitudreal: String?,
    viewModel: RegistroNuevaDireccionViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val resultado by viewModel.resultado.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    val nombre by viewModel.nombre.observeAsState("")
    val telefono by viewModel.telefono.observeAsState("")
    val puntoReferencia by viewModel.puntoReferencia.observeAsState("")
    val direccion by viewModel.direccion.observeAsState("")

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }
    var showModal2Boton by remember { mutableStateOf(false) }
    var navegarAPrincipal by remember { mutableStateOf(false) }

    val loginButtonColor = if (isPressed) {
        colorResource(id = R.color.colorAppPrimary).copy(alpha = 0.8f)
    } else {
        colorResource(id = R.color.colorAppPrimary)
    }
    val elevation by animateDpAsState(if (isPressed) 12.dp else 6.dp, label = "btn_elev")

    // Strings fuera de lambdas
    val texto8CaracteresTelefono = stringResource(R.string.telefono_son_8_digitos)
    val stringNombreEsRequerido = stringResource(R.string.nombre_es_requerido)
    val stringTelefonoEsRequerido = stringResource(R.string.telefono_es_requerido)
    val stringDireccionEsRequerido = stringResource(R.string.direccion_es_requerido)
    val stringDireccionRegistrada = stringResource(R.string.direccion_registrada)
    val stringErrorReintentar = stringResource(R.string.error_reintentar_de_nuevo)

    // Cargar id usuario
    LaunchedEffect(Unit) {
        idusuario = tokenManager.idUsuario.first()
    }

    // Navegación segura
    LaunchedEffect(navegarAPrincipal) {
        if (navegarAPrincipal) {
            navController.navigate(Routes.VistaPrincipal.createRoute("menu")) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
            navegarAPrincipal = false
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.colorAppPrimary))
                .padding(innerPadding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(Modifier.height(24.dp))

                // ── TÍTULO ─────────────────────────────────────────
                Text(
                    text = stringResource(id = R.string.nueva_direccion),
                    fontFamily = FontFamily(Font(R.font.arthura_medium)),
                    color = Color.White,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(Modifier.height(24.dp))

                // ── CARD FORMULARIO ────────────────────────────────
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {

                        // Nombre
                        Text(
                            text = stringResource(R.string.nombre),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        BloqueEntradaGeneral(
                            text = nombre,
                            onTextChanged = { viewModel.setNombre(it) },
                            maxLength = 100,
                            placeholderResId = R.string.nombre,
                            icon = Icons.Filled.Person
                        )

                        Spacer(Modifier.height(12.dp))

                        // Teléfono
                        Text(
                            text = stringResource(R.string.telefono),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        BloqueEntradaGeneral(
                            text = telefono,
                            onTextChanged = { viewModel.setTelefono(it) },
                            maxLength = 8,
                            placeholderResId = R.string.telefono,
                            icon = Icons.Filled.Numbers,
                            keyboardType = KeyboardType.Phone
                        )

                        Spacer(Modifier.height(12.dp))

                        // Dirección
                        Text(
                            text = stringResource(R.string.direccion),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        BloqueEntradaGeneral(
                            text = direccion,
                            onTextChanged = { viewModel.setDireccion(it) },
                            maxLength = 400,
                            placeholderResId = R.string.direccion,
                            icon = Icons.Filled.Map
                        )

                        Spacer(Modifier.height(12.dp))

                        // Punto de referencia (opcional)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.punto_referencia),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Spacer(Modifier.size(6.dp))
                            Text(
                                text = stringResource(R.string.opcional),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        BloqueEntradaGeneral(
                            text = puntoReferencia ?: "",
                            onTextChanged = { viewModel.setPuntoReferencia(it) },
                            maxLength = 400,
                            placeholderResId = R.string.punto_referencia,
                            icon = Icons.Filled.House
                        )

                        Spacer(Modifier.height(24.dp))

                        // Botón guardar
                        Button(
                            onClick = {
                                keyboardController?.hide()

                                when {
                                    nombre.isBlank() -> {
                                        modalMensajeString = stringNombreEsRequerido
                                        showModal1Boton = true
                                    }
                                    telefono.isBlank() -> {
                                        modalMensajeString = stringTelefonoEsRequerido
                                        showModal1Boton = true
                                    }
                                    telefono.length < 8 -> {
                                        modalMensajeString = texto8CaracteresTelefono
                                        showModal1Boton = true
                                    }
                                    direccion.isBlank() -> {
                                        modalMensajeString = stringDireccionEsRequerido
                                        showModal1Boton = true
                                    }
                                    else -> {
                                        showModal2Boton = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .shadow(elevation = elevation, shape = RoundedCornerShape(30.dp)),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = loginButtonColor,
                                contentColor = Color.White
                            ),
                            interactionSource = interactionSource
                        ) {
                            Text(
                                text = stringResource(id = R.string.guardar).uppercase(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                    }
                } // end Card

                Spacer(Modifier.height(32.dp))
            }

            // ── MODALES ────────────────────────────────────────────
            if (showModal1Boton) {
                CustomModal1Boton(
                    showDialog = showModal1Boton,
                    message = modalMensajeString,
                    onDismiss = { showModal1Boton = false }
                )
            }

            if (isLoading) {
                LoadingModal(isLoading = isLoading)
            }

            // ── RESULTADO API ──────────────────────────────────────
            resultado?.getContentIfNotHandled()?.let { result ->
                when (result.success) {
                    1 -> {
                        CustomToasty(ctx, stringDireccionRegistrada, ToastType.SUCCESS)
                        navegarAPrincipal = true
                    }
                    else -> {
                        CustomToasty(ctx, stringErrorReintentar, ToastType.ERROR)
                    }
                }
            }

            // ── MODAL CONFIRMACION ─────────────────────────────────
            if (showModal2Boton) {
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.registrar_direccion),
                    onDismiss = { showModal2Boton = false },
                    onAccept = {
                        showModal2Boton = false
                        scope.launch {
                            viewModel.registrarNuevaDireccionRetrofit(
                                idusuario,
                                idzona.toString(),
                                latitud,
                                longitud,
                                latitudreal,
                                longitudreal
                            )
                        }
                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }
        }
    }
}