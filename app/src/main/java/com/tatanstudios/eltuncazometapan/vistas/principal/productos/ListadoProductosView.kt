package com.tatanstudios.eltuncazometapan.vistas.principal.productos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import kotlinx.coroutines.launch
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
import androidx.compose.ui.unit.sp
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColor
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductosArray
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.viewmodel.ListadoProductosViewModel
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductosTerceraArray

@Composable
fun ListadoProductosScreen(navController: NavHostController,
                           idCategoria: Int,
                           viewModel: ListadoProductosViewModel = viewModel()
) {

    val ctx = LocalContext.current
    var boolDatosCargados by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine
    val keyboardController = LocalSoftwareKeyboardController.current

    var modeloListaProductosArray: List<ModeloProductosArray> by remember { mutableStateOf(listOf<ModeloProductosArray>()) }


    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.listadoProductosRetrofit(idCategoria)
        }
    }

    // ocultar teclado
    keyboardController?.hide()


    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.productos),
                colorResource(R.color.colorAppPrimary),
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (boolDatosCargados) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    modeloListaProductosArray.forEach { categoria ->
                        // Encabezado de categoría (rojo subrayado + fondo crema)
                        item {
                            CategoriaHeader(title = categoria.nombre ?: "")
                        }

                        // Productos de la categoría
                        items(categoria.listaProductoCategoriaTercera) { producto ->
                            ProductoItemCard(
                                producto = producto,
                                onClick = {

                                    navController.navigate(
                                        Routes.VistaInformacionProducto.createRoute(producto.id)
                                    ) {
                                        popUpTo(Routes.VistaInformacionProducto.route) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        if (isLoading) LoadingModal(isLoading = true)

        resultado?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    modeloListaProductosArray = result.listaProductoCategoria
                    boolDatosCargados = true
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
    }




}

// Encabezado de categoría con estilo solicitado
@Composable
fun CategoriaHeader(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.colorBlanco)) // define este color (p.ej. #FFFDF4)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = colorResource(id = R.color.colorAppPrimary),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(
            color = colorResource(id = R.color.colorAppPrimary),
            thickness = 2.dp,
            modifier = Modifier
                .padding(top = 4.dp)
        )
    }
}

// Card del producto con imagen abajo-derecha y fallback
@Composable
fun ProductoItemCard(
    producto: ModeloProductosTerceraArray,
    onClick: () -> Unit
) {
    val baseUrlImagenes = "${RetrofitBuilder.urlImagenes}${producto.imagen}"
    var traeImagen = (producto.utilizaImagen == 1)
    if (producto.imagen.isNullOrBlank()) traeImagen = false

    val imageSlot = 96.dp // ancho reservado para la imagen + padding

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            // Texto: reservamos espacio a la derecha para la imagen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .padding(end = imageSlot),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = producto.nombre ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (!producto.descripcion.isNullOrBlank()) {
                    Text(
                        text = producto.descripcion.replace("\\r\\n|\\n".toRegex(), "\n"),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        lineHeight = 16.sp
                    )
                }

                if (!producto.precio.isNullOrBlank()) {
                    Text(
                        text = "$${producto.precio}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.colorAppPrimary)
                    )
                }
            }

            // Imagen: CENTRADA verticalmente a la derecha
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)   // <-- clave
                    .padding(end = 10.dp)
                    .size(72.dp)
            ) {
                if (traeImagen) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(baseUrlImagenes)
                            .crossfade(true)
                            .placeholder(R.drawable.spinloading)
                            .error(R.drawable.camaradefecto)
                            .build(),
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.camaradefecto),
                        contentDescription = producto.nombre,
                        modifier = Modifier
                            .size(72.dp)                 // 👈 define tamaño cuadrado
                            .clip(CircleShape)           // 👈 recorta en forma de círculo
                            .border(2.dp, Color.LightGray, CircleShape), // 👈 borde opcional
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}