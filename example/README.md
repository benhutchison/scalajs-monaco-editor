## Example App

This example creates a Monaco editor instance in a web page. It replies upon

- [Scalajs Bundler to bundle `monaco-editor` and webpack plugin NPM dependencies](https://github.com/scalacenter/scalajs-bundler)
- [Webpack custom configuration](https://github.com/scalacenter/scalajs-bundler/tree/master/sbt-scalajs-bundler/src/sbt-test/sbt-scalajs-bundler/webpack-assets)
- [AMD loading of Monaco Editor](https://github.com/Microsoft/monaco-editor-samples/tree/master/browser-amd-editor)

To run:

```
sbt> example/fastOptJS::webpack

bash> open target/scala-2.12/scalajs-bundler/main/index.html
```

You should see an editor pane open in the browser page containing a Hello World function def.
