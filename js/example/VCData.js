const VC = {
  "@context": [
    "https://www.w3.org/ns/credentials/v2",
    "https://www.w3.org/ns/credentials/examples/v2",
    "https://w3id.org/vc/render-method/v1"
  ],
  "id": "http://example.edu/credentials/3732",
  "type": ["VerifiableCredential", "UniversityDegreeCredential"],
  "issuer": "https://example.edu/issuers/14",
  "validFrom": "2010-01-01T19:23:24Z",
  "credentialSubject": {
    "id": "did:example:ebfeb1f712ebc6f1c276e12ec21",
    "name":"BlackNight",
    "dob": "1981-05-01",
    "email": "krish@mosip.io",
    "gender": "Male",
    "mobile": "+91 9343909050",
    "benefits": [
      "Guaranteed Income Benefit",
      "Payer Premium Protection Cover"
    ],
    "fullName": "Krishnan",
    "policyName": "Guaranteed Future Plus",
    "policyNumber": "1012",
    "policyIssuedOn": "2024-05-05",
    "policyExpiresOn": "2025-05-04",
    "degree": {
      "type": "BachelorDegree",
      "name": "Bachelor of Science and Arts"
    }
  },
  "renderMethod": [{
    "id": "https://350f-106-221-28-222.ngrok-free.app/insurance_svg_template.svg",
    "type": "SvgRenderingTemplate",
    "name": "Portrait Mode",
    "css3MediaQuery": "@media (orientation: portrait)",
    "digestMultibase": "zQmAPdhyxzznFCwYxAp2dRerWC85Wg6wFl9G270iEu5h6JqW"
  }]
}

const svgTemplate = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"250\" height=\"400\" viewBox=\"0 0 250 400\">\n" +
    "<text x=\"20\" y=\"60\" fill = \"#0000ff\" font-size=\"18\" font-weight=\"bold\">Hi {{credentialSubject/name}}</text>\n" +
    "</svg>";

module.exports = {VC, svgTemplate};
