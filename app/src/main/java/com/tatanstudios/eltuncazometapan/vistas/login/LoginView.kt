package com.tatanstudios.eltuncazometapan.vistas.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.TextStyle
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tatanstudios.eltuncazometapan.componentes.BloqueTextFieldLogin
import com.tatanstudios.eltuncazometapan.componentes.BloqueTextFieldPassword
import com.tatanstudios.eltuncazometapan.componentes.CustomModal1Boton
import com.tatanstudios.eltuncazometapan.componentes.CustomToasty
import com.tatanstudios.eltuncazometapan.componentes.LoadingModal
import com.tatanstudios.eltuncazometapan.componentes.ToastType
import com.tatanstudios.eltuncazometapan.extras.TokenManager
import com.tatanstudios.eltuncazometapan.model.rutas.Routes
import com.tatanstudios.eltuncazometapan.viewmodel.LoginViewModel

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.Arrangement


import kotlinx.coroutines.launch


@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = viewModel()) {

    val ctx = LocalContext.current
    val usuario by viewModel.usuario.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val resultado by viewModel.resultado.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    var isPasswordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val tokenManager = remember { TokenManager(ctx) }
    val scope = rememberCoroutineScope()

    val loginButtonColor = if (isPressed) {
        colorResource(id = R.color.colorAppPrimary).copy(alpha = 0.8f)
    } else {
        colorResource(id = R.color.colorAppPrimary)
    }
    val elevation by animateDpAsState(if (isPressed) 12.dp else 6.dp)

    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }

    // Strings fuera de lambdas
    val stringUsuarioRequerido = stringResource(R.string.usuario_es_requerido)
    val stringPasswordRequerido = stringResource(R.string.password_es_requerido)
    val stringDatosIncorrectos = stringResource(R.string.datos_incorrectos)

    // Estado para navegación segura
    var navegarAPrincipal by remember { mutableStateOf<String?>(null) }

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

            // ── BLOQUE SUPERIOR MORADO ──────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(colorResource(id = R.color.colorAppPrimary)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .background(Color.White, shape = CircleShape)
                            .shadow(8.dp, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logoapp),
                            contentDescription = stringResource(id = R.string.descripcion_negocio),
                            modifier = Modifier.size(110.dp)
                        )
                    }
                }
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

            // ── TÍTULO ─────────────────────────────────────────────
            Text(
                text = stringResource(id = R.string.descripcion_negocio),
                fontFamily = FontFamily(Font(R.font.arthura_medium)),
                color = Color.Black,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-8).dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── CARD DE LOGIN ──────────────────────────────────────
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón ingresar
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            when {
                                usuario.isBlank() -> {
                                    modalMensajeString = stringUsuarioRequerido
                                    showModal1Boton = true
                                }
                                password.isBlank() -> {
                                    modalMensajeString = stringPasswordRequerido
                                    showModal1Boton = true
                                }
                                else -> {
                                    viewModel.verificarUsuarioPasssword()
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
                            text = stringResource(id = R.string.iniciar_sesion).uppercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Registrarse
                    Text(
                        text = stringResource(R.string.registrarse),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Routes.VistaRegistro.route) {
                                    popUpTo(Routes.VistaRegistro.route) { inclusive = true }
                                }
                            },
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.colorAppPrimary),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // ── MODALES ────────────────────────────────────────────────
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

        // ── RESULTADO API ──────────────────────────────────────────
        resultado?.getContentIfNotHandled()?.let { result ->

            Log.d("LOGIN_RESULT", "success: ${result.success}, id: ${result.id}, mensaje: ${result.mensaje}")

            when (result.success) {
                1 -> {
                    navegarAPrincipal = result.id.toString()
                }
                else -> {
                    modalMensajeString = stringDatosIncorrectos
                    showModal1Boton = true
                }
            }
        }
    }
}