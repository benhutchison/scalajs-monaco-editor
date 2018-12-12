#!/bin/bash

set -euox pipefail

MONACO_VERSION=${1:-`npm show monaco-editor version`}
ROOT_DIR="$(git rev-parse --show-toplevel)"
MONACO_SCALA="$ROOT_DIR/src/main/scala/facade/monacoEditorFacade.scala"
WORK_DIR=`mktemp -d`

pushd $WORK_DIR

wget -O monaco-editor.tgz `npm v monaco-editor@$MONACO_VERSION dist.tarball`

tar xf monaco-editor.tgz

# Curated list of edits required for scala-js-ts-importer to parse monaco.d.ts
sed \
   -e 's/): [^ ]* is [^; ]*;/): boolean;/' \
   -e 's/: void | /: /' \
   -e 's/onDidChange?: IEvent<this>/onDidChange?: IEvent<CodeLensProvider>/' \
   -e '/^declare module monaco.languages.\(typescript\|html\|css\|json\) {$/,/^}$/d' \
   < package/monaco.d.ts \
   > monaco.d.ts

git clone https://github.com/sjrd/scala-js-ts-importer.git

cd scala-js-ts-importer

sbt -batch "run ../monaco.d.ts $MONACO_SCALA facade"

echo -e "\n\nobject MonacoEditorVersion {def apply: String = \"$MONACO_VERSION\"}\n" >> $MONACO_SCALA

cd ..
rm -rf scala-js-ts-importer/
rm -rf package/
rm monaco-editor.tgz

echo "Work dir $WORK_DIR can be cleaned up now"

popd

