const { replaceSVGTemplatePlaceholders } = require(".");

const svgTemplate = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"250\" height=\"400\" viewBox=\"0 0 250 400\">\n" +
            "<text x=\"20\" y=\"60\" fill = \"#0000ff\" font-size=\"18\" font-weight=\"bold\">Hi {{user/name}}</text>\n" +
            "</svg>";

const data = {
    "user": {
     "name":   "BalaG"
    }
}

const replacedData = replaceSVGTemplatePlaceholders(svgTemplate, data);



console.log(replacedData)