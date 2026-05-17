package com.tatanstudios.eltuncazometapan.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.eltuncazometapan.R
import com.tatanstudios.eltuncazometapan.model.rutas.Routes

val RobotoMediumFont = FontFamily(
    Font(R.font.robotomedium, FontWeight.Normal),
    Font(R.font.robotomedium, FontWeight.Bold)
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraToolbarColor(navController: NavController, titulo: String, backgroundColor: Color, estadoBotonAtras: Int = 0) {

    var isNavigating by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Medium,
            )
        },

        navigationIcon = {
            IconButton(
                onClick = {

                    if(estadoBotonAtras == 0){
                        if (!isNavigating) {
                            isNavigating = true
                            navController.popBackStack()
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.atras),
                    tint = Color.White // Color del ícono de navegación
                )
            }
        },
        actions = {
            // Puedes agregar acciones adicionales aquí si lo necesitas
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor, // Color de fondo de la barra
            navigationIconContentColor = Color.White, // Color del ícono de navegación
            titleContentColor = Color.White, // Color del título
            actionIconContentColor = Color.White // Color de las acciones
        ),
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 56.dp) // Define una altura mínima
    )
}


@Composable
fun CustomModal2Botones(
    showDialog: Boolean,
    message: String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    txtBtnAceptar: String,
    txtBtnCancelar: String
) {
    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = message,
                        fontSize = 17.sp,
                        color = colorResource(R.color.colorNegro),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón Cancelar (Gris claro)
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorGrisv2),
                                contentColor = Color.White
                            )
                        ) {
                            Text(txtBtnCancelar)
                        }

                        // Botón Aceptar (Verde)
                        Button(
                            onClick = onAccept,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorVerde), // Verde
                                contentColor = Color.White
                            )
                        ) {
                            Text(txtBtnAceptar)
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun OtpTextField(codigo: String, onTextChanged: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier.fillMaxWidth(), // Centrado horizontal
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = codigo,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            onValueChange = { newText ->
                if (newText.length <= 4) {
                    onTextChanged(newText)
                }
            },
            singleLine = true
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp), // Espaciado arriba/abajo
                horizontalArrangement = Arrangement.spacedBy(16.dp), // Separación entre dígitos
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val number = when {
                        index >= codigo.length -> ""
                        else -> codigo[index].toString()
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = number,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .size(48.dp)
                                .wrapContentSize(Alignment.Center)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(2.dp)
                                .background(Color.Black)
                        )
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraToolbarColorMenuPrincipal(navController: NavController, titulo: String, backgroundColor: Color) {

    var isNavigating by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Medium,
            )
        },

        actions = {
            IconButton(
                onClick = {
                    // Acción que quieres realizar al presionar el ícono
                    if (!isNavigating) {
                        isNavigating = true

                        navController.navigate(Routes.VistaPerfil.route) {
                            launchSingleTop = true
                        }

                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Person, // Cambia esto por tu ícono deseado
                    contentDescription = "Opciones",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor, // Color de fondo de la barra
            navigationIconContentColor = Color.White, // Color del ícono de navegación
            titleContentColor = Color.White, // Color del título
            actionIconContentColor = Color.White // Color de las acciones
        ),
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 56.dp) // Define una altura mínima
    )
}






@Composable
fun CustomModalCerrarSesion(
    showDialog: Boolean,
    message: String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = message,
                        fontSize = 18.sp,
                        color = colorResource(R.color.colorNegro),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorGrisv1),
                                contentColor = colorResource(R.color.colorBlanco),
                            ),
                        ) {
                            Text(stringResource(id = R.string.no), color = Color.White)
                        }

                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorAzul),
                                contentColor = colorResource(R.color.colorBlanco)
                            ),
                        ) {
                            Text(stringResource(id = R.string.si), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun CardHistorialOrden(
    orden: String,
    fecha: String,
    venta: String,
    estado: String?,
    haycupon: Int,
    cupon: String?,
    haypremio: Int,
    premio: String?, // textopremio
    direccion: String,
    notaOrden: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CampoTexto(stringResource(R.string.orden_numeral), orden)
            Spacer(modifier = Modifier.height(6.dp))
            CampoTexto(stringResource(R.string.direccion), direccion)
            Spacer(modifier = Modifier.height(6.dp))
            CampoTexto(stringResource(R.string.estado), estado)
            Spacer(modifier = Modifier.height(6.dp))
            CampoTexto(stringResource(R.string.total_a_pagar), venta)
            Spacer(modifier = Modifier.height(6.dp))
            CampoTexto(stringResource(R.string.fecha), fecha)
            Spacer(modifier = Modifier.height(6.dp))
            CampoTexto(stringResource(R.string.nota_orden), notaOrden)
            if (haycupon == 1){
                Spacer(modifier = Modifier.height(6.dp))
                CampoTexto(stringResource(R.string.cupon), cupon, colorResource(R.color.colorRojo)) }
            if (haypremio == 1){
                Spacer(modifier = Modifier.height(6.dp))
                CampoTexto(stringResource(R.string.premio), premio, colorResource(R.color.colorRojo))
            }
        }
    }
}



@Composable
fun CampoTexto(etiqueta: String, valor: String?,
               colorEtiqueta: Color = Color.Black
) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = "$etiqueta: ",
            fontWeight = FontWeight.Bold,
            color = colorEtiqueta,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = valor ?: "",
            color = colorEtiqueta,
            fontSize = 17.sp,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}




@Composable
fun ProductoListadoHistorialItemCard(
    cantidad: String,
    hayImagen: Int,
    imagenUrl: String,
    titulo: String?,
    descripcion: String?, // lo que el cliente escribe ejemplo (pollo, res)
    precio: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cantidad
            Box(
                modifier = Modifier
                    .background(color = colorResource(R.color.colorAzul), shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${cantidad}x",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            // Imagen del producto desde URL
            if(hayImagen == 1){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imagenUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.spinloading)
                        .error(R.drawable.camaradefecto)
                        .build(),
                    contentDescription = stringResource(R.string.imagen_por_defecto),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.camaradefecto),
                    contentDescription = stringResource(R.string.imagen_por_defecto),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }


            Spacer(modifier = Modifier.width(8.dp))

            // Título y descripción
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titulo?: "",
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis
                )
                if(!descripcion.isNullOrBlank()){
                    Text(
                        text = descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Precio
            Text(
                text = "$$precio",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}



@Composable
fun CardHorarioV1(nombre: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen a la izquierda
            Image(
                painter = painterResource(id = R.drawable.logoapp), // Reemplaza con tu imagen
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Texto a la derecha
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



@Composable
fun CardHorarioV2(titulo: String?, descripcion: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen a la izquierda
            Image(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Icono de fecha",
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Texto a la derecha
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titulo?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = descripcion?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}



@Composable
fun CardMisPuntos(nombre: String, txtPuntos: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
        ) {
            Text(
                text = nombre,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = txtPuntos?: "",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            )
        }
    }
}




@Composable
fun CardSeleccionPremioItem(
    texto: String,
    seleccionado: Int,
    puntos: Int,
    onClick: () -> Unit
) {
    val backgroundColor = if (seleccionado == 1) Color(0xFFF2FFDB) else Color.White
    val botonColor = if (seleccionado == 1) colorResource(R.color.colorRojo) else colorResource(R.color.colorVerde)
    val textoBoton = if (seleccionado == 1) "BORRAR SELECCIÓN" else "SELECCIONAR"

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icono_comida),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = texto,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Puntos: $puntos",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = botonColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = textoBoton,
                        color = Color.White
                    )
                }
            }
        }

        // Ícono de check arriba a la derecha
        if (seleccionado == 1) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Seleccionado",
                tint = Color(0xFF4CAF50), // Verde
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 4.dp) // Ajuste de posición
                    .size(24.dp)
            )
        }
    }
}


@Composable
fun CardMisDirecciones(
    nombre: String,
    seleccionado: Int,
    minimoCompra: String,
    direccion: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (seleccionado == 1) Color(0xFFF2FFDB) else Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Contenido principal
                Column(modifier = Modifier.weight(1f)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.House,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Nombre: $nombre",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Dirección: $direccion",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = minimoCompra,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    )
                }

                // Check alineado a la derecha y centrado verticalmente
                if (seleccionado == 1) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Seleccionado",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraToolbarColorDireccion(
    titulo: String,
    backgroundColor: Color,
    onBackClick: (() -> Unit)? = null // nuevo parámetro opcional
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Medium,
            )
        },
        actions = {
            IconButton(
                onClick = {
                    onBackClick?.invoke()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Opciones",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 56.dp)
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraToolbarColorCarritoCompras(
    navController: NavController,
    titulo: String,
    backgroundColor: Color,
    onDeleteClick: () -> Unit   // 👈 nuevo callback
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onDeleteClick() } // 👈 lo llamas aquí
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar carrito",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 56.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraToolbarColorParaListaOrdenes(titulo: String, backgroundColor: Color) {


    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Medium,
            )
        },

        actions = {
            // Puedes agregar acciones adicionales aquí si lo necesitas
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor, // Color de fondo de la barra
            navigationIconContentColor = Color.White, // Color del ícono de navegación
            titleContentColor = Color.White, // Color del título
            actionIconContentColor = Color.White // Color de las acciones
        ),
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 56.dp) // Define una altura mínima
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraToolbarColorOrdenesEstado(navController: NavController, titulo: String, backgroundColor: Color) {

    var isNavigating by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!isNavigating) {
                        isNavigating = true
                        navController.popBackStack()
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.atras),
                    tint = Color.White
                )
            }
        },
        actions = {},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 56.dp)
    )
}

