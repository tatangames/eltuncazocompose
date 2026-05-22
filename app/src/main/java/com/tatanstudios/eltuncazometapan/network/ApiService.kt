package com.tatanstudios.eltuncazometapan.network

import com.tatanstudios.eltuncazometapan.model.modelos.ModeloCarrito
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloDatosBasicos
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloDirecciones
import com.tatanstudios.eltuncazometapan.model.modelos.ModeloHistorialOrdenes
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
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // VERIFICACION DE NUMERO
    @POST("cliente/login")
    @FormUrlEncoded
    fun verificarUsuarioPassword(@Field("usuario") telefono: String,
                          @Field("password") password: String,
                          ): Single<ModeloDatosBasicos>

    // REGISTRARSE
    @POST("cliente/registro")
    @FormUrlEncoded
    fun registrarme(@Field("usuario") telefono: String,
                    @Field("password") password: String
    ): Single<ModeloDatosBasicos>




    //*****************************************************************************
    // LISTA MENU PRINCIPAL
    @POST("cliente/lista/servicios-bloque")
    @FormUrlEncoded
    fun listadoMenuPrincipal(@Field("id") id: String,
    ): Single<ModeloMenuPrincipal>

    // ACTUALIZAR CONTRASENA
    @POST("cliente/actualizar/password")
    @FormUrlEncoded
    fun actualizarPassword(@Field("id") id: String,
                           @Field("password") password: String,
    ): Single<ModeloDatosBasicos>

    // LISTADO DE DIRECCIONES
    @POST("cliente/listado/direcciones")
    @FormUrlEncoded
    fun listadoDirecciones(@Field("id") id: String,
    ): Single<ModeloDirecciones>

    // REGISTRAR NUEVA DIRECCION
    @POST("cliente/nueva/direccion")
    @FormUrlEncoded
    fun registrarNuevaDireccion(@Field("id") idusuario: String,
                                @Field("nombre") nombre: String,
                                @Field("direccion") direccion: String,
                                @Field("punto_referencia") puntoReferencia: String?,
                                @Field("telefono") telefono: String,

                                ): Single<ModeloDatosBasicos>

    // SELECCIONAR DIRECCION
    @POST("cliente/seleccionar/direccion")
    @FormUrlEncoded
    fun seleccionarDireccion(@Field("id") id: String,
                             @Field("dirid") dirid: Int,
    ): Single<ModeloDatosBasicos>


    // BORRAR DIRECCION
    @POST("cliente/eliminar/direccion")
    @FormUrlEncoded
    fun borrarDireccion(@Field("id") id: String,
                        @Field("dirid") dirid: Int,
    ): Single<ModeloDatosBasicos>




    // LISTADO DE PRODUCTOS DE UNA CATEGORIA
    @POST("cliente/servicios/listado/menu")
    @FormUrlEncoded
    fun listadoProductos(@Field("categoria") idcategoria: Int,
    ): Single<ModeloProductos>


    // INFORMACION DE UN PRODUCTO PARA METER AL CARRITO
    @POST("cliente/informacion/producto")
    @FormUrlEncoded
    fun informacionProducto(@Field("productoid") idproducto: Int,
    ): Single<ModeloInformacionProducto>


    // AGREGAR PRODUCTO A CARRITO TEMPORAL
    @POST("cliente/carrito/producto/agregar")
    @FormUrlEncoded
    fun enviarProductosAlCarrito(@Field("clienteid") clienteid: String,
                                 @Field("productoid") productoid: Int,
                                 @Field("cantidad") cantidad: Int,
                                 @Field("notaproducto") notaproducto: String?,
    ): Single<ModeloDatosBasicos>



    // LISTADO CARRITO DE COMPRAS
    @POST("cliente/carrito/ver/orden")
    @FormUrlEncoded
    fun listadoCarritoCompras(@Field("clienteid") idcliente: String,
    ): Single<ModeloCarrito>


    // PETICION PARA BORRAR CARRITO DE COMPRAS
    @POST("cliente/carrito/borrar/orden")
    @FormUrlEncoded
    fun borrarCarritoCompras(@Field("clienteid") idcliente: String,
    ): Single<ModeloDatosBasicos>


    // ELIMINAR PRODUCTO INDIVIDUAL DEL CARRITO COMPRAS
    @POST("cliente/carrito/eliminar/producto")
    @FormUrlEncoded
    fun eliminarFilaCarrito(@Field("clienteid") idcliente: String,
                            @Field("carritoid") idcarrito: Int,
    ): Single<ModeloDatosBasicos>


    // INFORMACION DE PRODUCTO PARA EDITARLO
    @POST("cliente/carrito/ver/producto")
    @FormUrlEncoded
    fun informacionProductoParaEditar(@Field("clienteid") idcliente: String,
                                      @Field("carritoid") idcarrito: Int,
    ): Single<ModeloInformacionProductoEditar>


    // ACTUALIZAR PRODUCTO EDITADO
    @POST("cliente/carrito/cambiar/cantidad")
    @FormUrlEncoded
    fun actualizarProductoEditado(@Field("clienteid") idcliente: String,
                                  @Field("cantidad") cantidad: Int,
                                  @Field("carritoid") idcarrito: Int,
                                  @Field("nota") nota: String?,
    ): Single<ModeloDatosBasicos>


    // INFORMACION DE ORDEN PARA ENVIAR
    @POST("cliente/carrito/ver/proceso-orden")
    @FormUrlEncoded
    fun informacionOrdenParaEnviar(@Field("clienteid") idcliente: String,
    ): Single<ModeloInformacionOrdenParaEnviar>


    // ENVIAR ORDEN FINAL
    @POST("cliente/proceso/orden/estado-1")
    @FormUrlEncoded
    fun enviarOrdenFinal(@Field("clienteid") idcliente: String,
                         @Field("nota") nota: String?,
                         @Field("version") version: String?,
    ): Single<ModeloDatosBasicos>




    /// LISTADO DE ORDENES
    @POST("cliente/ver/ordenes-activas")
    @FormUrlEncoded
    fun listadoOrdenes(@Field("clienteid") clienteid: String,
    ): Single<ModeloOrdenes>


    // INFORMACION DE UNA ORDEN PARA VER LOS ESTADOS
    @POST("cliente/ver/estado-orden")
    @FormUrlEncoded
    fun informacionOrdenIndividual(@Field("ordenid") ordenid: Int,
    ): Single<ModeloOrdenesIndividual>


    // INFORMACION LISTADO DE UN PRODUCTO DE UNA ORDEN
    @POST("cliente/listado/productos/ordenes")
    @FormUrlEncoded
    fun listadoProductosOrden(@Field("ordenid") ordenid: Int,
    ): Single<ModeloProductosDeOrden>


    // CANCELAR ORDEN ANTES DE INICIARSE
    @POST("cliente/proceso/orden/cancelar")
    @FormUrlEncoded
    fun cancelarOrden(@Field("ordenid") idorden: Int,
    ): Single<ModeloDatosBasicos>


    // COMPLETAR ORDEN
    @POST("cliente/proceso/completar/orden")
    @FormUrlEncoded
    fun completarOrden(@Field("ordenid") idorden: Int,
    ): Single<ModeloDatosBasicos>



}


