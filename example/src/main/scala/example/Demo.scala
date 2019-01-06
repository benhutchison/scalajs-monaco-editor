package example

import scala.concurrent._

import scala.scalajs._
import org.scalajs._

import dom._

import facade.monaco.editor._

import scala.concurrent.ExecutionContext.Implicits.global


object Example {
  def main(args: Array[String]): Unit = {

    for {
      _ <- loadMonaco()
    } {
      Editor.create(document.body, facade.jsObject((o: IEditorConstructionOptions) => {
        o.value = "function hello() {\n\talert('Hello world!');\n}"
        o.language = "javascript"
      }))
    }
  }

  //I wasn't able to get Monaco to load as a ES Module using @JSImport despite much effort
  //In the end I fell back to copying the technique used in Metabrowse (https://github.com/scalameta/metabrowse),
  // where the module is loaded in code
  //See also https://github.com/Microsoft/monaco-editor-samples/blob/master/browser-amd-editor/index.html
  def loadMonaco(): Future[Unit] = {
    val promise = Promise[Unit]()
    js.Dynamic.global.require(js.Array("vs/editor/editor.main"), {
      ctx: js.Dynamic =>
        println("Monaco Editor loaded")
        promise.success(())
    }: js.ThisFunction)
    promise.future
  }

}