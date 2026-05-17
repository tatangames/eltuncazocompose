package com.tatanstudios.eltuncazometapan.vistas.principal.ordenes

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColorOrdenesEstado
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1BotonTitulo
import com.tatanstudios.eltuncazometapan.componentes.CustomModal2Botones
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloOrdenesIndividualArray
import com.tatanstudios.eltuncazometapan.viewmodel.CancelarOrdenViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.CompletarOrdenViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.InformacionDeUnaOrdenViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EstadoOrdenScreen(
    navController: NavHostController,
    idorden: Int,
    viewModel: InformacionDeUnaOrdenViewModel = viewModel(),
    viewModelCancelar: CancelarOrdenViewModel = viewModel(),
    viewModelCompletar: CompletarOrdenViewModel = viewModel(),
) {
    val ctx = LocalContext.current

    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingCancelar by viewModelCancelar.isLoading.observeAsState(false)
    val resultadoCancelar by viewModelCancelar.resultado.observeAsState()

    val isLoadingCompletar by viewModelCompletar.isLoading.observeAsState(false)
    val resultadoCompletar by viewModelCompletar.resultado.observeAsState()

    var modeloOrdenesArray by remember { mutableStateOf(listOf<ModeloOrdenesIndividualArray>()) }

    LaunchedEffect(idorden) {
        viewModel.informacionOrdenIndividualRetrofit(idorden)
    }

    var showModalCancelarOrden by rememberSaveable { mutableStateOf(false) }
    var showModalCompletarOrden by rememberSaveable { mutableStateOf(false) }

    var showModalOrdenYaFueIniciada by rememberSaveable { mutableStateOf(false) }
    var tituloOrdenYaIniciada by rememberSaveable { mutableStateOf("") }
    var mensajeOrdenYaIniciada by rememberSaveable { mutableStateOf("") }

    var refreshing by remember { mutableStateOf(false) }

    fun recargar() {
        refreshing = true
        viewModel.informacionOrdenIndividualRetrofit(idorden)
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, { recargar() })

    // Dentro del composable, antes del Scaffold:
    BackHandler {
        navController.popBackStack()
    }

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
                .pullRefresh(pullRefreshState)
        ) {
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
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Orden #: ${orden.id}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        // Botones: Productos + Cancelar (solo si no fue iniciada)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate(
                                        Routes.VistaListaProductosDeOrden.createRoute(orden.id)
                                    ) { launchSingleTop = true }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1565C0),
                                    contentColor = Color.White
                                )
                            ) { Text("Productos") }

                            if (orden.estadoIniciada == 0) {
                                Button(
                                    onClick = { showModalCancelarOrden = true },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFD32F2F),
                                        contentColor = Color.White
                                    )
                                ) { Text("Cancelar") }
                            }
                        }

                        Divider()

                        Text(
                            text = "Estado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        // Solo el estado de iniciada
                        EstadoItem(
                            titulo = if (orden.estadoIniciada == 1)
                                (orden.textoIniciada?.takeIf { it.isNotBlank() } ?: "Orden iniciada")
                            else
                                "Esperando iniciar orden",
                            activo = orden.estadoIniciada == 1,
                            fecha = if (orden.estadoIniciada == 1) orden.fechaEstimadaTxt else null
                        )

                        // Botón Completar: solo cuando la orden fue iniciada
                        if (orden.estadoIniciada == 1) {
                            Button(
                                onClick = { showModalCompletarOrden = true },
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .align(Alignment.CenterHorizontally),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E7D32),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                            ) { Text("Completar") }
                        }

                        // Estado cancelada si aplica
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

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            if (refreshing) LoadingModal(isLoading = true)
            if (isLoadingCancelar) LoadingModal(isLoading = true)
            if (isLoadingCompletar) LoadingModal(isLoading = true)

            // Modal cancelar orden
            if (showModalCancelarOrden) {
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.cancelar_orden),
                    onDismiss = { showModalCancelarOrden = false },
                    onAccept = {
                        showModalCancelarOrden = false
                        viewModelCancelar.cancelarOrdenRetrofit(idorden)
                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }

            // Modal completar orden
            if (showModalCompletarOrden) {
                CustomModal2Botones(
                    showDialog = true,
                    message = "¿Deseas finalizar esta orden?",
                    onDismiss = { showModalCompletarOrden = false },
                    onAccept = {
                        showModalCompletarOrden = false
                        viewModelCompletar.completarOrdenRetrofit(idorden)
                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }

            // Modal respuesta de cancelar (cuando el restaurante ya inició)
            if (showModalOrdenYaFueIniciada) {
                CustomModal1BotonTitulo(
                    showModalOrdenYaFueIniciada,
                    tituloOrdenYaIniciada,
                    mensajeOrdenYaIniciada,
                    onDismiss = {
                        showModalOrdenYaFueIniciada = false
                        viewModel.informacionOrdenIndividualRetrofit(idorden)
                    }
                )
            }
        }
    }

    // Resultado: cargar orden
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

    // Resultado: cancelar orden
    resultadoCancelar?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                tituloOrdenYaIniciada = result.titulo ?: ""
                mensajeOrdenYaIniciada = result.mensaje ?: ""
                showModalOrdenYaFueIniciada = true
            }
            2 -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.orden_cancelada),
                    ToastType.SUCCESS
                )
                navController.popBackStack()
            }
            else -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
            }
        }
    }

    // Resultado: completar orden
    resultadoCompletar?.getContentIfNotHandled()?.let { result ->
        if (result.success == 1) {
            CustomToasty(
                ctx,
                "Orden completada",
                ToastType.SUCCESS
            )
            navController.navigate(Routes.VistaPrincipal.createRoute("ordenes")) {
                popUpTo(Routes.VistaEstadoOrden.route) { inclusive = true }
                launchSingleTop = true
            }
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