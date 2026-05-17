package com.tatanstudios.eltuncazometapan.vistas.principal.productos

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColor
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductosDeOrdenArray
import com.tatanstudios.eltuncazometapan.viewmodel.ListadoProductosDeUnaOrdenViewModel

@Composable
fun ListadoProductosDeUnaOrdenScreen(
    navController: NavHostController,
    idorden: Int,
    viewModel: ListadoProductosDeUnaOrdenViewModel = viewModel()
) {
    val ctx = LocalContext.current
    var boolDatosCargados by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    var modeloListaProductosArray: List<ModeloProductosDeOrdenArray> by remember { mutableStateOf(listOf()) }

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.listadoProductosDeUnaOrdenRetrofit(idorden)
        }
    }

    keyboardController?.hide()

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController = navController,
                titulo = stringResource(R.string.productos),
                backgroundColor = colorResource(R.color.colorAppPrimary),
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (boolDatosCargados) {
                if (modeloListaProductosArray.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay productos",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        itemsIndexed(
                            items = modeloListaProductosArray,
                            key = { index, item -> "${item.id}_$index" }
                        ) { _, producto ->
                            ProductoItemCardDeOrden(
                                producto = producto,
                                onClick = { }
                            )
                        }
                    }
                }
            }

            if (isLoading) LoadingModal(isLoading = true)

            resultado?.getContentIfNotHandled()?.let { result ->
                when (result.success) {
                    1 -> {
                        modeloListaProductosArray = result.productos
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
}


@Composable
fun ProductoItemCardDeOrden(
    producto: ModeloProductosDeOrdenArray,
    onClick: () -> Unit
) {
    val baseUrlImagenes = "${RetrofitBuilder.urlImagenes}${producto.imagen}"
    var traeImagen = (producto.utiliza_imagen == 1)
    if (producto.imagen.isNullOrBlank()) traeImagen = false

    val imageSlot = 96.dp

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
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.colorNegro)
                )

                Text(
                    text = "Precio: $${producto.precio}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.colorNegro)
                )

                Text(
                    text = "Cantidad: ${producto.cantidad}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.colorNegro)
                )

                Text(
                    text = "Total: ${producto.multiplicado}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.colorNegro)
                )

                if (!producto.nota.isNullOrBlank()) {
                    Text(
                        text = "Nota: ${producto.nota}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.colorRojo)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
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
                        contentDescription = producto.nombreproducto,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.camaradefecto),
                        contentDescription = producto.nombreproducto,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.LightGray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}