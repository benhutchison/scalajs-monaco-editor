# Scalajs [facade](./src/main/scala/facade/monacoEditorFacade.scala) for Monaco Editor

The artifact version reflects the Monaco Editor version it's based on.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.benhutchison/scalajs-monaco-editor_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.benhutchison/scalajs-monaco-editor_2.12)

# Generation

When I started out, I'd naively hoped that the generation process would be fully automated. It may one day reach that point, but
it seems far off, as currently considerable judgement & hand-editing is required to get the generated Scalajs facade to compile.

1. The first step however *is* automated; this script:
 - downloads the monaco npm package
 - extracts the source
 - performs some mechanical edits using `sed` required for import to work
 - runs the [typescript to scalajs importer](https://github.com/sjrd/scala-js-ts-importer)
 - appends a `MonacoEditorVersion` object based on the npm version

```
bin/generate_monaco_facade.sh <optional version, default latest>
```

Remember to clean up the workdir (printed by script) afterwards

2. Then begins a series of edits required to get the facade to compile. Rough notes:

Basic JS types need to be imported
```
import js.|
import js.RegExp
import js.Thenable
```

Monaco refers to some DOM types (from `"org.scala-js" %%% "scalajs-dom"`):
```
import org.scalajs.dom._
import org.scalajs.dom.raw.HTMLElement
```

There are monaco subpackages that define types referred to elsewhere that aren't visible until imported, eg:
```
import facade.monaco.editor.Editor.{BuiltinTheme, IColors, IEditorModel, IEditorViewState, EditorAutoClosingStrategy, EditorAutoSurroundStrategy}
import facade.monaco.editor.Editor.{IReadOnlyModel, IModel}

import facade.monaco.languages.Languages.{CharacterPair, ProviderResult, Definition, TextEdit, IShortMonarchLanguageRule1}
import facade.monaco.languages.Languages.{IShortMonarchLanguageRule2, IMonarchLanguageRule, IShortMonarchLanguageAction, IMonarchLanguageAction}

import facade.monaco.languages.typescript.Typescript.CompilerOptionsValue
```

The importer has a tendency to define scala `var`s for read/write properties, and then try to override them in a subclass
with a `def` readonly field, which is not legal scala. Generally just remove the fields from the subclass.

Other times it will override a def with a def in a subclass, but not use `override` keyword, so I just added it.

Removed dubious default values for parameters like `limitResultCount: Double = ???`

Some overrides it generates are not legal in Scala's type system, where a subclass signature returns a narrower (more specific)
type than the method it overrides. The problem comes when the union type `|` is used, eg `def getModel(): ITextModel | Null`,
because `|` is invariant in its params. So `ITextModel <: IEditorModel` doesn't imply that `ITextModel | Null <: IEditorModel | Null`.
The solution Ive used is to change the return types to the less specific base signature, though that might require downcasts
in application code that uses Monaco.

Finally, after these steps the facade should compile.

# Example Using the Facade

See the [example subproject](./example/)

# Credits & Thanks

The contributors to the importer https://github.com/sjrd/scala-js-ts-importer

Typescript -> Scalajs conversion is built on work by @jonas in https://github.com/scalameta/metabrowse


