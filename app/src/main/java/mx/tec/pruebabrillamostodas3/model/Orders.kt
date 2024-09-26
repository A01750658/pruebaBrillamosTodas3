package mx.tec.pruebabrillamostodas3.model
/**
 * @author Carlos Iker Fuentes Reyes
 *  Clase de datos que se utiliza como formato para la
 * lectura del JSON en la clase abstracta Todasbrillamos API
 *
{
 "ordenes" : [{
        "id_orden": 182,
            "orden": [
            {
            "nombre": "Toalla Femenina Reutilizable Regular Violeta",
            "cantidad": 4,
            "Total": 0
            },
            {
            "nombre": "Reutilizable Nocturna Margarita",
            "cantidad": 100,
            "Total": 0
            }
            ]
        }]
 }
 */
data class Orders(
    val ordenes: List<OrderInfo>
)
