package org.comp.bio.aging.extractor

import better.files._
import java.io.{File => JFile}

import org.backuity.clist._

class Extract extends Command(description = "extracts references from pdf") with Common{

  lazy val extractor  = new Extractor

  var from: JFile = arg[JFile](description = "pdf to extract references from")

  var update: JFile = opt[JFile](description = "Into some text")

  var tsv: String = opt[String](description = "TSV to write extracted references to")

  protected def printBiblioReferences(file: JFile) = {
    val refs = extractor.extractBibEntries(file)
    import purecsv.safe._
    for(r <- refs.biblioReferences) println(r.toCSV("\t"))
  }

  protected def writeTSV(refs: BibReferences, where: String): Unit = {
    import purecsv.safe._
    val to = File(where)
    val sep = if(where.endsWith("tsv")) "\t" else ";"
    val headers = Seq("#", "url", "title", "authors", "journal", "year")
    println(s"writing results to the file ${to} : ")
    refs.biblioReferences.writeCSVToFile(to.toJava, sep, Some(headers))
  }

  protected def rewriteFile(refs: BibReferences, file: File) = {
    val content = file.contentAsString
    val parser = new BiblioReferenceParser(refs.biblioReferences)
    parser.text
  }

  def run() = {
    from match {
      case file if file.exists() && this.update.toScala.notExists && this.tsv.isEmpty=>
        printBiblioReferences(file)

      case file if file.exists()=>
        val refs: BibReferences = extractor.extractBibEntries(file)
        if(tsv.nonEmpty) writeTSV(refs, tsv)
        if(update.exists()) rewriteFile(refs, update.toScala)


      case non => throw new Exception(s"File ${non.getPath} does not exist")
    }
  }
}


