package com.tatanstudios.eltuncazometapan.vistas.opciones.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1Boton
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloMenuPrincipalCategoriasArray
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloMenuPrincipalPopularesArray
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.ui.theme.ColorBlanco
import com.tatanstudios.eltuncazometapan.ui.theme.ColorGris
import com.tatanstudios.eltuncazometapan.viewmodel.ListadoMenuPrincipal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuPrincipalScreen(
    navController: NavHostController,
    viewModel: ListadoMenuPrincipal = viewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val layoutDirection = LocalLayoutDirection.current

    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    var modeloListaCategoriasArray by remember { mutableStateOf(listOf<ModeloMenuPrincipalCategoriasArray>()) }

    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }

    val stringErrorReintentar = stringResource(R.string.error_reintentar_de_nuevo)

    LaunchedEffect(Unit) {
        idusuario = tokenManager.idUsuario.first()
        viewModel.listadoMenuPrincipalRetrofit(idusuario)
    }

    Scaffold(
        topBar = {
            BarraToolbarColorMenuPrincipal(
                navController,
                stringResource(R.string.menu),
                colorResource(R.color.colorAppPrimary)
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->

        val totalPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding(),
            start = contentPadding.calculateStartPadding(layoutDirection),
            end = contentPadding.calculateEndPadding(layoutDirection)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorCremaV1)),
            contentPadding = totalPadding
        ) {

            items(modeloListaCategoriasArray) { categoria ->
                val imagenUrl = "${RetrofitBuilder.urlImagenes}${categoria.imagen}"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            var isImageLoading by remember { mutableStateOf(true) }

                            AsyncImage(
                                model = ImageRequest.Builder(ctx)
                                    .data(imagenUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = categoria.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                onLoading = { isImageLoading = true },
                                onSuccess = { isImageLoading = false },
                                onError = { isImageLoading = false },
                                error = painterResource(R.drawable.camaradefecto),
                            )

                            // Spinner centrado solo mientras carga
                            if (isImageLoading) {
                                Image(
                                    painter = painterResource(R.drawable.spinloading),
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp) // 👈 tamaño fijo del spinner
                                )
                            }
                        }

                        // Nombre debajo
                        Text(
                            text = categoria.nombre ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // ── MODALES ────────────────────────────────────────────────
        if (isLoading) LoadingModal(true)

        LaunchedEffect(resultado) {
            resultado?.getContentIfNotHandled()?.let { result ->
                when (result.success) {
                    1 -> modeloListaCategoriasArray = result.arrayCategorias
                    else -> CustomToasty(ctx, stringErrorReintentar, ToastType.ERROR)
                }
            }
        }

        if (showModal1Boton) {
            CustomModal1Boton(showModal1Boton, modalMensajeString) { showModal1Boton = false }
        }

    } // end Scaffold
}



// REDIRECCIONAR
fun redireccionarAjustes(context: Context){
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}


private fun navigateToLogin(navController: NavHostController) {
    navController.navigate(Routes.VistaLogin.route) {
        popUpTo(Routes.VistaPrincipal.route) {
            inclusive = true // Elimina VistaPrincipal de la pila
        }
        launchSingleTop = true // Asegura que no se creen múltiples instancias de VistaLogin
    }
}


private fun navigateToDirecciones(
    navController: NavHostController,
    btnBloqueoAtras: Int = 0
) {

    /*navController.navigate(
        Routes.VistaMisDirecciones.createRoute(btnBloqueoAtras)
    ) {
        popUpTo(Routes.VistaMisDirecciones.route) {
            inclusive = true
        }
        launchSingleTop = true
    }*/
}


