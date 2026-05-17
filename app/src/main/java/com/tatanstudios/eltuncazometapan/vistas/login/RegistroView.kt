package com.tatanstudios.eltuncazometapan.vistas.login

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tatanstudios.eltuncazometapan.R
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tatanstudios.eltuncazometapan.componentes.BloqueTextFieldLogin
import com.tatanstudios.eltuncazometapan.componentes.BloqueTextFieldPassword
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1Boton
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1BotonTitulo
import com.tatanstudios.eltuncazometapan.componentes.CustomModal2Botones
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.viewmodel.RegistroViewModel
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(navController: NavHostController, viewModel: RegistroViewModel = viewModel()) {

    val ctx = LocalContext.current
    val usuario by viewModel.usuario.observeAsState("")
    val password by viewModel.password.observeAsState("")

    val resultado by viewModel.resultado.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val tokenManager = remember { TokenManager(ctx) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val loginButtonColor = if (isPressed) {
        colorResource(id = R.color.colorAppPrimary).copy(alpha = 0.8f)
    } else {
        colorResource(id = R.color.colorAppPrimary)
    }

    val elevation by animateDpAsState(if (isPressed) 12.dp else 6.dp)
    val scope = rememberCoroutineScope()

    // MODAL 1 BOTON
    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }

    // titulo y mensaje de respuestas
    var textoTituloApi by remember { mutableStateOf("") }
    var textoMensajeApi by remember { mutableStateOf("") }
    var showDialogApi by remember { mutableStateOf(false) }

    var showModal2Boton by remember { mutableStateOf(false) }

    // Strings fuera de lambdas
    val stringUsuarioEsRequerido = stringResource(R.string.usuario_es_requerido)
    val stringPasswordEsRequerido = stringResource(R.string.password_es_requerido)
    val texto4CaracteresMinimo = stringResource(R.string.minimo_4_caracteres)
    val stringErrorReintentar = stringResource(R.string.error_reintentar_de_nuevo)

    // Estado para navegar después del registro exitoso
    var navegarAPrincipal by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect para navegación segura
    LaunchedEffect(navegarAPrincipal) {
        navegarAPrincipal?.let { id ->
            tokenManager.saveID(id)
            navController.navigate(Routes.VistaPrincipal.createRoute("menu")) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
            navegarAPrincipal = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(colorResource(id = R.color.colorAppPrimary)),
                contentAlignment = Alignment.Center // 👈 ya centra, pero la status bar lo desplaza
            ) {
                Text(
                    text = stringResource(id = R.string.crear_una_cuenta),
                    fontFamily = FontFamily(Font(R.font.arthura_medium)),
                    color = Color.White,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .statusBarsPadding() // 👈 esto empuja el texto hacia abajo respetando la status bar
                )
            }

            // ── WAVE ───────────────────────────────────────────────
            Image(
                painter = painterResource(id = R.drawable.icono_wave),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .offset(y = (-1).dp),
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── CARD ───────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    BloqueTextFieldLogin(
                        text = usuario,
                        onTextChanged = { viewModel.setUsuario(it) },
                        maxLength = 20
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    BloqueTextFieldPassword(
                        text = password,
                        onTextChanged = { viewModel.setPassword(it) },
                        isPasswordVisible = isPasswordVisible,
                        onPasswordVisibilityChanged = { isPasswordVisible = it },
                        maxLength = 16
                    )

                    Text(
                        text = stringResource(R.string.minimo_4_caracteres), // o el string que tengas
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón registrarse
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            when {
                                usuario.isBlank() -> {
                                    modalMensajeString = stringUsuarioEsRequerido
                                    showModal1Boton = true
                                }
                                password.isBlank() -> {
                                    modalMensajeString = stringPasswordEsRequerido
                                    showModal1Boton = true
                                }
                                password.length < 4 -> {
                                    modalMensajeString = texto4CaracteresMinimo
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
                            text = stringResource(id = R.string.registrarse).uppercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // ── MODALES ────────────────────────────────────────────────
        if (showDialogApi) {
            CustomModal1BotonTitulo(
                showDialog = showDialogApi,
                title = textoTituloApi ,
                message = textoMensajeApi,
                onDismiss = { showDialogApi = false }
            )
        }

        if (showModal1Boton) {
            CustomModal1Boton(
                showModal1Boton,
                modalMensajeString,
                onDismiss = { showModal1Boton = false }
            )
        }

        if (isLoading) {
            LoadingModal(isLoading = isLoading)
        }

        // CONFIRMAR PARA REGISTRARSE
        if (showModal2Boton) {
            CustomModal2Botones(
                showDialog = true,
                message = stringResource(R.string.registrarse),
                onDismiss = { showModal2Boton = false },
                onAccept = {
                    showModal2Boton = false
                    scope.launch {
                        viewModel.registroRetrofit()
                    }
                },
                stringResource(R.string.si),
                stringResource(R.string.no),
            )
        }

        resultado?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // USUARIO YA REGISTRADO
                    textoTituloApi = result.titulo ?: ""
                    textoMensajeApi = result.mensaje ?: ""
                    showDialogApi = true
                }
                2 -> {
                    // REGISTRADO CORRECTAMENTE
                    navegarAPrincipal = result.id.toString()
                }
                else -> {
                    CustomToasty(ctx, stringErrorReintentar, ToastType.ERROR)
                }
            }
        }
    }
}


fun getVersionName(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "N/A"
    } catch (e: PackageManager.NameNotFoundException) {
        "N/A"
    }
}