import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TextPrompterApp() {
    var text by remember { mutableStateOf("Bu bir metin prompter uygulamasıdır.\nBuraya kendi metninizi ekleyebilirsiniz.") }
    var isScrolling by remember { mutableStateOf(false) }
    var scrollSpeed by remember { mutableStateOf(50L) } // Milisaniye cinsinden hız
    var fontSize by remember { mutableStateOf(24f) }
    var isFullscreen by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isScrolling) {
        while (isScrolling) {
            scrollState.scrollTo(scrollState.value + 5) // Yavaş kaydırma
            delay(scrollSpeed)
        }
    }

    if (isFullscreen) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .padding(top = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * 1.3).sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Button(onClick = { isScrolling = !isScrolling },
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
                Text(if (isScrolling) "Durdur" else "Başlat")
            }

            Button(
                onClick = { isFullscreen = false },
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            ) {
                Text("Tam Ekrandan Çık")
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Metin") },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { isScrolling = !isScrolling }) {
                    Text(if (isScrolling) "Durdur" else "Başlat")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        scrollState.scrollTo(0)
                    }
                }) {
                    Text("Başa Dön")
                }
                Button(onClick = { isFullscreen = true }) {
                    Text("Tam Ekran")
                }
            }
            Text("Akış Hızı: ${scrollSpeed.toFloat()}", modifier = Modifier.padding(top = 8.dp))
            Slider(
                value = scrollSpeed.toFloat(),
                onValueChange = { scrollSpeed = it.toLong() },
                valueRange = 10f..200f,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Yazı Boyutu: ${fontSize.toInt()}", modifier = Modifier.padding(top = 8.dp))
            Slider(
                value = fontSize,
                onValueChange = { fontSize = it },
                valueRange = 12f..72f,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .weight(2f) // Prompter alanına esnek yer ayır
                    .fillMaxWidth()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        lineHeight = (fontSize * 1.3).sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
