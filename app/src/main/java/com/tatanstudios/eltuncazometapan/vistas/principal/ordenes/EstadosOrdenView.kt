package com.tatanstudios.eltuncazometapan.vistas.principal.ordenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.first
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColorOrdenesEstado
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1BotonTitulo
import com.tatanstudios.eltuncazometapan.componentes.CustomModal2Botones
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloOrdenesIndividualArray
import com.tatanstudios.eltuncazometapan.viewmodel.CalificarOrdenViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.CancelarOrdenViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.InformacionDeUnaOrdenViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EstadoOrdenScreen(
    navController: NavHostController,
    idorden: Int,
    viewModel: InformacionDeUnaOrdenViewModel = viewModel(),
    viewModelCancelar: CancelarOrdenViewModel = viewModel(),
    viewModelCalificar: CalificarOrdenViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val tokenManager = remember { TokenManager(ctx) }

    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingCancelar by viewModelCancelar.isLoading.observeAsState(false)
    val resultadoCancelar by viewModelCancelar.resultado.observeAsState()

    val isLoadingCalificar by viewModelCalificar.isLoading.observeAsState(false)
    val resultadoCalificar by viewModelCalificar.resultado.observeAsState()

    var modeloOrdenesArray by remember { mutableStateOf(listOf<ModeloOrdenesIndividualArray>()) }

    // cargar datos cuando cambia el id
    LaunchedEffect(idorden) {
        viewModel.informacionOrdenIndividualRetrofit(idorden)
    }

    var showRatingDialog by rememberSaveable { mutableStateOf(false) }
    var rating by rememberSaveable { mutableIntStateOf(1) } // ⭐ por defecto y mínima

    var showModalCancelarOrden by rememberSaveable { mutableStateOf(false) }

    var showModalOrdenYaFueIniciada by rememberSaveable { mutableStateOf(false) }
    var tituloOrdenYaIniciada by rememberSaveable { mutableStateOf("") }
    var mensajeOrdenYaIniciada by rememberSaveable { mutableStateOf("") }


    var showModalCalificar by rememberSaveable { mutableStateOf(false) }
    var tituloOrdenCalificada by rememberSaveable { mutableStateOf("") }
    var mensajeOrdenCalificada by rememberSaveable { mutableStateOf("") }


    // estado de refresco
    var refreshing by remember { mutableStateOf(false) }

    fun recargar() {
        refreshing = true
        viewModel.informacionOrdenIndividualRetrofit(idorden)
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, { recargar() })

    Scaffold(
        topBar = {
            BarraToolbarColorOrdenesEstado(
                navController,
                stringResource(R.string.estado_de_orden),
                colorResource(R.color.colorAppPrimary)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState) // 👈 habilita “jalón para recargar”
        ) {
            // Contenido con scroll
            if (isLoading && !refreshing) {
                LoadingModal(isLoading = true)
            } else {
                val orden = modeloOrdenesArray.firstOrNull()
                if (orden == null) {
                    Text(
                        text = stringResource(id = R.string.error_reintentar_de_nuevo),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()) // 👈 scroll vertical
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // ===========================

                        Text(
                            text = "Orden #: ${orden.id}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {

                                    navController.navigate(
                                        Routes.VistaListaProductosDeOrden.createRoute(orden.id)
                                    ) {
                                        launchSingleTop = true
                                    }

                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1565C0), // Azul
                                    contentColor = Color.White
                                )
                            ) { Text("Productos") }

                            if (orden.estadoIniciada == 0) {
                                Button(
                                    onClick = {

                                        // == CANCELAR ORDEN ==
                                        showModalCancelarOrden = true

                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFD32F2F), // Rojo
                                        contentColor = Color.White
                                    )
                                ) { Text("Cancelar") }
                            }
                        }

                        Divider()

                        Text(
                            text = "Estados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        EstadoItem(
                            titulo = if (orden.estadoIniciada == 1)
                                (orden.textoIniciada?.takeIf { it.isNotBlank() } ?: "Orden iniciada")
                            else
                                "Esperando Iniciar Orden",
                            activo = orden.estadoIniciada == 1,
                            fecha = orden.fechaEstimadaTxt
                        )

                        EstadoItem(
                            titulo = if (orden.estadoCamino == 1)
                                ("Motorista en Camino")
                            else
                                "En espera",
                            activo = orden.estadoCamino == 1,
                            fecha = orden.fechaCaminoTxt
                        )

                        EstadoItem(
                            titulo = if (orden.estadoEntregada == 1)
                                ("Orden Entregada")
                            else
                                "En espera",
                            activo = orden.estadoEntregada == 1,
                            fecha = orden.fechaEntregadaTxt
                        )

                        if (orden.estadoEntregada == 1) {
                            Button(
                                onClick = {
                                    rating = 1             // siempre inicia en 1
                                    showRatingDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .align(Alignment.CenterHorizontally),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E7D32),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                            ) { Text("Finalizar orden") }
                        }

                        if (orden.estadoCancelada == 1) {
                            EstadoItem(
                                titulo = "Cancelada",
                                activo = true,
                                fecha = orden.fechaCancelada,
                                colorActivo = Color(0xFFB00020)
                            )
                            orden.notaCancelada?.takeIf { it.isNotBlank() }?.let { nota ->
                                Text(
                                    text = nota,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Red,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Indicador de “pull to refresh”
            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            if(refreshing){
                LoadingModal(isLoading = true)
            }

            if(isLoadingCancelar){
                LoadingModal(isLoading = true)
            }

            if(isLoadingCalificar){
                LoadingModal(isLoading = true)
            }

            if(showModalCancelarOrden){
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.cancelar_orden),
                    onDismiss = { showModalCancelarOrden = false },
                    onAccept = {
                        showModalCancelarOrden = false

                            viewModelCancelar.cancelarOrdenRetrofit(
                                idorden
                            )
                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }

            if (showRatingDialog) {
                RatingDialog(
                    rating = rating,
                    onRatingChange = { new ->
                        rating = new.coerceAtLeast(1) // mínimo 1
                    },
                    onConfirm = {
                        showRatingDialog = false
                        // TODO: Llama a tu ViewModel para enviar la calificación
                        viewModelCalificar.calificarOrdenRetrofit(idorden,rating)
                    },
                    onCancel = { showRatingDialog = false }
                )
            }

            if(showModalOrdenYaFueIniciada){
                CustomModal1BotonTitulo(showModalOrdenYaFueIniciada, tituloOrdenYaIniciada, mensajeOrdenYaIniciada, onDismiss = {
                    showModalOrdenYaFueIniciada = false
                    viewModel.informacionOrdenIndividualRetrofit(idorden)
                })
            }

            if(showModalCalificar){
                CustomModal1BotonTitulo(showModalCalificar, tituloOrdenCalificada, mensajeOrdenCalificada, onDismiss = {
                    showModalCalificar = false

                    // SALIR
                    navController.navigate(Routes.VistaPrincipal.createRoute("ordenes")
                    ) {
                        popUpTo(Routes.VistaEstadoOrden.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                })
            }
        }
    }


    // manejar resultado retrofit
    resultado?.getContentIfNotHandled()?.let { result ->
        refreshing = false
        if (result.success == 1) {
            modeloOrdenesArray = result.ordenes

        } else {
            CustomToasty(
                ctx,
                stringResource(id = R.string.error_reintentar_de_nuevo),
                ToastType.ERROR
            )
        }
    }


    // manejar resultado retrofit
    resultadoCancelar?.getContentIfNotHandled()?.let { result ->

        when (result.success) {

            1 -> {
                // ORDEN YA FUE INICIADA POR RESTAURANTE
                val titulo = result.titulo ?: ""
                val mensaje = result.mensaje ?: ""
                tituloOrdenYaIniciada = titulo
                mensajeOrdenYaIniciada = mensaje
                showModalOrdenYaFueIniciada = true
            }
            2 -> {
                // ORDEN CANCELADA CORRECTAMENTE
               // val titulo = result.titulo ?: ""
              //  val mensaje = result.mensaje ?: ""

                CustomToasty(
                    ctx,
                    stringResource(id = R.string.orden_cancelada),
                    ToastType.SUCCESS
                )

                navController.popBackStack()
            }
            else -> {
                // Error, mostrar Toast
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
            }
        }
    }


    resultadoCalificar?.getContentIfNotHandled()?.let { result ->
        refreshing = false
        if (result.success == 1) {
            val titulo = result.titulo ?: ""
            val mensaje = result.mensaje ?: ""

            tituloOrdenYaIniciada = titulo
            mensajeOrdenCalificada = mensaje
            showModalCalificar = true

        } else {
            CustomToasty(
                ctx,
                stringResource(id = R.string.error_reintentar_de_nuevo),
                ToastType.ERROR
            )
        }
    }
}


@Composable
private fun RatingDialog(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Califica tu experiencia") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Selecciona de 1 a 5 estrellas",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    (1..5).forEach { i ->
                        IconButton(onClick = { onRatingChange(i) }) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "$i estrellas",
                                tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Enviar") }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Cancelar") }
        }
    )
}

@Composable
private fun EstadoItem(
    titulo: String,
    activo: Boolean,
    fecha: String? = null,
    colorActivo: Color = Color(0xFF2E7D32)
) {
    val chipColor = if (activo) colorActivo else Color(0xFFBDBDBD)
    val textColor = if (activo) Color.Black else Color(0xFF616161)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(chipColor)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                fontWeight = if (activo) FontWeight.SemiBold else FontWeight.Normal
            )
            if (!fecha.isNullOrEmpty()) {
                Text(
                    text = fecha,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}

