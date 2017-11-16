var webpack = require("webpack");
var path = require("path");
var outputMin = "build/classes/main/min/";

module.exports = {
    entry: path.resolve(__dirname, outputMin + "kotlin-conf-js.js"),
    output: {
        path: path.resolve(__dirname, "build"),
        filename: "bundle.js"
    },
    resolve: {
        alias: {
            'conf-js':          path.resolve(__dirname, outputMin + "conf-js.js"),
            'runner-js':        path.resolve(__dirname, outputMin + "runner-js.js"),
            'kotlin':           path.resolve(__dirname, outputMin + "kotlin.js"),
            'kotlinx-html-js':  path.resolve(__dirname, outputMin + "kotlinx-html-js.js")
        }
    },
    plugins: [
        new webpack.optimize.UglifyJsPlugin()
    ]
};
