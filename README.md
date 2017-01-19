A Command Line Tool for extraction of references
================================================

This tools contains commands to extract literature references from pdf-s and
substitute [] with download links to sci-hub.

It requires java-8 to be installed.

```bash

 extract <from> [<tsv>] [<update>] [<updateTo>] : extracts references from pdf

Arguments

   <from>   : pdf to extract references from
   <tsv>    : TSV to write extracted references to
   <update> : Substitute references in [] by sci-hub links in the file
   <updateTo> : if present than writes substitutions to the new file

```

Compiling from source
====================

Just download [sbt](http://www.scala-sbt.org/) and make sure that java-8 is installed.
Then to compile
```
sbt compile
```
to run:
```
sbt run
```

Packaging
=========

run 
```
sbt universal:packageBin
```
And then get the generated zip from target/universal

Notes
=====

Reference extraction is based on [CERMINE](https://github.com/CeON/CERMINE) and is not always very accurate.
