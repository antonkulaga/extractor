package org.comp.bio.aging.extractor

import pl.edu.icm.cermine.ContentExtractor
import pl.edu.icm.cermine.bibref.model.BibEntry

import scala.collection.immutable._
import scala.collection.JavaConverters._
import better.files._
import java.io.{FileInputStream, InputStream, File => JFile}

/**
  * Class that extracts biblioRefences from original Cermine BibEntries
  * @param entries
  */
class BibReferences(entries: IndexedSeq[BibEntry]){
  lazy val references: IndexedSeq[Reference] = entries.map(e=> Reference(e))
  lazy val biblioReferences: IndexedSeq[BiblioReference] = for((r, index) <- references.zipWithIndex) yield r.toBiblioReference(index + 1)
}

class Extractor {

  def refs2BiblioRefs(refs: Seq[Reference]) = for(
    (r, index) <- refs.zipWithIndex) yield r.toBiblioReference(index + 1)

  def extractBibEntries(file: JFile): BibReferences = extractBibEntries(new FileInputStream(file))

  def extractBibEntries(path: String): BibReferences = extractBibEntries(new FileInputStream(path))

  def extractBibEntries(stream: InputStream): BibReferences = {
    new BibReferences(extractor(stream).getReferences.asScala.toVector)
  }

  private def extractor(inputStream: InputStream): ContentExtractor = {
    val extractor = new ContentExtractor()
    extractor.setPDF(inputStream)
    extractor
  }
}
