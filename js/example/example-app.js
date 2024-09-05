const {VCRenderer} = require("../lib/commonJs/vcrenderer.js")
const {InsuranceVC, MosipVC} = require('./sample-vc.js')
VCRenderer.renderSVG(MosipVC).then(
    (response) => console.log("SVG Image->",response)
);