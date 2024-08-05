package io.mosip.injivcrendererexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.mosip.injivcrendererexample.ui.theme.InjivcrendererexampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InjivcrendererexampleTheme {
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


fun replaceTemplate(){
    val svgTemplate = """
        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="250" height="400" viewBox="0 0 250 400">
          <defs>
            <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" style="stop-color:#91d9e3;stop-opacity:1" />
              <stop offset="100%" style="stop-color:#ffA500;stop-opacity:1" />
            </linearGradient>
            <clipPath id="rounded-clip">
              <rect width="100%" height="100%" rx="15" ry="15" />
            </clipPath>
          </defs>
          <!-- Background rectangle with rounded corners -->
          <rect width="100%" height="100%" fill="url(#gradient)" rx="15" ry="15" />
          
          <!-- Main image clipped with rounded corners -->
          <image id="main-image" width="100%" height="100%" href="https://34e6-2401-4900-1f2b-c22-c944-58cd-bfed-966c.ngrok-free.app/background.png" clip-path="url(#rounded-clip)" /> 
          
            <!-- QR Code -->
          <image 
            x="170" 
            y="15" 
            width="70" 
            height="70" 
            xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABBoAAAQaCAYAAADt3BtfAAAAAXNSR0IArs4c6QAAAARzQklUCAgI
        CHwIZIgAACAASURBVHic7N29cGkUGgAAAIA0Cg0AAABAGoUG
        AAAAII1CAwAAAJBGoQEAAABIo9AAAAAApFFoAAAAANL8F7csoc8C012cAAAAAElFTkSuQmCC" 
            clip-path="url(#rounded-clip)" 
          />
          
          <!-- Other images and text elements -->
          <image 
            x="5" 
            y="-15" 
            width="50" 
            height="50" 
            xlink:href="https://sunbird.org/images/sunbird-logo-new.png" 
            clip-path="url(#rounded-clip)" 
          />
          <image 
            x="70" 
            y="70" 
            width="100" 
            height="100" 
            xlink:href="https://83b8-2401-4900-1cd1-a419-6154-ee6-76a4-466.ngrok-free.app/profile_photo.png" 
            clip-path="url(#rounded-clip)" 
          />
          <text x="20" y="200" fill="#000000" font-size="16" font-weight="bold">{{credentialSubject/policyName}}</text>
          <text x="80" y="220" fill="#0000ff" font-size="18" font-weight="bold">{{credentialSubject/policyNumber}}</text>
          <text x="20" y="240" fill="#000000" font-size="16" font-weight="bold">{{credentialSubject/fullName}}</text>
          <text x="20" y="260" fill="#000000" font-size="12" font-weight="normal">{{credentialSubject/gender}}</text>
          <text x="20" y="280" fill="#000000" font-size="12" font-weight="normal">{{credentialSubject/email}}</text>
          <text x="20" y="300" fill="#000000" font-size="12" font-weight="normal">{{credentialSubject/mobile}}</text>

          <text x="5" y="350" fill="#000000" font-size="12" font-weight="normal">Policy Issued On</text>
          <text x="5" y="370" fill="#0000ff" font-size="12" font-weight="normal">{{credentialSubject/policyIssuedOn}}</text>

          <text x="150" y="350" fill="#000000" font-size="12" font-weight="normal">Policy Expires On</text>
          <text x="150" y="370" fill="#0000ff" font-size="12" font-weight="normal">{{credentialSubject/policyExpiresOn}}</text>
        </svg>
    """.trimIndent()

    val vcData = """
     {
        "@context": [
            "https://www.w3.org/2018/credentials/v1",
            "https://holashchand.github.io/test_project/insurance-context.json",
            "https://w3id.org/security/suites/ed25519-2020/v1"
        ],
        "credentialSubject": {
            "id": "did:jwk:eyJrdHkiOiJSU0EiLCJlIjoiQVFBQiIsInVzZSI6InNpZyIsImFsZyI6IlJTMjU2IiwibiI6IndGQ2ZaZlM0QUNmTEkzZHZMUnduNWhRQ3pLUkFkS1lKSWlUZkNDZ0VhTjVLcFBOVVNKaENVWXpwY0w1a0FneWs5MjNCaUJpbC1YZUxIRU5rSjR0MXM1ODl6T0FQY0lSNFIwbUFNU051d2tmeDM2SUFNWlhUVk1lUlVCTEwyUXd3TzlSU3NQX3E2RVVwU2hnQzB6SG5rNWpXeDRLbXd0M3pkOTVISXBXT1lldHFfbDZIVHZPbS02VEhlZTFLODVTRnNxUVZGdFVjamZLRjVkUmtILWZjVk5lNEdxbUxSeFQ3VTVXb29rRGs3SkFUUlJ3bjdYaUc4Q0FkdXRITVZwSTNVUUNWT0JKVDJSbENEa0g2RGpVTktqX2FxWkpDalNQWVF1RlFFTVRzZ2JaUjA5cFBzMC1uU0NuYTRWb0U4bHFrOEU2aDEwUzRicnptV2pVa25tNGYydyJ9",
            "dob": "2000-01-01",
            "email": "aswin@gmail.com",
            "gender": "Female",
            "mobile": "0123456789",
            "benefits": [
                "Critical Surgery",
                "Full body checkup"
            ],
            "fullName": "Aswin",
            "policyName": "Start Insurance Gold Premium",
            "policyNumber": "1607-2024",
            "policyIssuedOn": "2024-07-16",
            "policyExpiresOn": "2034-07-16"
        },
        "expirationDate": "2024-08-23T07:27:35.635Z",
        "id": "did:rcw:d262fe62-e7a9-40bb-b5b7-3659dac9f187",
        "issuanceDate": "2024-07-24T07:27:35.644Z",
        "issuer": "did:web:api.dev1.mosip.net:identity-service:8ebda1d0-665b-4bb7-abc7-d4bf56b6ee09",
        "proof": {
            "created": "2024-07-24T07:27:36Z",
            "proofPurpose": "assertionMethod",
            "proofValue": "z5MtFyNtLn79nbsHxgg9oPGb4jmHNoUPRFdLQLcgwemrcUvoLLbHD8M6sJmajPp6ZAitUJpKVZi7Qwp2TqUB92vgR",
            "type": "Ed25519Signature2020",
            "verificationMethod": "did:web:api.dev1.mosip.net:identity-service:8ebda1d0-665b-4bb7-abc7-d4bf56b6ee09#key-0"
        },
        "type": [
            "VerifiableCredential",
            "InsuranceCredential"
        ],
        "renderMethod": [
            {
                "id": "https://83b8-2401-4900-1cd1-a419-6154-ee6-76a4-466.ngrok-free.app/insurance_svg_template.svg",
                "type": "SvgRenderingTemplate",
                "name": "Portrait Mode"
            }
        ]
    }
    """.trimIndent()



    val updatedData = InjiVcRenderer().replaceSVGTemplatePlaceholders(svgTemplate, vcData)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InjivcrendererexampleTheme {
        Greeting("Android")
    }
}