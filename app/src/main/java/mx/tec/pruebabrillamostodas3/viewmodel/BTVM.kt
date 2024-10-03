package mx.tec.pruebabrillamostodas3.viewmodel

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.tec.pruebabrillamostodas3.model.Comentario
import mx.tec.pruebabrillamostodas3.model.Direccion
import mx.tec.pruebabrillamostodas3.model.Foro
import mx.tec.pruebabrillamostodas3.model.ListaForo
import mx.tec.pruebabrillamostodas3.model.ModelConnectionR
import mx.tec.pruebabrillamostodas3.model.Order
import mx.tec.pruebabrillamostodas3.model.Producto
import mx.tec.pruebabrillamostodas3.model.Usuario

/**
 * Clase que contiene la lógica de negocio de la aplicación
 *
 * @author Iker Fuentes
 * @author Santiago Chevez
 * @author Alan Vega
 * @author Andres Cabrera
 * @author Cesar Augusto
 *
 */
class BTVM: ViewModel() {
    val modeloR: ModelConnectionR = ModelConnectionR()

    //EstadoFecha de la fecha de nacimiento
    private val _estadoFecha = MutableLiveData("Seleccionar Fecha")
    val estadoFecha: LiveData<String> = _estadoFecha

    //EstadoDatePicker para escoger la fecha de nacimiento
    private val _showDatePicker = MutableLiveData(false)
    val showDatePicker: LiveData<Boolean> = _showDatePicker

    //Estado para que sirva como bandera global en la aplicación
    private val _once = MutableStateFlow(false)
    val once: StateFlow<Boolean> = _once
    fun setOnce(show: Boolean) {
        _once.value = show
    }

    //Estado contraseña perdida
    private val _contrasenaPerdida = MutableLiveData(false)
    val contrasenaPerdida: LiveData<Boolean> = _contrasenaPerdida

    //Estado cambio de contraseña
    private val _cambioContrasena = MutableLiveData(false)
    val cambioContrasena: LiveData<Boolean> = _cambioContrasena

    //Estado Lista Productos proveniente de modelo
    private val _estadoListaProductosModelo = MutableStateFlow(listOf<Producto>())
    val estadoListaProductosModelo: StateFlow<List<Producto>> = _estadoListaProductosModelo
    //Estado cantidad de Productos que se van a recibir del modelo
    private val _estadoCantidadProductosModelo = MutableStateFlow(0)
    val estadoCantidadProductosModelo: StateFlow<Int> = _estadoCantidadProductosModelo

    //Estado Lista Productos que se van a mostrar en la vista
    private val _estadoListaProducto = MutableStateFlow(mutableListOf<EstadoProducto>())
    val estadoListaProducto: StateFlow<MutableList<EstadoProducto>> = _estadoListaProducto

    //Estado carrito que se encarga de guardar los productos en el carrito,
    //asi como el total del precio de los productos
    private val _estadoCarrito = MutableStateFlow<Carrito>(Carrito())
    val estadoCarrito: StateFlow<Carrito> = _estadoCarrito

    //Estado producto que se encarga de guardar el producto seleccionado para saber cuanto se debe de añadir al carrito
    private val _estadoanadirCarrito = MutableStateFlow<Pair<EstadoProducto,Int>>(Pair(EstadoProducto(1,"","",0,0,0,0,""),1))
    val estadoAnadirCarrito: StateFlow<Pair<EstadoProducto,Int>> = _estadoanadirCarrito

    //Estado Producto Seleccionado nos da el producto seleccionado para mostrar en la tienda
    private val _estadoSeleccionado = MutableStateFlow(-1)
    val estadoSeleccionado: StateFlow<Int> = _estadoSeleccionado

    //Estado Expanded nos sirve para saber si el se tiene extendido la lista de Estados para la dirección
    private val _estadoExpanded = MutableStateFlow(false)
    val estadoExpanded: StateFlow<Boolean> = _estadoExpanded

    //Estado usuario nos guarda la información del usuario.
    private val _estadoUsuario = MutableStateFlow<EstadoUsuario>(EstadoUsuario())
    val estadoUsuario: StateFlow<EstadoUsuario> = _estadoUsuario

    //Estado errores estado que levanta errores en las distintas vistas que pueda cometer el usuario
    private val _estadoErrors = MutableStateFlow<EstadoErrors>(EstadoErrors())
    val estadoErrors: StateFlow<EstadoErrors> = _estadoErrors

    //Estado que nos guarda la dirección por si el usuario no guarda los cambios de la dirección
    private val _estadoCopiaDireccion = MutableStateFlow<Direccion>(Direccion())
    val estadoCopiaDireccion: StateFlow<Direccion> = _estadoCopiaDireccion
    var copiaListaProductos = mutableListOf<EstadoProducto>()

    //Estado categorias es el estado que nos muestra las categorías de productos
    private val _estadoCategorias : MutableStateFlow<MutableSet<String>> = MutableStateFlow(mutableSetOf<String>())
    val estadoCategorias : StateFlow<MutableSet<String>> = _estadoCategorias

    //Estado categoria seleccionada muestra que categoría está seleccionada de productos en la tienda
    private val _categoriaSeleccionada = MutableStateFlow("Todas")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada

    //Estado de la lista filtrada por categoría
    var listaFiltradaPorCategoria = mutableListOf<EstadoProducto>()

    //Foros la lista de foros que se van a mostrar en la vista
    private val _estadoForo : MutableStateFlow<List<Foro>> = MutableStateFlow<List<Foro>>(listOf())
    val estadoForo: StateFlow<List<Foro>> = _estadoForo
    //Comentarios los comentarios de cada foro que se van a mostrar en la vista
    private val _estadoComentarios : MutableStateFlow<List<Comentario>> = MutableStateFlow<List<Comentario>>(listOf())
    val estadoComentarios: StateFlow<List<Comentario>> = _estadoComentarios

    /**
     * Función que obtiene los productos del modelo
     * @author Iker Fuentes
     * @author Santiago Chevez
     * @author Alan Vega
     * @author Andres Cabrera
     */
    fun getProductos() {
        viewModelScope.launch {
            if (_estadoListaProducto.value.isEmpty()) { // Verificar si la lista está vacía
                try {
                    val (cantidad, productos) = modeloR.getProductsWithToken(estadoUsuario.value.key)
                    _estadoCantidadProductosModelo.value = cantidad
                    _estadoListaProductosModelo.value = productos
                    val nuevaLista = mutableListOf<EstadoProducto>() // Crear una nueva lista
                    for (producto in estadoListaProductosModelo.value) {
                        nuevaLista.add(
                            EstadoProducto(
                                id = producto.id,
                                nombre = producto.nombre,
                                descripcion = producto.descripcion,
                                precio_normal = producto.precio_normal,
                                precio_rebajado = producto.precio_rebajado,
                                rebaja = producto.rebaja,
                                categoria = producto.categoria,
                                imagen = modeloR.getProductImage(producto.id),
                                cantidad= producto.cantidad
                            )
                        )
                        _estadoCategorias.value.add(producto.categoria)
                    }
                    _estadoListaProducto.value = nuevaLista // Emitir la nueva lista
                    copiaListaProductos = nuevaLista
                } catch (e: Exception) {
                    println(e)
                }
            }
        }

    }

    /**
     * Función para filtrar productos por categoría
     * @author Iker Fuentes
     * @author Andrés Cabrera
     * @param categoria Categoría a filtrar
     */
    fun setListaFiltradaPorCategoria(categoria: String) {
        listaFiltradaPorCategoria.clear() //limpia lista
        for (producto in copiaListaProductos) {
            if (producto.categoria == categoria) {
                listaFiltradaPorCategoria.add(producto)
            }
        }
        _estadoListaProducto.value = listaFiltradaPorCategoria
    }

    /**
     * Función para establecer la categoría seleccionada
     * @author Andrés Cabrera
     * @param categoria Categoría seleccionada
     */
    fun setCategoriaSeleccionada(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    /**
     * Función para resetear la lista filtrada por categoría
     * @author Iker Fuentes
     */
    fun resetListaFiltradaPorCategoria() {
        _estadoListaProducto.value = copiaListaProductos
        listaFiltradaPorCategoria.clear()
    }

    /**
     * Función para establecer el producto seleccionado
     * @author Iker Fuentes
     * @param IdProd Id del producto seleccionado
     */
    fun setEstadoSeleccionado(IdProd: Int) {
        _estadoSeleccionado.value = IdProd
    }

    /**
     * Función para agregar una dirección a la lista de direcciones del usuario
     * @author Iker Fuentes
     * @param direccion Dirección a agregar
     */
    fun addAddress(direccion: Direccion) {
        viewModelScope.launch {
            try {
                modeloR.addAddress(estadoUsuario.value.key,  direccion)
            } catch (e: Exception) {
                println(e)

            }
        }
    }

    /**
     * Función para actualizar una dirección del usuario
     * @author Santiago Chevez
     * @param direccion La nueva dirección
     */
    fun updateAddress(direccion: Direccion) {
        viewModelScope.launch {
            try {
                modeloR.updateAddress(estadoUsuario.value.key,  direccion)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    /**
     * Función para agregar un producto al carrito
     * @author Santiago Chevez
     * @param producto Producto a agregar
     * @param cantidad Cantidad del producto a agregar
     */
    fun addProducto(producto: EstadoProducto, cantidad: Int) {
        estadoCarrito.value.productos.add(Pair(producto, cantidad))
    }

    /**
     * Función para eliminar un producto del carrito
     * @author Santiago Chevez
     * @param producto Producto a eliminar
     * @param cantidad Cantidad del producto a eliminar
     */
    fun removeProducto(producto: EstadoProducto, cantidad: Int) {
        val newProductos = estadoCarrito.value.productos.toMutableList()
        newProductos.remove(Pair(producto, cantidad))
        _estadoCarrito.value = estadoCarrito.value.copy(productos = newProductos)
    }


    /**
     * Función para agregar una orden a la base de datos
     * @author Iker Fuentes
     * @author Santiago Chevez
     * @param id Id de la orden
     */
    fun addOrder(id: Int) {
        var newList: MutableList<Pair<Int, Int>> = mutableListOf()
        for (producto in estadoCarrito.value.productos) {
            newList.add(Pair(producto.first.id, producto.second))
        }

        viewModelScope.launch {
            try {
                var orden: Order = Order(modeloR.createDataInfo(newList), estadoUsuario.value.id)
                val response = modeloR.addOrderWithToken(orden, estadoUsuario.value.key)
            }
            catch (e: Exception) {
                println(e)
            }
        }
    }

    /**
     * Función para crear un usuario en la base de datos
     * @author Iker Fuentes
     * @author Santiago Chevez
     * @param nombre Nombre del usuario
     * @param apellido_paterno Apellido paterno del usuario
     * @param apellido_materno Apellido materno del usuario
     * @param fecha_nacimiento Fecha de nacimiento del usuario
     * @param correo Correo del usuario
     * @param password Contraseña del usuario
     * @param terminos Saber si el usuario acepta los términos y condiciones
     * @param publicidad Saber si el usuario desea recibir publicidad
     * @param telefono Telefono del usuario
     */
    fun signUp(nombre: String, apellido_paterno: String, apellido_materno: String, fecha_nacimiento: String, correo: String, password: String,terminos :Boolean, publicidad : Boolean,telefono: String) {
        val terminos : Int = if (terminos) 1 else 0
        val publicidad : Int = if (publicidad) 1 else 0
        println(terminos)
        println(publicidad)
        val user : Usuario = Usuario(
            nombre,
            apellido_paterno,
            apellido_materno,
            fecha_nacimiento,
            correo,
            //modeloR.hash(password),
            password,
            terminos,
            publicidad,
            telefono
        )
        viewModelScope.launch {
            try {
                val response = modeloR.signUp(user)
                if (response.result=="error"){
                    throw Exception("Could not create User")
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    /**
     * Función para iniciar sesión en la aplicación autenticandonos con la base de datos
     * @author Iker Fuentes
     * @param email Correo del usuario
     * @param password Contraseña del usuario
     */
    fun login(email : String, password: String){
        viewModelScope.launch {
            try {
                val response = modeloR.getJWTKey(email, password)

                if (response.message != "Success generating token") {
                    throw Exception(response.message)
                }
                val userData = modeloR.getUserData(response.data)

                setUserKey(response.data)
                setUserId(response.id)
                setNombreUsuario(userData.nombre)
                setApellidoMaternoUsuario(userData.apellido_materno)
                setApellidoPaternoUsuario(userData.apellido_paterno)
                setDireccionUsuario(userData.direccion)
                setTelefonoUsuario(userData.telefono)
                getProductos()

            } catch (e: Exception) {
                _estadoErrors.value = _estadoErrors.value.copy(errorLogin = true)
            }
        }
    }

    /**
     * Sirve para cambiar la dirección del usuario en la aplicación (NO en la base de datos)
     * @author Santiago Chevez
     */
    fun changeAddress(){
        _estadoUsuario.value = _estadoUsuario.value.copy(direccion = _estadoCopiaDireccion.value)
    }

    /**
     * Función para abrir una página web en el navegador del usuario
     * @author Santiago Chevez
     * @param url La url de la página web a abrir
     * @param context El contexto de la aplicación
     * @param startActivity La función para iniciar una actividad (Abrir la página)
     */
    fun openWebPage(url: String, context: Context, startActivity: (Intent) -> Unit) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Manejar la excepción, por ejemplo, mostrando un mensaje al usuario
        }
    }

    /**
     * Función para obtener los foros de la base de datos
     * @author Iker Fuentes
     */
    fun getForos(){
        viewModelScope.launch {
            _estadoForo.value = modeloR.getForo(getEstadoUsuario().key).foros
        }
    }

    /**
     * Función para obtener los comentarios de un foro
     * @author Iker Fuentes
     * @param id Id del foro
     */
    fun getComments(id:Int){
        viewModelScope.launch {
            _estadoComentarios.value = modeloR.getComments(getEstadoUsuario().key,id).comentarios
        }
    }

    /**
     * Función para enviar un correo
     * @author Santiago Chevez
     * @param correo Correo electrónico
     * @param context Contexto de la aplicación
     */
    fun enviarCorreo(correo:String,context:Context) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$correo")
        }
        context.startActivity(intent)
    }

    /**
     * Función para llamar a un teléfono
     * @author Santiago Chevez
     * @param telefono Número de teléfono
     * @param context Contexto de la aplicación
     */
    fun llamada(telefono: String, context: Context) {
        val intent = Intent(Intent.ACTION_DIAL,Uri.parse("tel:$telefono"))
        context.startActivity(intent)
    }

    /**
     * Función para llevar al mapa a una ubicación
     * @author Santiago Chevez
     * @param ubicacion la direccion
     * @param context Contexto de la aplicación
     */
    fun ubicacion(ubicacion: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$ubicacion"))
        context.startActivity(intent)
    }

    /**
     * Para cambiar el correo en el estado usuario
     * @author Santiago Chevez
     * @param correo Correo del usuario
     */
    fun setCorreoUsuario(correo: String) {
        _estadoUsuario.value = _estadoUsuario.value.copy(correo = correo)
    }

    /**
     * Para cambiar la contraseña en el estado usuario
     * @author Santiago Chevez
     * @param contrasena Contraseña del usuario
     */
    fun setContrasenaUsuario(contrasena: String) {
        _estadoUsuario.value = _estadoUsuario.value.copy(password = contrasena)
    }

    /**
     * Para cambiar el nombre en el estado usuario
     * @author Santiago Chevez
     * @param nombre Nombre del usuario
     */
    fun setNombreUsuario(nombre: String) {
        _estadoUsuario.value = _estadoUsuario.value.copy(nombre = nombre)
    }

    /**
     * Para cambiar la Apellido paterno en el estado usuario
     * @author Santiago Chevez
     * @param apellido Contraseña del usuario
     */
    fun setApellidoPaternoUsuario(apellido: String) {
        _estadoUsuario.value = _estadoUsuario.value.copy(apellido_paterno = apellido)
    }

    /**
     * Para cambiar la Apellido Materno en el estado usuario
     * @author Santiago Chevez
     * @param apellido Contraseña del usuario
     */
    fun setApellidoMaternoUsuario(apellido: String) {
        _estadoUsuario.value = _estadoUsuario.value.copy(apellido_materno = apellido)
    }

    /**
     * Para cambiar la dirección en el estado usuario
     * @author Santiago Chevez
     * @param apellido Contraseña del usuario
     */
    fun setDireccionUsuario(direccion : Direccion){
        _estadoUsuario.value = _estadoUsuario.value.copy(direccion = direccion)
    }

    /**
     * Para cambiar la confirmación de contraseña en el estado usuario
     * @author Santiago Chevez
     * @param contrasena Contraseña del usuario
     */
    fun setConfirmacionContrasenaUsuario(contrasena: String) {
        _estadoUsuario.value = _estadoUsuario.value.copy(confirmacion_password = contrasena)
    }

    /**
     * Para cambiar el estado de la contraseña en el estado usuario
     * @author Santiago Chevez
     */
    fun checkPasswordErrors(){
        _estadoErrors.value = _estadoErrors.value.copy(errorContrasenas=_estadoUsuario.value.password!=_estadoUsuario.value.confirmacion_password)
    }

    /**
     * Para cambiar la clave en el estado usuario
     * @author Iker Fuentes
     * @param key Key del usuario
     */
    fun setUserKey(key : String){
        _estadoUsuario.value = _estadoUsuario.value.copy(key = key)
    }

    /**
     * Para cambiar el id en el estado usuario
     * @author Iker Fuentes
     * @param id Id del usuario
     */
    fun setUserId(id : Int){
        _estadoUsuario.value = _estadoUsuario.value.copy(id = id)
    }

    /**
     * Para cambiar el errorLogin en el estado errors
     * @author Santiago Chevez
     * @param error Error
     */
    fun setErrorLogin(error: Boolean){
        _estadoErrors.value = _estadoErrors.value.copy(errorLogin = error)
    }

    /**
     * Para cambiar el loading en el estado usuario
     * @author Santiago Chevez
     * @param loading Loading
     */
    fun setLoading(loading: Boolean){
        _estadoUsuario.value = _estadoUsuario.value.copy(loading = loading)
    }

    /**
     * Para cambiar el telefono en el estado usuario
     * @author Santiago Chevez
     * @param telefono Telefono del usuario
     */
    fun setTelefonoUsuario(telefono: String) {
        _estadoUsuario.value = _estadoUsuario.value.copy(telefono = telefono)
    }

    /**
     * Para cambiar el intent para saber si intento logearse en el estado usuario
     * @author Santiago Chevez
     * @param intent Intent
     */
    fun setIntent(intent: Boolean) {
        _estadoUsuario.value = _estadoUsuario.value.copy(intent = intent)
    }

    /**
     * Para cambiar la fecha de nacimiento en el estado usuario
     * @author Cesar Flores
     * @author Andrés Cabrera
     * @param day Día de nacimiento
     * @param month Mes de nacimiento
     * @param year Año de nacimiento
     */
    fun setFecha(day: Int, month: Int, year: Int) {
        _estadoUsuario.value = _estadoUsuario.value.copy(ano_nacimiento = year, mes_nacimiento = month, día_nacimiento = day)

        val months = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
        val fecha = "%02d-%s-%04d".format(day, months[month - 1], year)
        setEstadoFecha(fecha)
    }

    /**
     * Para obtener la fecha de nacimiento del usuario
     * @author Cesar Flores
     * @author Andrés Cabrera
     */
    fun getFecha(): Triple<Int, Int, Int> {
        return Triple(_estadoUsuario.value.ano_nacimiento, _estadoUsuario.value.mes_nacimiento, _estadoUsuario.value.día_nacimiento)
    }

    /**
     * Para cambiar el errorType en el estado errors
     * @author Santiago Chevez
     * @param b valor para el error
     */
    fun setErrorType(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorType = b)
    }

    /**
     * Para cambiar el errorCell en el estado errors
     * @author Santiago Chevez
     * @param b valor para el error
     */
    fun setErrorCell(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorCell = b)
    }

    /**
     * Para cambiar el errorCorreo en el estado errors
     * @author Santiago Chevez
     * @param b valor para el error
     */
    fun setErrorCorreo(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorCorreo = b)
    }

    /**
     * Para cambiar el errorLengthPassword en el estado errors
     * @author Santiago Chevez
     * @param b valor para el error
     */
    fun setErrorLengthPassword(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorLengthPassword = b)
    }

    /**
     * Para cambiar el errorDireccion en el estado errors
     * @author Santiago Chevez
     * @param b valor para el error
     */
    fun copiarDireccion() {
        _estadoCopiaDireccion.value = _estadoUsuario.value.direccion
        println(_estadoCopiaDireccion.value.id_usuario)
    }

    /**
     * Para cambiar la calle en el estado copiaDireccion
     * @author Santiago Chevez
     * @param calle Calle
     */
    fun setCalle(calle: String) {
        _estadoCopiaDireccion.value = _estadoCopiaDireccion.value.copy(calle = calle)
    }

    /**
     * Para cambiar la colonia en el estado copiaDireccion
     * @author Santiago Chevez
     * @param colonia Colonia
     */
    fun setColonia(colonia: String) {
        _estadoCopiaDireccion.value = _estadoCopiaDireccion.value.copy(colonia = colonia)
    }

    /**
     * Para cambiar el municipio en el estado copiaDireccion
     * @author Santiago Chevez
     * @param municipio Municipio
     */
    fun setMunicipio(municipio: String) {
        _estadoCopiaDireccion.value = _estadoCopiaDireccion.value.copy(municipio = municipio)
    }

    /**
     * Para cambiar el estado en el estado copiaDireccion
     * @author Santiago Chevez
     * @param estado Estado
     */
    fun setEstado(estado:String) {
        _estadoCopiaDireccion.value = _estadoCopiaDireccion.value.copy(estado = estado)
    }

    /**
     * Para cambiar el cp en el estado copiaDireccion
     * @author Santiago Chevez
     * @param cp CP
     */
    fun setCp(cp: String) {
        _estadoCopiaDireccion.value = _estadoCopiaDireccion.value.copy(cp = cp)
    }

    /**
     * Para cambiar el numero exterior en el estado copiaDireccion
     * @author Santiago Chevez
     * @param num Numero exterior
     */
    fun setNumeroExt(num:String){
        _estadoCopiaDireccion.value = _estadoCopiaDireccion.value.copy(numero_exterior = num)
    }

    /**
     * Para cambiar el numero interior en el estado copiaDireccion
     * @author Santiago Chevez
     * @param num Numero interior
     */
    fun setNumeroInt(num:String){
        _estadoCopiaDireccion.value = _estadoCopiaDireccion.value.copy(numero_int = num)
    }

    /**
     * Para cambiar el valor del estado expanded
     * @author Santiago Chevez
     * @param expanded Valor del estado
     */
    fun setExpanded(expanded: Boolean) {
        _estadoExpanded.value = expanded
    }

    /**
     * Para obtener el estado del usuario
     * @author Santiago Chevez
     */
    fun getEstadoUsuario() : EstadoUsuario{
        return _estadoUsuario.value
    }

    /**
     * Para cambiar el codigo para recuperar la contraseña
     * @author Cesar Flores
     * @param codigo El nuevo código
     */
    fun setCodigoUsuario(codigo: String){
        _estadoUsuario.value = _estadoUsuario.value.copy(codigo = codigo)
    }

    /**
     * Para añadir un producto al Carrito
     * @author Andrés Cabrera
     * @author Santiago Chevez
     */
    fun setEstadoAnadirCarrito(producto: EstadoProducto){
        if (_estadoanadirCarrito.value.first != producto){
            _estadoanadirCarrito.value = Pair(producto,1)
        }
    }

    /**
     * Para sumar o restar un producto antes de añadir al carrito
     * @author Santiago Chevez
     * @author Andrés Cabrera
     * @param sign si se suma 1 o se resta -1
     * @param producto el producto a sumar o restar
     */
    fun sumarorestarproducto(sign: Int, producto: EstadoProducto){
        if (sign == 1){
            if(_estadoanadirCarrito.value.second == producto.cantidad){
                return
            }
            _estadoanadirCarrito.value = _estadoanadirCarrito.value.copy(first = producto, second = _estadoanadirCarrito.value.second+1)
        }
        else{
            if (_estadoanadirCarrito.value.second == 1){
                return
            }
            _estadoanadirCarrito.value = _estadoanadirCarrito.value.copy(first = producto,second = _estadoanadirCarrito.value.second-1)
        }
    }

    /**
     * Para saber si el usuario esta haciendo el proceso de recuperar contraseña
     * @author Andrés Cabrera
     * @param b el valor que tiene que tomar
     */
    fun setContrasenaPerdida(b: Boolean) {
        _contrasenaPerdida.value = b
    }

    /**
     * Esta función pide el cambio de contraseña
     * @author Cesar Flores
     * @author Andrés Cabrera
     * @param email Correo del usuario
     */
    fun recuperarContrasena(email: String){
        viewModelScope.launch {
            try {
                val response = modeloR.getRecoveryPasswordToken(email)
                if (response.result == "error") {
                    throw Exception("Could not recover password")
                }
                println(response.message)
                //cambiar estado loading a false
                _estadoUsuario.value = _estadoUsuario.value.copy(loading = false)
                //cambiar estado contraseña perdida a true
                _contrasenaPerdida.value = true
            }
            catch (e: Exception) {
                println(e)
                _estadoErrors.value = _estadoErrors.value.copy(errorLogin = true)
            }
        }
    }

    /**
     * Función para llevar a cabo el cambio de contraseña
     * @author Cesar Flores
     * @param code Código de recuperación
     * @param email Correo del usuario
     * @param password Nueva contraseña
     */
    fun changePassword(code: Int, email: String, password: String){
        viewModelScope.launch {
            try {
                val response = modeloR.changePassword(code, email, password)
                if (response.result == "error") {
                    throw Exception("Could not change password")
                }
                _estadoUsuario.value = _estadoUsuario.value.copy(loading = false)
                setCambioContrasena(true)
            }
            catch (e: Exception) {
                println(e)
                _estadoErrors.value = _estadoErrors.value.copy(errorLogin = true)//cambio de contraseña
            }
        }
    }

    /**
     * Para cambiar el valor del estado cambioContrasena
     * @author Cesar Flores
     * @param b el valor que tiene que tomar
     */
    fun setCambioContrasena(b: Boolean) {
        _cambioContrasena.value = b
    }

    /**
     * Para cambiar el valor del estado showDatePicker
     * @author Cesar Flores
     * @param b el valor que tiene que tomar
     */
    fun setErrorCodigo(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorCodigo = b)
    }

    /**
     * Para cambiar el valor del Error Calle en el estado Errors
     * @author Santiago Chevez
     * @param b el valor que tiene que tomar
     */
    fun setErrorCalle(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorCalle = b)
    }

    /**
     * Para cambiar el valor del Error Municipio en el estado Errors
     * @author Santiago Chevez
     * @param b el valor que tiene que tomar
     */
    fun setErrorMunicipio(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorMunicipio = b)
    }

    /**
     * Para cambiar el valor del Error Colonia en el estado Errors
     * @author Santiago Chevez
     * @param b el valor que tiene que tomar
     */
    fun setErrorColonia(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorColonia = b)
    }

    /**
     * Para cambiar el valor del Error Cp en el estado Errors
     * @author Santiago Chevez
     * @param b el valor que tiene que tomar
     */
    fun setErrorCp(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorCp = b)
    }

    /**
     * Para cambiar el valor del Error Numero Exterior en el estado Errors
     * @author Santiago Chevez
     * @param b el valor que tiene que tomar
     */
    fun setErrorNumeroExt(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorNumeroExt = b)
    }

    /**
     * Para cambiar el valor del Error Numero Interior en el estado Errors
     * @author Santiago Chevez
     * @param b el valor que tiene que tomar
     */
    fun setErrorNumeroInt(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorNumeroInt = b)
    }

    /**
     * Para cambiar el valor del Error Estado en el estado Errors
     * @author Santiago Chevez
     * @param b el valor que tiene que tomar
     */
    fun setErrorEstado(b: Boolean) {
        _estadoErrors.value = _estadoErrors.value.copy(errorEstado = b)
    }

    /**
     * Para cambiar el valor del total en el estado carrito
     * @author Alan Vega
     * @author Santiago Chevez
     * @param total el valor que tiene que tomar
     */
    fun setTotalCarrito(total: Float){
        _estadoCarrito.value.total = total
    }

    /**
     * Para cambiar el valor del estado fecha
     * @author Cesar Flores
     * @param fecha el valor que tiene que tomar
     */
    fun setEstadoFecha(fecha: String) {
        _estadoFecha.value = fecha
    }

    /**
     * Para cambiar el valor del estado showDatePicker
     * @author Cesar Flores
     * @param show el valor que tiene que tomar
     */
    fun setShowDatePicker(show: Boolean) {
        _showDatePicker.value = show
    }

}