package mx.tec.pruebabrillamostodas3.model

/**
 * @author Carlos Iker Fuentes Reyes
 */

import com.google.gson.Gson
import io.ktor.client.statement.HttpResponse
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest


class ModelConnectionR {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://apex.oracle.com/pls/apex/todasbrillamos/connection/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val service by lazy {
        retrofit.create(TodasBrillamosAPI::class.java)
    }

    //Product functions
    suspend fun getProductsWithToken(userToken: String): Pair<Int, List<Producto>> {
        val response: ListaProducto = service.getProductos(userToken)
        val return_value: Pair<Int, List<Producto>> =
            Pair(response.cantidad, response.productos)
        return return_value
    }
    suspend fun getProductImage(imageId: Int): ByteArray {
        val response: ResponseBody = service.getProductImage(imageId.toString())
        return response.bytes()
    }

    //User Functions
    suspend fun signUp(user: Usuario): ResponseFormat {
        val response: ResponseFormat = service.addUser(user)
        return response
    }

    suspend fun getJWTKey(user: String, password: String): JWT_KEY {
        val response: JWT_KEY = service.getJWTKey(user, password)
        return response
    }
    suspend fun addAddress(user_token: String, address: Direccion): ResponseFormat {
        val response: ResponseFormat = service.addAddressWithToken(address, user_token)
        return response
    }
    suspend fun getUserData(user_token: String) : DataUsuario{
        val response : DataUsuario = service.getUserDataWithToken(user_token)
        return response
    }
    //Password Recovery Functions
    fun hash(password: String): String {
        val bytes = password.toByteArray()

        val md = MessageDigest.getInstance("SHA-256")
        println(md)
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
    suspend fun getRecoveryPasswordToken(email: String): ResponseFormat {
        val response: ResponseFormat = service.getRecoveryPasswordToken(email)
        return response
    }
    suspend fun changePassword(code: Int, email: String, password: String): ResponseFormat {
        val response: ResponseFormat = service.changePassword(ChangePassword(code, email, password))
        return response
    }



    //Order Functions
    fun createDataInfo(productos: MutableList<Pair<Int, Int>>): String {
        var id_productos: String = ""
        var cantidad_producto: String = ""
        for (producto in productos) {
            id_productos += "${producto.first},"
            cantidad_producto += "${producto.second},"
        }
        val result = id_productos.dropLast(1) + ";" + cantidad_producto.dropLast(1)
        return result
    }

    suspend fun addOrderWithToken(order: Order, userToken: String): ResponseFormat {
        val response : ResponseFormat = service.addOrderWithToken(order, userToken)
        return response
    }
    suspend fun getOrderInfo(userToken: String) : HashMap<Int,List<Orderproducts>>{

        val response : Orders = service.getOrderInfoWithToken(userToken)
        val userOrders : HashMap<Int,List<Orderproducts>> = hashMapOf()
        for (order in response.ordenes){
            userOrders[order.id_orden] = order.orden
        }
        return userOrders
    }
}

suspend fun main(){
    val modelConnection = ModelConnectionR()
    println(modelConnection.hash("AAAAA"))
    val lista : List<Producto> = modelConnection.getProductsWithToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJPUkRTIiwic3ViIjoiaWtlckBnbWFpbC5jb207MTIzNCIsImlhdCI6MTcyNjQ5ODg3NiwiZXhwIjoxNzI2NDk5MTc2fQ.KCwualrayru-DSqHG-Zi5IpXc3TPx8LDb1EvKOtWymA").second
    println(lista)
//val p :MutableList<Pair<Int,Int>> = mutableListOf()
    /*for (i in lista){
        p.add(Pair(i.id,1))
    }*/
    //val dInfo = modelConnection.createDataInfo(p)
    //val order = Order(dInfo,201)
    //val us : Usuario = Usuario("Iker","Fuentes","Reyes","16-DEC-2002","iker23537676565877654@gmail.com","1234",0,1,"5532532512")
    //println(modelConnection.addOrderWithToken(order,modelConnection.getJWTKey(us.email,us.password).data))
    //println(modelConnection.addAddress(modelConnection.getJWTKey(us.email,us.password).data,Direccion("Convento de SantaMaría","Jardines de ","Tlane","EdoMex","54050","12","12",201)))
    // val us : Usuario = Usuario("Iker","Fuentes","Reyes","16-DEC-2002","iker2365@gmail.com","1234",0)
    //println(modelConnection.getJWTKey(us.email,us.password))
    //println(modelConnection.signUp(us))
    //println(modelConnection.getUserData(modelConnection.getJWTKey("iker@gmail.com","1234").data))
    //println(modelConnection.getRecoveryPasswordToken("iker.fuentesreyes@gmail.com"))
    //println(modelConnection.changePassword(109076427,"iker.fuentesreyes@gmail.com","777"))
    println(modelConnection.getOrderInfo("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJPUkRTIiwic3ViIjoiaWtlci5mdWVudGVzcmV5ZXNAZ21haWwuY29tOzA0MDAiLCJpYXQiOjE3MjcyODA2NDQsImV4cCI6MTcyNzI4MDk0NH0.xQ3muy9SPCltMit4muOIcEPRrBe3rOjYMACnuscBLO4"))
}