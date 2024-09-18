package mx.tec.pruebabrillamostodas3.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import mx.tec.pruebabrillamostodas3.viewmodel.BTVM
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignUp(btVM: BTVM, navController: NavHostController) {
    val scrollState = rememberScrollState()
    val showDatePicker by btVM.showDatePicker.observeAsState(false)
    val estado = btVM.estadoUsuario.collectAsState()
    var valorCorreo by rememberSaveable { mutableStateOf(estado.value.correo) }
    var valorPassword by rememberSaveable { mutableStateOf(estado.value.password) }
    var valorNombre by rememberSaveable { mutableStateOf(estado.value.nombre) }
    var valorApellidoPaterno by rememberSaveable { mutableStateOf(estado.value.apellido_paterno) }
    var valorApellidoMaterno by rememberSaveable { mutableStateOf(estado.value.apellido_materno) }
    var valorConfirmacionPassword by rememberSaveable { mutableStateOf(estado.value.confirmacion_password) }
    var valortelefono by rememberSaveable { mutableStateOf(estado.value.telefono) }
    var valorAvisos by rememberSaveable { mutableStateOf(estado.value.aviso) }
    var valorMarketing by rememberSaveable { mutableStateOf(estado.value.marketing) }
    val estadoErrors = btVM.estadoErrors.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .verticalScroll(scrollState),

        ){
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
        ){
            Titulo(titulo ="SIGN UP", modifier = Modifier.padding(bottom = 2.dp), color = MaterialTheme.colorScheme.onTertiary)
            Spacer(modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.onTertiary)
                .padding(bottom = 2.dp)
                .fillMaxWidth()
            )
            Spacer(modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
            )
            Etiqueta("Nombre*", Modifier.padding(bottom = 3.dp))
            Inputtexto(estado.value.nombre,{
                nuevoTexto ->
                btVM.setIntent(false)
                if (nuevoTexto.length > 20 || nuevoTexto.any {it.isDigit()}) {
                    btVM.setErrorType(true)
                } else{
                    btVM.setErrorType(false)
                }
                valorNombre = nuevoTexto
                btVM.setNombreUsuario(valorNombre)
            })
            Etiqueta("Apellido Paterno*", Modifier.padding(bottom = 3.dp))
            Inputtexto(estado.value.apellido_paterno,
                {
                    nuevoTexto ->
                    btVM.setIntent(false)
                    if (nuevoTexto.length > 20 || nuevoTexto.any {it.isDigit()}) {
                        btVM.setErrorType(true)
                    } else{
                        btVM.setErrorType(false)
                    }
                    valorApellidoPaterno = nuevoTexto
                    btVM.setApellidoPaternoUsuario(valorApellidoPaterno)
                })
            Etiqueta("Apellido Materno*", Modifier.padding(bottom = 3.dp))
            Inputtexto(estado.value.apellido_materno,
                {
                    nuevoTexto ->
                    btVM.setIntent(false)
                    if (nuevoTexto.length > 20 || nuevoTexto.any {it.isDigit()}) {
                        btVM.setErrorType(true)
                    } else{
                        btVM.setErrorType(false)
                    }
                    valorApellidoMaterno = nuevoTexto
                    btVM.setApellidoMaternoUsuario(valorApellidoMaterno)
                })
            if (estadoErrors.value.errorType){
                Etiqueta("Todos los campos deben ser llenados. Solo puedes usar letras y no se puede superar los 20 caracteres.", Modifier.padding(bottom = 16.dp), color= MaterialTheme.colorScheme.onPrimary)
            }
            Etiqueta("Fecha de Nacimiento", Modifier.padding(bottom = 3.dp))
            //DatePicker(state = DatePickerState(locale = CalendarLocale.GERMAN))

            TextButton(onClick = { btVM.setShowDatePicker(true) },
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            ){
                Text(
                    text = "Seleccionar Fecha",
                    textAlign = TextAlign.Center,

                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }

            if (showDatePicker) {
                Dialog(onDismissRequest = { btVM.setShowDatePicker(false) }) {
                    DatePickerScreen(
                        modifier = Modifier
                            .padding(bottom = 3.dp, start = 16.dp)
                            .background(MaterialTheme.colorScheme.onTertiary)
                    )
                }
            }

            //DatePickerScreen(modifier = Modifier.padding(bottom = 3.dp,start = 16.dp).background(MaterialTheme.colorScheme.onTertiary))
            Etiqueta("Telefono*", Modifier.padding(bottom = 3.dp))
            Inputtexto(estado.value.telefono,
                {
                        nuevoTexto ->
                    if (nuevoTexto.length != 10 || nuevoTexto.any {it.isLetter()}) {
                        btVM.setErrorCell(true)
                    } else{
                        btVM.setErrorCell(false)
                    }
                    btVM.setIntent(false)
                    valortelefono = nuevoTexto
                    btVM.setTelefonoUsuario(valortelefono)
                },
                keyBoardType = KeyboardType.Number)
            if (estadoErrors.value.errorCell){
                Etiqueta("El celular debe de ser de 10 dígitos", Modifier.padding(bottom = 16.dp), color= MaterialTheme.colorScheme.onPrimary)
            }
            Etiqueta("Correo Electrónico*", Modifier.padding(bottom = 3.dp))
            Inputtexto(estado.value.correo,
                {
                    nuevoTexto ->
                    if (nuevoTexto.length > 50 || !nuevoTexto.contains("@") || !nuevoTexto.contains(".")) {
                        btVM.setErrorCorreo(true)
                    } else{
                        btVM.setErrorCorreo(false)
                    }
                    btVM.setIntent(false)
                    valorCorreo = nuevoTexto
                    btVM.setCorreoUsuario(valorCorreo)
                },
                keyBoardType = KeyboardType.Email)
            if (estadoErrors.value.errorCorreo){
                Etiqueta("El correo debe de tener un formato válido", Modifier.padding(bottom = 16.dp), color= MaterialTheme.colorScheme.onPrimary)
            }
            Etiqueta("Contraseña*", Modifier.padding(bottom = 3.dp))
            InputContraseña(estado.value.password,
                { nuevoTexto ->
                    if (nuevoTexto.length < 8) {
                        btVM.setErrorLengthPassword(true)
                    } else{
                        btVM.setErrorLengthPassword(false)
                    }
                    btVM.setIntent(false)
                    valorPassword = nuevoTexto
                    btVM.setContrasenaUsuario(valorPassword)
                    btVM.checkPasswordErrors()})
            if (estadoErrors.value.errorLengthPassword){
                Etiqueta("La contraseña debe tener al menos 8 caracteres",modifier = Modifier.padding(bottom = 16.dp) ,color= MaterialTheme.colorScheme.onPrimary)
            }
            Etiqueta("Confirmar Contraseña*", Modifier.padding(bottom = 3.dp))
            InputContraseña(estado.value.confirmacion_password,
                { nuevoTexto ->
                    btVM.setIntent(false)
                    valorConfirmacionPassword = nuevoTexto
                    btVM.setConfirmacionContrasenaUsuario(valorConfirmacionPassword)
                    btVM.checkPasswordErrors()})
            if (estadoErrors.value.errorContrasenas){
                Etiqueta("Las contraseñas no coinciden", modifier = Modifier.padding(bottom = 16.dp), color = MaterialTheme.colorScheme.onPrimary)
            }
            Row {
                TextButton(onClick = { navController.navigate(Pantallas.RUTA_AVISO) }, modifier = Modifier.weight(5f)) {
                    Text(text = "He leído y acepto el aviso de privacidad y leyenda de devolución",
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = TextDecoration.Underline
                        ),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
                Checkbox(
                    checked = valorAvisos,
                    onCheckedChange = {
                        btVM.setIntent(false)
                        valorAvisos = it },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)

                )

            }
            if (!valorAvisos && estado.value.intent){
                Etiqueta("Debes confirmar que haz leído y aceptado el aviso de privacidad y leyenda de devolución")
            }
            Row {
                TextButton(onClick = { navController.navigate(Pantallas.RUTA_AVISO) }, modifier = Modifier.weight(5f)) {
                    Text(text = "Permitir el uso de datos con fines de marketing",
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
                Checkbox(
                    checked = valorMarketing,
                    onCheckedChange = {
                        btVM.setIntent(false)
                        valorMarketing = it },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)

                )

            }
            Spacer(modifier = Modifier.padding(16.dp))
            TextButton(onClick = {
                btVM.setIntent(true)
                if(valorNombre.isNotEmpty() && valorApellidoPaterno.isNotEmpty() && valorApellidoMaterno.isNotEmpty()
                    && valorCorreo.isNotEmpty() && valorPassword.isNotEmpty() && !estadoErrors.value.errorContrasenas
                    && valortelefono.isNotEmpty() && valorAvisos) {
                    navController.navigate(Pantallas.RUTA_LOGIN)
                                    } else if (valorNombre.isEmpty() || valorApellidoPaterno.isEmpty() || valorApellidoMaterno.isEmpty()){
                                        btVM.setErrorType(true)
                                    } else if (valortelefono.isEmpty()){
                                        btVM.setErrorCell(true)
                                    } else if (!valorAvisos){
                                        btVM.setErrorLogin(true)
                                    }
                                 },
                modifier = Modifier
                    .padding(horizontal = 100.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.tertiary)){
                Text(
                    text = "Registrarse",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
            PreguntaBoton("¿Ya tienes cuenta?","Inicia sesión", {navController.navigate(Pantallas.RUTA_LOGIN)})


        }
    }
}