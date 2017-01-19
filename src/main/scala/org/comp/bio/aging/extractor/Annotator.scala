package org.comp.bio.aging.extractor

import better.files._
import java.io.{File => JFile}

import fastparse.all._
import fastparse.core.Parser

import scala.collection.immutable.Seq

class Annotator(refs: BibReferences) {

 // val parser = new BiblioReferenceParser(refs.biblioReferences)

  def update(file: JFile, refs: Seq[BiblioReference]) = {
    val fl = file.toScala
    val str = fl.contentAsString
    fl.write(str)
  }

  protected def refs2Biblio(refs: Seq[Reference]) = for(
    (r, index) <- refs.zipWithIndex) yield r.toBiblioReference(index + 1)


}
