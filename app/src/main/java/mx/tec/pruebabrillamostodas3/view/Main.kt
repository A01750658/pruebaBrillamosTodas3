package mx.tec.pruebabrillamostodas3.view

import android.content.Context
import android.net.Uri
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.tec.pruebabrillamostodas3.ui.theme.PruebaBrillamosTodas3Theme
import mx.tec.pruebabrillamostodas3.viewmodel.BTVM
import mx.tec.pruebabrillamostodas3.viewmodel.PaymentsViewModel

/**
 * @author Alan Vega
 */

@Composable
fun Main(btVM: BTVM, paymentsVM: PaymentsViewModel, flag: Boolean, savedDeepLinkUri: Uri?, modifier: Modifier = Modifier){
    val navController = rememberNavController()
    PruebaBrillamosTodas3Theme{
        Scaffold(topBar = {AppTopBar(navController)},
            bottomBar = {AppBottomBar(navController)}){
                innerPadding ->
            AppNavHost(
                btVM,
                paymentsVM,
                navController,
                flag,
                savedDeepLinkUri,
                modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavHostController) {
    if(navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_LOGIN && navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_SIGNUP && navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_APP_HOME
        && navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_PERFIL && navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_REDES && navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_FOROS
        && navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_TIENDA){
        if(navController.currentBackStackEntryAsState().value?.destination?.route == Pantallas.RUTA_CARRITO){
            TopAppBar(
                title = {
                    Text(text = "Continuar comprando",
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }){
                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onTertiary)
                    }
                }
            )
        }else{
            TopAppBar(
                title = {
                    Text(
                        text = "",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    if(navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_LOGIN
        && navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_SIGNUP
        && navController.currentBackStackEntryAsState().value?.destination?.route != Pantallas.RUTA_AVISO){
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onTertiary,

            ){
            val pilaNavegacion by navController.currentBackStackEntryAsState()
            val pantallaActual = pilaNavegacion?.destination

            Pantallas.listaPantallas.forEach{pantalla ->
                NavigationBarItem(
                    selected = pantalla.ruta == pantallaActual?.route,
                    onClick = {
                        navController.navigate(pantalla.ruta){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {Text(text = pantalla.etiqueta,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = 13.sp)},
                    icon = {Icon(
                        imageVector = pantalla.icono,
                        contentDescription = pantalla.etiqueta,
                        tint = MaterialTheme.colorScheme.onTertiary)},
                    alwaysShowLabel = true,
                )
            }
        }
    }
}

@Composable
fun AppNavHost(btVM: BTVM, paymentsVM: PaymentsViewModel,navController: NavHostController, flag: Boolean, savedDeepLinkUri: Uri?,modifier: Modifier = Modifier) {

    NavHost(navController = navController,
        startDestination = if (!flag) Pantallas.RUTA_LOGIN else Pantallas.RUTA_CARRITO,
        modifier = modifier,){
        composable(Pantallas.RUTA_APP_HOME){
            Home(btVM, navController)
        }
        composable(Pantallas.RUTA_REDES){
            Redes(btVM)
        }
        composable(Pantallas.RUTA_FOROS){
            Foros()
        }
        composable(Pantallas.RUTA_PERFIL){
            Perfil(btVM, navController)
        }
        composable(Pantallas.RUTA_TIENDA){
            Tienda(btVM, modifier, navController)
        }
        composable(Pantallas.RUTA_SIGNUP){
            SignUp(btVM, navController)
        }
        composable(Pantallas.RUTA_LOGIN){
            LogIn(btVM, navController, paymentsVM)
        }
        composable(Pantallas.RUTA_INFO){
            Info(btVM)
        }
        composable(Pantallas.RUTA_CONTACTO){
            Contacto(btVM)
        }
        composable(Pantallas.RUTA_AVISO){
            AvisoyLeyenda()
        }
        composable(Pantallas.RUTA_CARRITO){
            Carrito(btVM, paymentsVM, savedDeepLinkUri)
        }
        composable(Pantallas.RUTA_EDITAR_DIRECCION) {
            EditarDireccion(btVM,navController)
        }
    }
}