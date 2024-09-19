package mx.tec.pruebabrillamostodas3.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.tec.pruebabrillamostodas3.viewmodel.BTVM

@Composable
fun MenuDirecciones(btVM: BTVM, navController: NavHostController){
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .verticalScroll(scrollState),

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .padding(10.dp)
                    .size(80.dp)
                    .fillMaxWidth(),

                )
            Titulo(
                titulo = "Dirección",
                color = MaterialTheme.colorScheme.primaryContainer,
                fontSize = 50
            )
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(bottom = 50.dp)

            ) {
                Spacer(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                )
                Subtitulo(
                    "Direccion Registrada",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onTertiary
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onTertiary,

                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                        .border(6.dp, MaterialTheme.colorScheme.tertiary),
                    contentAlignment = Alignment.Center


                        ) {
                            Row {
                                Text(
                                    "Vazco de Quiroga 2134, Central, Santa Fe, CP 5867",
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .padding(vertical = 20.dp)
                                        .weight(5f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                ElevatedButton(
                                    onClick = { navController.navigate(Pantallas.RUTA_EDITAR_DIRECCION) },
                                    modifier = Modifier
                                        .padding(vertical = 20.dp)
                                        .padding(horizontal = 10.dp)
                                        .height(50.dp)
                                        .width(110.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                        contentColor = MaterialTheme.colorScheme.onTertiary
                                    )
                                ) {
                                    Text(
                                        "Editar",
                                        modifier = Modifier.height(20.dp),
                                        color = MaterialTheme.colorScheme.onTertiary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(bottom = 16.dp))
            }
        }
