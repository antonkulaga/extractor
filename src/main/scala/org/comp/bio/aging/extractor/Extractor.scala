package org.comp.bio.aging.extractor

import pl.edu.icm.cermine.ContentExtractor
import pl.edu.icm.cermine.bibref.model.BibEntry

import scala.collection.immutable.{List, Seq}
import scala.collection.JavaConverters._
import better.files._
import java.io.{FileInputStream, InputStream, File => JFile}

trait Extractor {

  protected def refs2Biblio(refs: Seq[Reference]) = for(
    (r, index) <- refs.zipWithIndex) yield r.toBiblioReference(index + 1)

  def printBiblioReferences(file: JFile) = {
    val bib = extractBibEntries(file)
    val refs = bibEntries2References(bib)
    import purecsv.safe._
    for(r <- refs2Biblio(refs)) println(r.toCSV("\t"))
  }

  def writeBiblioReferences2File(file: JFile, toFileName: String): Unit = {
    writeBiblioReferences2File(file, File(toFileName).toJava)
  }

  def writeBiblioReferences2File(file: JFile, to: JFile): Unit = {
    val bib = extractBibEntries(file)
    val refs = bibEntries2References(bib)
    import purecsv.safe._
    val sep = if(to.getName.endsWith("tsv")) "\t" else ";"
    val headers = Seq("#", "url", "title", "authors", "journal", "year")
    println(s"writing results to the file ${to} : ")
    refs2Biblio(refs).writeCSVToFile(to, sep, Some(headers))
  }

  def bibEntries2References(entries: List[BibEntry]): List[Reference] = entries.map(e=> Reference(e))

  def extractBibEntries(file: JFile): List[BibEntry] = extractBibEntries(new FileInputStream(file))

  def extractBibEntries(path: String): List[BibEntry] = extractBibEntries(new FileInputStream(path))

  def extractBibEntries(stream: InputStream): List[BibEntry] = {
    extractor(stream).getReferences.asScala.toList
  }

  private def extractor(inputStream: InputStream): ContentExtractor = {
    val extractor = new ContentExtractor()
    extractor.setPDF(inputStream)
    extractor
  }
}
