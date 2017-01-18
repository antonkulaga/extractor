package org.comp.bio.aging.extractor

import better.files._
import java.io.{File => JFile}

import scala.collection.immutable.Seq

trait Annotator {

  def update(file: JFile, refs: Seq[BiblioReference]) = {
    val fl = file.toScala
    val str = fl.contentAsString
    fl.write(str)
  }

  protected def refs2Biblio(refs: Seq[Reference]) = for(
    (r, index) <- refs.zipWithIndex) yield r.toBiblioReference(index + 1)


}
