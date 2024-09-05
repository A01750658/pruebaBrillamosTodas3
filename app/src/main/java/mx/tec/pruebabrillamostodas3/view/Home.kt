package mx.tec.pruebabrillamostodas3.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.tec.pruebabrillamostodas3.viewmodel.BTVM

/**
 * @author Santiago Chevez
 * @author Alan Vega
 */

@Composable
fun Home(btVM: BTVM, navController: NavHostController){
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .verticalScroll(scrollState)
    )
    {
      Column(modifier = Modifier.padding(horizontal = 16.dp)){
          Titulo("ZAZIL",color= MaterialTheme.colorScheme.primaryContainer, fontSize = 90)
          Subtitulo("Cambia el mundo con un solo gesto.")
          HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primaryContainer)
          Column(
              Modifier
                  .padding(vertical = 20.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .border(
                      color = MaterialTheme.colorScheme.tertiary,
                      width = 3.dp,
                      shape = MaterialTheme.shapes.medium
                  )
                  .background(color = MaterialTheme.colorScheme.onTertiary)){
              Text(text = "¡Una menstruación sostenible!",
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.tertiary,
                  modifier = Modifier
                      .padding(10.dp)
                      .fillMaxWidth())
              Text(text = "Encuentra la mejor información disponible para ti, acerca de la menstrua" +
                      "ción, sus ciclos y más con nuestro equipo de investigación.\n" +
                      "Tampoco olvides visitar los productos que tenemos disponibles para ti.",
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.bodySmall,
                  modifier = Modifier
                      .padding(start = 10.dp, bottom = 10.dp, end = 10.dp)
                      .fillMaxWidth())
          }
          BotonTextandIcon(text = "Ver Catálogo", icon = Icons.Default.ShoppingCart, onClick = {navController.navigate(Pantallas.RUTA_TIENDA)})
          BotonTextandIcon(text = "Conoce más información",icon = Icons.Default.Info, onClick = {navController.navigate(Pantallas.RUTA_INFO)},color = MaterialTheme.colorScheme.primaryContainer, fontSize = 20)
          BotonTextandIcon(text = "Contactanos", icon = Icons.Default.Person, onClick = { navController.navigate(Pantallas.RUTA_CONTACTO)}, color = MaterialTheme.colorScheme.secondaryContainer)
      }
    }
}