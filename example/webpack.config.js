const ScalaJS = require("./scalajs.webpack.config");
const Merge = require("webpack-merge");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require('copy-webpack-plugin');

const path = require("path");
const rootDir = path.resolve(__dirname, "../../../..");
const resourcesDir = path.resolve(rootDir, "src/main/resources");
const MonacoEditorBaseDir = path.resolve(__dirname, 'node_modules/monaco-editor/min')

const WebApp = Merge(ScalaJS, {
  module: {
    rules: [
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"]
      }
    ]
  },
  plugins: [
      new CopyWebpackPlugin([
        {
          from: path.resolve(MonacoEditorBaseDir, 'vs'),
          to: 'vs',
          ignore: [ 'basic-languages/**/*' ]
        }
      ]),
      new HtmlWebpackPlugin({
        inject: true,
        template: resourcesDir + '/index.html',
      })
    ]
});

module.exports = WebApp;