package com.tatanstudios.eltuncazometapan.vistas.principal.productos


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.navOptions
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColor
import com.tatanstudios.eltuncazometapan.componentes.CardHistorialOrden
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloDireccionesArray
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloHistorialOrdenesArray
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductos
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductosArray
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.viewmodel.ListadoProductosViewModel
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1Boton
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1BotonTitulo
import com.tatanstudios.eltuncazometapan.componentes.CustomModal2Botones
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloInformacionProductoArray
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductosTerceraArray
import com.tatanstudios.eltuncazometapan.viewmodel.EnviarOrdenFinalViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.EnviarProductoAlCarritoViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.InformacionOrdenParaEnviarViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.InformacionProductoViewModel
import com.tatanstudios.eltuncazometapan.vistas.login.getVersionName
import retrofit2.http.Field
import java.util.Locale


@Composable
fun EnviarOrdenScreen(
    navController: NavHostController,
    viewModel: InformacionOrdenParaEnviarViewModel = viewModel(),
    viewModelEnviarOrden: EnviarOrdenFinalViewModel = viewModel(),
) {
    val ctx = LocalContext.current

    val tokenManager = remember { TokenManager(ctx) }
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingEnviarOrden by viewModelEnviarOrden.isLoading.observeAsState(true)
    val resultadoEnviarOrden by viewModelEnviarOrden.resultado.observeAsState()

    var idusuario by remember { mutableStateOf("") }

    // 1: SI PUEDE ORDENAR   0: NO PUEDE ORDENAR
    var minimoInt0NoPuede by remember { mutableStateOf(0) }
    var mensajeMinimoConsumoString by remember { mutableStateOf("") }
    var direccionString by remember { mutableStateOf("") }
    var clienteString by remember { mutableStateOf("") }
    var totalString by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }


    var datosCargados by remember { mutableStateOf(false) }


    var textoCuponEscrito by remember { mutableStateOf("") }


    var tengoCupon by remember { mutableStateOf(0) }
    // MENSAJE DE LO QUE APLICA EL CUPON
    var textoCuponGanado by remember { mutableStateOf("") }

    // LA PRIMERA PALABRA (TOTAL) QUE CAMBIARA A SUB TOTAL CUANDO SE APLIQUE CUPON DINERO O DESCUENTO
    var txtSubTotalLetra by remember { mutableStateOf("Total") }

    val versionLocal = getVersionName(ctx)

    // PARA SABER SI ES CUPON DE DINERO O PORCENTAJE
    var tengoCuponDineroOrPorcentaje by remember { mutableStateOf(false) }

    // CUANDO SE APLICA CUPON DINERO O PORCENTAJE, SABER CUANTO SE PAGARA AL FINAL
    var totalCancelarPorCupon by remember { mutableStateOf("") }

    // PARA MOSTRAR MODAL PARA ENVIAR ORDEN SI OR NO
    var showModal2BotonParaEnviarOrden by remember { mutableStateOf(false) }

    // MOSTRAR RESPUESTA DE ERROR PARA CUANDO ENVIAMOS LA ORDEN
    var showModal1Botonw by remember { mutableStateOf(false) }
    var modalTituloString by remember { mutableStateOf("") }
    var modalMensajeString by remember { mutableStateOf("") }

    // PARA CUANDO SE ENVIO ORDEN FINAL Y REDIRECCIONA A ORDENES
    var showModal1BotonFinal by remember { mutableStateOf(false) }
    var modalTituloStringFinal by remember { mutableStateOf("") }
    var modalMensajeStringFinal by remember { mutableStateOf("") }

    // NOTA FINAL PARA REDIRECCIONARME A ORDENES
    // PARA CUANDO SE ENVIO ORDEN FINAL Y REDIRECCIONA A ORDENES
    var showModal1BotonRedireccionar by remember { mutableStateOf(false) }
    var modalTituloStringRedireccionar by remember { mutableStateOf("") }
    var modalMensajeStringRedireccionar by remember { mutableStateOf("") }

    var idonesignal by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine

    // cargar datos
    LaunchedEffect(Unit) {
        idusuario = tokenManager.idUsuario.first()
        viewModel.informacionOrdenParaEnviarRetrofit(idusuario)
    }


    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(id = R.string.enviar_orden),
                colorResource(id = R.color.colorAppPrimary)
            )
        },
        // ⬇️ Botón fijo en bottomBar (maneja IME y barras de navegación)
        bottomBar = {
            if (datosCargados) {
                Button(
                    onClick = {
                        if (minimoInt0NoPuede == 0) {
                            CustomToasty(ctx, "Mínimo de Compra es Requerido", ToastType.INFO)
                        } else {
                            showModal2BotonParaEnviarOrden = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .navigationBarsPadding()
                        .imePadding()   // <- se queda aquí
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.colorAppPrimary),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(stringResource(R.string.confirmar_orden).uppercase())
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding) // ✅ no se acumulan
        ) {

            if (datosCargados) {

                // ===== CONTENIDO SCROLLEABLE/DE PANTALLA =====
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Faja Total
                    Surface(
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = txtSubTotalLetra,
                                color = Color.Black,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = totalString,
                                color = Color.Black,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Divider(color = Color.LightGray, thickness = 1.5.dp)

                    if (minimoInt0NoPuede == 0) {
                        // === CARD PARA MOSTRAR MINIMO DE CONSUMO ===
                        Spacer(Modifier.height(15.dp))

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E6EB))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = stringResource(id = R.string.minimo_de_consumo),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text = mensajeMinimoConsumoString,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    if (tengoCupon == 1) {
                        // === CARD CUPÓN APLICADO ===
                        Spacer(Modifier.height(15.dp))

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E6EB))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = stringResource(id = R.string.cupon),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text = textoCuponGanado,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp
                                )
                            }

                            if (tengoCuponDineroOrPorcentaje) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = stringResource(id = R.string.total_a_pagar),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = totalCancelarPorCupon,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }



                    Spacer(Modifier.height(15.dp))

                    // Card Dirección de entrega
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E6EB))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = stringResource(id = R.string.direccion_de_entrega),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = stringResource(id = R.string.cliente),
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = clienteString,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = stringResource(id = R.string.direccion),
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = direccionString,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Divider(color = Color.LightGray, thickness = 1.5.dp)

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.nota_para_la_orden_ejemplo),
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(6.dp))

                    TextField(
                        value = nota,
                        onValueChange = { nota = if (it.length <= 300) it else it.take(300) },
                        placeholder = { Text(stringResource(R.string.nota)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = colorResource(id = R.color.colorAzul),
                            unfocusedIndicatorColor = Color.Gray,
                            errorIndicatorColor = MaterialTheme.colorScheme.error,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        )
                    )

                    // ⛔️ Ya no hay Spacer(96.dp). El espacio lo maneja el bottomBar.
                    Spacer(Modifier.height(24.dp))
                }

                // ===== Modal de cupón =====

            } // -end if-datosCargados

            // === Modales y Loadings globales ===
            if (showModal1Botonw) {
                CustomModal1BotonTitulo(
                    showModal1Botonw,
                    modalTituloString,
                    modalMensajeString,
                    onDismiss = { showModal1Botonw = false }
                )
            }

            if (showModal1BotonFinal) {
                CustomModal1BotonTitulo(
                    showModal1BotonFinal,
                    modalTituloStringFinal,
                    modalMensajeStringFinal,
                    onDismiss = { showModal1BotonFinal = false

                        navController.navigate(Routes.VistaPrincipal.createRoute("ordenes")
                        ) {
                            popUpTo(Routes.VistaEnviarOrden.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            if (isLoading) LoadingModal(isLoading = isLoading)
            if (isLoadingEnviarOrden) LoadingModal(isLoading = isLoadingEnviarOrden)

            if (showModal2BotonParaEnviarOrden) {
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.enviar_orden),
                    onDismiss = { showModal2BotonParaEnviarOrden = false },
                    onAccept = {
                        showModal2BotonParaEnviarOrden = false

                        viewModelEnviarOrden.enviarOrdenRetrofit(idusuario, nota, textoCuponEscrito)
                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }

            if (showModal1BotonRedireccionar) {
                CustomModal1BotonTitulo(
                    showModal1BotonRedireccionar,
                    modalTituloStringRedireccionar,
                    modalMensajeStringRedireccionar,
                    onDismiss = {
                        showModal1BotonRedireccionar = false
                        navController.navigate(
                            Routes.VistaPrincipal.createRoute("ordenes")
                        ) {
                            popUpTo(Routes.VistaSplash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

        }
    }



    // CARGAR INFORMACION PANTALLA
    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {

            // CLIENTE SIN DIRECCION
            1 -> {
                    modalTituloString = "Nota"
                    modalMensajeString = "Se necesita Dirección de envio"
                    showModal1Botonw = true
            }
            // DATOS CORRECTOS
            2 -> {
                minimoInt0NoPuede = result.minimo
                mensajeMinimoConsumoString = result.mensaje ?: ""
                totalString = result.total ?: ""
                direccionString = result.direccion ?: ""
                clienteString = result.cliente ?: ""

                datosCargados = true
            }
            else -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
                navController.popBackStack()
            }
        }
    }




    resultadoEnviarOrden?.getContentIfNotHandled()?.let { result ->
        when (result.success) {

            // CERRADO
            1 -> {
                modalTituloString = "Nota"
                modalMensajeString = "Cerrado"
                showModal1Botonw = true
            }
            // CERRADO DESDE PANEL
            2 -> {
                modalTituloString = "Nota"
                modalMensajeString = result.mensaje ?: ""
                showModal1Botonw = true
            }
            // MINIMO DE CONSUMO ES BAJO
            3 -> {
                modalTituloString = "Nota"
                modalMensajeString = result.mensaje ?: ""
                showModal1Botonw = true
            }
            // CARRITO DE COMPRAS NO ENCONTRADO
            4 -> {
                modalTituloString = "Nota"
                modalMensajeString = result.mensaje ?: ""
                showModal1Botonw = true
            }
            5 -> {
                // ENVIADO CORRECTAMENTE
                modalTituloStringRedireccionar = "Nota"
                modalMensajeStringRedireccionar = "Orden enviada correctamente"
                showModal1BotonRedireccionar = true
            }
            else -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
                navController.popBackStack()
            }
        }
    }



}



