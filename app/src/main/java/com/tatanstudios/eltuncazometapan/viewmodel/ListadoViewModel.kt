package com.tatanstudios.eltuncazometapan.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.eltuncazometapan.extras.Event
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloCarrito
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloDatosBasicos
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloDirecciones
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloHistorialOrdenes
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloHorario
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloInfoProducto
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloInformacionOrdenParaEnviar
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloInformacionProducto
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloInformacionProductoEditar
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloMenuPrincipal
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloOrdenes
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloOrdenesIndividual
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloPoligonos
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloPremios
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductoHistorialOrdenes
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductos
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloProductosDeOrden
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.http.Field
import kotlin.Int




class LoginViewModel : ViewModel() {
    private val _usuario = MutableLiveData<String>()
    val usuario: LiveData<String> get() = _usuario

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setUsuario(usuario: String) {
        _usuario.value = usuario
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun verificarUsuarioPasssword() {
        if (isRequestInProgress) return

        isRequestInProgress = true
        _isLoading.value = true

        //Log.d("LOGIN_REQUEST", "usuario: ${_usuario.value}, password: ${_password.value}")

        disposable = RetrofitBuilder.getApiService()
            .verificarUsuarioPassword(_usuario.value!!, _password.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                    //Log.d("LOGIN_RESPONSE", "success: ${response.success}")
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                    //Log.e("LOGIN_ERROR", "Error: ${error.message}", error)
                    _resultado.value = Event(ModeloDatosBasicos(success = 0))
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class RegistroViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _usuario = MutableLiveData<String>()
    val usuario: LiveData<String> get() = _usuario

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _correo = MutableLiveData<String?>()
    val correo: LiveData<String?> get() = _correo


    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setUsuario(usuario: String) {
        _usuario.value = usuario
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setCorreo(correo: String) {
        _correo.value = correo
    }

    fun registroRetrofit() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().registrarme(_usuario.value!!, _password.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}







// PANTALLA PRINCIPAL -> OPCION MENU
class ListadoMenuPrincipal() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloMenuPrincipal>>()
    val resultado: LiveData<Event<ModeloMenuPrincipal>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoMenuPrincipalRetrofit(id: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoMenuPrincipal(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                    Log.d("MENU_RESPONSE", "success: ${response.success}")
                },
                { error ->
                    _isLoading.value = false
                    Log.e("MENU_ERROR", "Error: ${error.message}", error)
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class ActualizarPasswordViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _password = MutableLiveData<String>()
    val passowrd: LiveData<String> get() = _password

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setPassword(password: String) {
        _password.value = password
    }

    fun actualizarContrasenaRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().actualizarPassword(idusuario, _password.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}

class ListadoDireccionesViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDirecciones>>()
    val resultado: LiveData<Event<ModeloDirecciones>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoDireccionesRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoDirecciones(idusuario)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}







class RegistroNuevaDireccionViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> get() = _nombre

    private val _direccion = MutableLiveData<String>()
    val direccion: LiveData<String> get() = _direccion

    // UNICO OPCIONAL
    private val _puntoReferencia = MutableLiveData<String?>()
    val puntoReferencia: LiveData<String?> get() = _puntoReferencia

    private val _telefono = MutableLiveData<String>()
    val telefono: LiveData<String> get() = _telefono


    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setNombre(nombre: String) {
        _nombre.value = nombre
    }

    fun setDireccion(direccion: String) {
        _direccion.value = direccion
    }

    fun setPuntoReferencia(puntoReferencia: String) {
        _puntoReferencia.value = puntoReferencia
    }

    fun setTelefono(telefono: String) {
        _telefono.value = telefono
    }

    fun registrarNuevaDireccionRetrofit(
        idusuario: String,
        idzona: String,
        latitud: String,
        longitud: String,
        latitudreal: String?,
        longitudreal: String?
    ) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        _isLoading.value = true

        Log.d("DIRECCION_REQUEST", """
        idusuario: $idusuario
        nombre: ${_nombre.value}
        direccion: ${_direccion.value}
        puntoReferencia: ${_puntoReferencia.value}
        idzona: $idzona
        latitud: $latitud
        longitud: $longitud
        latitudreal: $latitudreal
        longitudreal: $longitudreal
        telefono: ${_telefono.value}
    """.trimIndent())

        disposable = RetrofitBuilder.getApiService().registrarNuevaDireccion(
            idusuario,
            _nombre.value ?: "",           // 👈 evita crash si es null
            _direccion.value ?: "",        // 👈
            _puntoReferencia.value ?: "",  // 👈 este es el más probable que cause crash
            idzona,
            latitud,
            longitud,
            latitudreal,
            longitudreal,
            _telefono.value ?: ""          // 👈
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                    Log.d("DIRECCION_RESPONSE", "success: ${response.success}")
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                    Log.e("DIRECCION_ERROR", "Error: ${error.message}", error)
                    _resultado.value = Event(ModeloDatosBasicos(success = 0))
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class SeleccionarDireccionViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun seleccionarDireccionRetrofit(idusuario: String, dirid: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().seleccionarDireccion(idusuario, dirid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class BorrarDireccionViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun borrarDireccionRetrofit(idusuario: String, dirid: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().borrarDireccion(idusuario, dirid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class ListadoProductosViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloProductos>>()
    val resultado: LiveData<Event<ModeloProductos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoProductosRetrofit(idcategoria: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoProductos(idcategoria = idcategoria)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class InformacionProductoViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloInformacionProducto>>()
    val resultado: LiveData<Event<ModeloInformacionProducto>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun informacionProductoRetrofit(idproducto: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().informacionProducto(idproducto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



class EnviarProductoAlCarritoViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun enviarProductoCarritoRetrofit(clienteid: String, productoid: Int, cantidad: Int, notaproducto: String?) {
        if (isRequestInProgress) return

        isRequestInProgress = true


        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().enviarProductosAlCarrito(clienteid, productoid, cantidad, notaproducto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



class ListadoCarritoComprasViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloCarrito>>()
    val resultado: LiveData<Event<ModeloCarrito>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoCarritoComprasRetrofit(idcliente: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoCarritoCompras(idcliente)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



class BorrarCarritoComprasViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun eliminarCarritoComprasRetrofit(idcliente: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().borrarCarritoCompras(idcliente)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class BorrarFilaCarritoViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun eliminarFilaCarritoRetrofit(idcliente: String, idcarritofila: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().eliminarFilaCarrito(idcliente, idcarritofila)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



class InformacionProductoEditadoViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloInformacionProductoEditar>>()
    val resultado: LiveData<Event<ModeloInformacionProductoEditar>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun informacionProductoEditarRetrofit(idcliente: String, idfilacarrito: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().informacionProductoParaEditar(idcliente, idfilacarrito)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



class ActualizarProductoEditadoViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun actualizarProductoEditadoRetrofit(idcliente: String, cantidad: Int, idcarritofila: Int, nota: String?) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().actualizarProductoEditado(idcliente, cantidad, idcarritofila, nota)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class InformacionOrdenParaEnviarViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloInformacionOrdenParaEnviar>>()
    val resultado: LiveData<Event<ModeloInformacionOrdenParaEnviar>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun informacionOrdenParaEnviarRetrofit(idcliente: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().informacionOrdenParaEnviar(idcliente)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class EnviarOrdenFinalViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun enviarOrdenRetrofit(idcliente: String, nota: String?, version: String?) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().enviarOrdenFinal(idcliente, nota, version, )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class InformacionDeUnaOrdenViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloOrdenesIndividual>>()
    val resultado: LiveData<Event<ModeloOrdenesIndividual>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun informacionOrdenIndividualRetrofit(ordenid: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().informacionOrdenIndividual(ordenid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



class CancelarOrdenViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun cancelarOrdenRetrofit(ordenid: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().cancelarOrden(ordenid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class ListadoOrdenesViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloOrdenes>>()
    val resultado: LiveData<Event<ModeloOrdenes>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoOrdenesRetrofit(idcliente: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoOrdenes(idcliente)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}

class OcultarOrdenViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun ocultarOrdenRetrofit(ordenid: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().ocultarOrden(ordenid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class CalificarOrdenViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun calificarOrdenRetrofit(ordenid: Int, valor: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().calificarOrden(ordenid, valor)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}














