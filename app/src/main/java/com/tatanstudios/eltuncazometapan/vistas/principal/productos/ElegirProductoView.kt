package com.tatanstudios.eltuncazometapan.vistas.principal.productos

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
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColor
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1BotonTitulo
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloInformacionProductoArray
import com.tatanstudios.eltuncazometapan.viewmodel.EnviarProductoAlCarritoViewModel
import com.tatanstudios.eltuncazometapan.viewmodel.InformacionProductoViewModel
import java.util.Locale

@Composable
fun ElegirProductoScreen(
    navController: NavHostController,
    idProducto: Int,
    viewModel: InformacionProductoViewModel = viewModel(),
    viewModelEnviar: EnviarProductoAlCarritoViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val tokenManager = remember { TokenManager(ctx) }
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingEnviar by viewModelEnviar.isLoading.observeAsState(true)
    val resultadoEnviar by viewModelEnviar.resultado.observeAsState()

    // MODAL 1 BOTON
    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }
    var modalTituloString by remember { mutableStateOf("") }

    // MOSTRAR MODAL SI NOTA ES REQUERIDA Y NO HE ESCRITO NADA
    var showModalNotaRequerida by remember { mutableStateOf(false) }
    var notaStringQueEsRequerido by remember { mutableStateOf("") }

    // estado UI
    var producto by remember { mutableStateOf<ModeloInformacionProductoArray?>(null) }
    var cantidad by remember { mutableStateOf(1) }

    // SI REQUIERE NOTA, ESTO DIRA EL MOTIVO
    var notaInput by remember { mutableStateOf("") }
    var errorNotaObligatoria by remember { mutableStateOf(false) }
    var idusuario by remember { mutableStateOf("") }

    // cargar datos
    LaunchedEffect(idProducto) {
        idusuario = tokenManager.idUsuario.first()
        viewModel.informacionProductoRetrofit(idProducto)
    }

    // manejar resultado retrofit
    resultado?.getContentIfNotHandled()?.let { result ->
        if (result.success == 1 && result.informacionProducto.isNotEmpty()) {
            producto = result.informacionProducto.first() // asumo 1 ítem por id
        } else {
            CustomToasty(
                ctx,
                stringResource(id = R.string.error_reintentar_de_nuevo),
                ToastType.ERROR
            )
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.elegir_cantidad),
                colorResource(R.color.colorAppPrimary)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            producto?.let { prod ->
                // parsear precio unitario
                val precioUnit = prod.precio?.toDoubleOrNull() ?: 0.0
                val total = precioUnit * cantidad
                notaStringQueEsRequerido = prod.nota ?: ""

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // IMAGEN (o placeholder)
                    item {
                        val tieneImagen = prod.utilizaImagen == 1 && !prod.imagen.isNullOrBlank()
                        if (tieneImagen) {
                            // Cárgala desde tu backend (reemplaza por tu URL base)

                            val imagenUrl = "${RetrofitBuilder.urlImagenes}${prod.imagen}"

                            AsyncImage(
                                model = ImageRequest.Builder(ctx)
                                    .data(imagenUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = prod.nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(R.drawable.spinloading),
                                error = painterResource(R.drawable.camaradefecto)
                            )
                        }
                    }

                    // NOMBRE
                    item {
                        Text(
                            text = prod.nombre ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = colorResource(R.color.colorAppPrimary)
                        )
                    }

                    // DESCRIPCIÓN
                    val desc = (prod.descripcion ?: "").replace("\r\n", "\n").replace("\\r\\n", "\n")

                    if (desc.isNotBlank()) {
                        item {
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    item { Spacer(Modifier.height(6.dp)) }

                    // PRECIO UNITARIO
                    item {
                        Text(
                            text = stringResource(R.string.precio) + ": " + formatearUSD(precioUnit),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // STEPPER CANTIDAD
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledTonalIconButton(
                                onClick = { if (cantidad > 1) cantidad-- },
                                enabled = cantidad > 1,
                                modifier = Modifier.size(44.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = colorResource(R.color.colorAppPrimary),      // Fondo gris claro
                                    contentColor = Color.White             // Icono negro
                                )
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Menos")
                            }


                            Text(
                                text = cantidad.toString(),
                                modifier = Modifier.padding(horizontal = 16.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                            FilledTonalIconButton(
                                onClick = { if (cantidad < 50) cantidad++ },
                                enabled = cantidad < 50,
                                modifier = Modifier.size(44.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = colorResource(R.color.colorAppPrimary),      // Fondo gris claro
                                    contentColor = Color.White             // Icono negro
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Más")
                            }
                        }
                    }

                    // NOTAS (máx 300)
                    item {
                        Column {
                            Text(
                                text = stringResource(R.string.notas),
                                style = MaterialTheme.typography.labelLarge
                            )
                            TextField(
                                value = notaInput,
                                onValueChange = {
                                    errorNotaObligatoria = false
                                    notaInput = if (it.length <= 300) it else it.take(300)
                                },
                                placeholder = { Text(stringResource(R.string.nota_para_este_producto)) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = false,
                                maxLines = 3,
                                isError = errorNotaObligatoria,
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = colorResource(R.color.colorAzul), // línea enfocada
                                    unfocusedIndicatorColor = Color.Gray,                     // línea sin foco
                                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                                    focusedContainerColor = Color.Transparent,                // sin fondo
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent
                                )
                            )
                            if (errorNotaObligatoria) {
                                Text(
                                    text = stringResource(R.string.nota_es_requerida),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    // TOTAL
                    item {
                        Text(
                            text = formatearUSD(total),
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    item { Spacer(Modifier.height(15.dp)) }

                    // BOTÓN AGREGAR
                    item {
                        Button(
                            onClick = {
                                // validar nota obligatoria si utiliza_nota == 1
                                if (prod.utilizaNota == 1 && notaInput.isBlank()) {
                                    errorNotaObligatoria = true

                                    showModalNotaRequerida = true

                                    return@Button
                                }

                                val cantidadElegida = cantidad
                                val notaElegida = notaInput.trim()

                                viewModelEnviar.enviarProductoCarritoRetrofit(idusuario, idProducto,
                                    cantidadElegida, notaElegida)

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorAppPrimary), // 🔴 fondo del botón
                                contentColor = Color.White                         // ⚪ texto/icono
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.agregar_a_la_orden),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    item { Spacer(Modifier.height(15.dp)) }

                }
            }

            if (isLoading) LoadingModal(isLoading = true)
            if (isLoadingEnviar) LoadingModal(isLoading = true)


            if(showModal1Boton){
                CustomModal1BotonTitulo(showModal1Boton, modalTituloString, modalMensajeString, onDismiss = {showModal1Boton = false})
            }

            if(showModalNotaRequerida){
                CustomModal1BotonTitulo(showModalNotaRequerida, "Nota Requerida", notaStringQueEsRequerido, onDismiss = {showModalNotaRequerida = false})
            }
        }
    }

    resultadoEnviar?.getContentIfNotHandled()?.let { result ->
        if(result.success == 1){
            CustomToasty(
                ctx,
                stringResource(id = R.string.agregado_al_carrito),
                ToastType.SUCCESS
            )
            navController.popBackStack()
        }else{
            CustomToasty(
                ctx,
                stringResource(id = R.string.error_reintentar_de_nuevo),
                ToastType.ERROR
            )
        }
    }
}

/** ===== Utilidades ===== **/

private fun formatearUSD(monto: Double): String {
    // Formato simple tipo $10.25
    return "$" + String.format(Locale.US, "%.2f", monto)
}