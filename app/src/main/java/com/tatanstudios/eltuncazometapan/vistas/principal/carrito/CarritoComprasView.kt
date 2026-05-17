package com.tatanstudios.eltuncazometapan.vistas.principal.carrito

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import kotlinx.coroutines.flow.first
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColorCarritoCompras
import com.tatanstudios.eltuncazometapan.componentes.CustomModal2Botones
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloCarritoTemporal
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.viewmodel.BorrarCarritoComprasViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.BorrarFilaCarritoViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.ListadoCarritoComprasViewModel
import kotlinx.coroutines.launch

@Composable
fun CarritoComprasScreen(
    navController: NavHostController,
    viewModel: ListadoCarritoComprasViewModel = viewModel(),
    viewModelBorrarCarrito: BorrarCarritoComprasViewModel = viewModel(),
    viewModelBorrarFila: BorrarFilaCarritoViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingBorrar by viewModelBorrarCarrito.isLoading.observeAsState(true)
    val resultadoBorrar by viewModelBorrarCarrito.resultado.observeAsState()

    val isLoadingFilaBorrar by viewModelBorrarFila.isLoading.observeAsState(true)
    val resultadoFilaBorrar by viewModelBorrarFila.resultado.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var idusuario by remember { mutableStateOf("") }
    var datosCargados by remember { mutableStateOf(false) }
    var hayProductoNoDisponible by remember { mutableStateOf(false) }
    var hayDireccionRegistrada by remember { mutableStateOf(true) }

    var subtotal: String by remember { mutableStateOf("") }
    var modeloListaCarritoArray by remember {
        mutableStateOf(listOf<ModeloCarritoTemporal>())
    }

    var showModal2BotonParaBorrarCarrito by remember { mutableStateOf(false) }
    var recargarListado by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        idusuario = TokenManager(ctx).idUsuario.first()
        viewModel.listadoCarritoComprasRetrofit(idusuario)
    }

    LaunchedEffect(recargarListado) {
        if (recargarListado && idusuario.isNotEmpty()) {
            recargarListado = false
            viewModel.listadoCarritoComprasRetrofit(idusuario)
        }
    }

    keyboardController?.hide()

    Scaffold(
        topBar = {
            BarraToolbarColorCarritoCompras(
                navController,
                stringResource(R.string.carrito),
                colorResource(R.color.colorAppPrimary),
                onDeleteClick = {
                    if (datosCargados) {
                        if (modeloListaCarritoArray.isNotEmpty()) {
                            showModal2BotonParaBorrarCarrito = true
                        } else {
                            CustomToasty(ctx, "No hay Productos", ToastType.INFO)
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (datosCargados) {
                val carritoVacio = modeloListaCarritoArray.isEmpty()
                BarraSubtotal(
                    subtotal = subtotal,
                    isEmpty = carritoVacio,
                    sinDireccion = !carritoVacio && !hayDireccionRegistrada,
                    onClick = {
                        when {
                            carritoVacio -> { /* no hace nada */ }
                            !hayDireccionRegistrada -> {
                                // Redirigir a agregar dirección
                                navController.navigate(
                                    Routes.VistaMisDirecciones.createRoute(0)
                                ) {
                                    launchSingleTop = true
                                }
                            }
                            !hayProductoNoDisponible -> {
                                navController.navigate(Routes.VistaEnviarOrden.route) {
                                    popUpTo(Routes.VistaEnviarOrden.route) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (datosCargados) {
                if (modeloListaCarritoArray.isEmpty()) {
                    // ── Carrito vacío ──────────────────────────────────────
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(72.dp),
                                tint = Color.LightGray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.carrito_vacio),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    // ── Lista de productos ─────────────────────────────────
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = modeloListaCarritoArray,
                            key = { it.carritoid }
                        ) { p ->
                            ItemCarritoSwipeAction(
                                p = p,
                                onClick = { item ->
                                    navController.navigate(
                                        Routes.VistaEditarProducto.createRoute(item.carritoid)
                                    ) {
                                        popUpTo(Routes.VistaInformacionProducto.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                onSwipeAction = { item ->
                                    scope.launch {
                                        viewModelBorrarFila.eliminarFilaCarritoRetrofit(idusuario, item.carritoid)
                                    }
                                }
                            )
                            Spacer(Modifier.height(6.dp))
                        }
                    }
                }
            }

            if (isLoading) LoadingModal(true)
            if (isLoadingBorrar) LoadingModal(true)
            if (isLoadingFilaBorrar) LoadingModal(true)

            if (showModal2BotonParaBorrarCarrito) {
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.borrar_carrito),
                    onDismiss = { showModal2BotonParaBorrarCarrito = false },
                    onAccept = {
                        showModal2BotonParaBorrarCarrito = false
                        scope.launch {
                            viewModelBorrarCarrito.eliminarCarritoComprasRetrofit(idusuario)
                        }
                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }
        }
    }

    // ── Resultado listado ──────────────────────────────────────────────────
    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                modeloListaCarritoArray = result.listadoCarritoTemporal
                hayProductoNoDisponible = (result.estadoProductoGlobal == 1)
                hayDireccionRegistrada = (result.hayDireccionRegistrada == 1)
                subtotal = result.subTotal ?: ""
                datosCargados = true
            }
            2 -> {
                modeloListaCarritoArray = emptyList()
                datosCargados = true
                subtotal = "0.00"
            }
            else -> {
                CustomToasty(ctx, stringResource(id = R.string.error_reintentar_de_nuevo), ToastType.ERROR)
            }
        }
    }

    // ── Resultado borrar carrito completo ──────────────────────────────────
    resultadoBorrar?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1, 2 -> {
                CustomToasty(ctx, stringResource(id = R.string.carrito_borrado), ToastType.SUCCESS)
                navController.popBackStack()
            }
            else -> {
                CustomToasty(ctx, stringResource(id = R.string.error_reintentar_de_nuevo), ToastType.ERROR)
            }
        }
    }

    // ── Resultado borrar fila ──────────────────────────────────────────────
    resultadoFilaBorrar?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                datosCargados = false
                CustomToasty(ctx, stringResource(id = R.string.carrito_borrado), ToastType.SUCCESS)
                navController.popBackStack()
            }
            2, 3 -> {
                datosCargados = false
                CustomToasty(ctx, stringResource(id = R.string.producto_eliminado), ToastType.SUCCESS)
                recargarListado = true
            }
            else -> {
                CustomToasty(ctx, stringResource(id = R.string.error_reintentar_de_nuevo), ToastType.ERROR)
            }
        }
    }
}

// ── ItemCarritoSwipeAction ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCarritoSwipeAction(
    p: ModeloCarritoTemporal,
    onClick: (ModeloCarritoTemporal) -> Unit,
    onSwipeAction: (ModeloCarritoTemporal) -> Unit
) {
    val scope = rememberCoroutineScope()
    val itemShape = RoundedCornerShape(8.dp)

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onSwipeAction(p)
                false
            } else false
        },
        positionalThreshold = { distance -> distance * 0.5f }
    )

    SwipeToDismissBox(
        modifier = Modifier
            .fillMaxWidth()
            .clip(itemShape)
            .background(MaterialTheme.colorScheme.surface),
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(itemShape)
                    .background(Color(0xFFFFE5E5))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                AssistChip(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                        onSwipeAction(p)
                        scope.launch { dismissState.reset() }
                    },
                    label = { Text("Borrar", color = Color.White) },
                    leadingIcon = {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = colorResource(id = R.color.colorAppPrimary)
                    )
                )
            }
        },
        content = {
            ItemCarritoCard(p = p, onClick = onClick)
        }
    )
}

// ── ItemCarritoCard ───────────────────────────────────────────────────────────

@Composable
private fun ItemCarritoCard(
    p: ModeloCarritoTemporal,
    onClick: (ModeloCarritoTemporal) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(p) },
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CantidadBadge(cantidad = p.cantidad)
            Spacer(Modifier.width(8.dp))

            if (p.utilizaImagen == 1 && !p.imagen.isNullOrBlank()) {
                val imagenUrl = "${RetrofitBuilder.urlImagenes}${p.imagen}"
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imagenUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    placeholder = painterResource(R.drawable.spinloading),
                    error = painterResource(R.drawable.camaradefecto),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.camaradefecto),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = p.nombre ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!p.titulo.isNullOrBlank() || !p.mensaje.isNullOrBlank()) {
                    Text(
                        text = (p.titulo.orEmpty() + " " + p.mensaje.orEmpty()).trim(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = p.precioformat ?: "",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End,
                modifier = Modifier.widthIn(min = 72.dp)
            )
        }
        Divider(thickness = 1.dp, color = Color(0xFFE0E0E0))
    }
}

// ── CantidadBadge ─────────────────────────────────────────────────────────────

@Composable
private fun CantidadBadge(cantidad: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(24.dp)
            .widthIn(min = 36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF1976D2))
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = "${cantidad}x",
            color = Color.White,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
    }
}

// ── BarraSubtotal ─────────────────────────────────────────────────────────────

@Composable
private fun BarraSubtotal(
    subtotal: String,
    isEmpty: Boolean,
    sinDireccion: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isEmpty      -> Color.Gray
        sinDireccion -> Color(0xFFF57C00) // naranja — advertencia
        else         -> colorResource(R.color.colorAppPrimary)
    }

    Surface(
        shadowElevation = 6.dp,
        modifier = Modifier.clickable(enabled = !isEmpty) { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding()
        ) {
            // Aviso de dirección faltante
            if (sinDireccion) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddLocation,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.agregar_direccion_para_continuar),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Fila principal: subtotal / mensaje vacío + flecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        isEmpty      -> stringResource(R.string.carrito_vacio)
                        sinDireccion -> stringResource(R.string.toca_para_agregar_direccion)
                        else         -> stringResource(R.string.subtotal) + ": $$subtotal"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                if (!isEmpty) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}