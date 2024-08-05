package io.mosip.injivcrenderer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.mosip.injivcrenderer.ui.theme.InjiVcRendererJarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InjiVcRendererJarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val svgTemplate = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"250\" height=\"400\" viewBox=\"0 0 250 400\">\n" +
            "<text x=\"20\" y=\"60\" fill = \"#0000ff\" font-size=\"18\" font-weight=\"bold\">Hi {{user/name}}</text>\n" +
            "</svg>";
    val sampleJson = """
        {
            "user": {
                "name": "Jon Doe"
            }
        }
    """.trimIndent()
    Column() {
        Text(
            text = "Hello $svgTemplate!",
            modifier = modifier
        )
        Button(onClick = {
            val replacedTemplate = InjiVcRenderer().replaceSVGTemplatePlaceholders(svgTemplate, sampleJson)
            System.out.print("---->$replacedTemplate")
        }) {
            Text(text = "Replace")

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InjiVcRendererJarTheme {
        Greeting("Android")
    }
}