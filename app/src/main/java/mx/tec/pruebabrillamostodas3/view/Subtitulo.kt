package mx.tec.pruebabrillamostodas3.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author Santiago Chevez
 * @author Andrés Cabrera
 * Es el formato de los subtitulos que s epueden encotnrar en la aplicación.
 * @param text Texto del subtitulo
 * @param modifier Modificador
 * @param color Color del subtitulo
 * @param fontSize Tamaño de fuente del subtitulo
 */
@Composable
fun Subtitulo(text: String, modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primaryContainer,fontSize: Int = 16){
    Text(
        text = text,
        fontSize = fontSize.sp,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        fontStyle = FontStyle.Italic,
        color = color,
        modifier = modifier.padding(bottom = 15.dp)
            .fillMaxWidth()
    )
}