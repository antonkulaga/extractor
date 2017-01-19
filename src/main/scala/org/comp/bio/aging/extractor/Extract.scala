package org.comp.bio.aging.extractor

import better.files._
import java.io.{File => JFile}

import fastparse.core.Parsed
import org.backuity.clist._

/**
  * Ectract command
  */
class Extract extends Command(description = "extracts references from pdf") with Common {

  lazy val extractor  = new Extractor

  var from = arg[JFile](description = "pdf to extract references from")

  var tsv = arg[java.io.File](required = false, description = "TSV to write extracted references to", default = new JFile(""))

  var update = arg[java.io.File](required = false, description = "Substitute references in [] by sci-hub links in the file", default = new JFile(""))

  var updateTo = arg[java.io.File](required = false, description = "if present than writes substitutions to the new file", default = new JFile(""))


  protected def printBiblioReferences(refs: BibReferences) = {
    println("extracted references:\n")
    import purecsv.safe._
    for(r <- refs.biblioReferences) println(r.toCSV("\t"))
  }

  protected def writeTSV(refs: BibReferences, to: JFile): Unit = {
    import purecsv.safe._
    println(s"writing tsv to ${to.getAbsolutePath}")
    val sep = if(to.getName.endsWith("tsv")) "\t" else ";"
    val headers = Seq("#", "url", "title", "authors", "journal", "year")
    println(s"writing references to tsv at ${to.getAbsolutePath}")
    refs.biblioReferences.writeCSVToFile(to, sep, Some(headers))
  }

  protected def rewriteFile(refs: BibReferences, fromFile: File, toFileOpt: Option[File] = None) = {
    println(s"substituting references in the file: ${fromFile.path}")
    val content = fromFile.contentAsString
    val parser = new BiblioReferenceParser(refs.biblioReferences)
    val writeTo = toFileOpt.getOrElse(fromFile)
    val Parsed.Success(result, _) = parser.text.parse(content)
    println(s"writing to ${writeTo.path}")
    writeTo.write(result)
  }

  def run() = {
    from match {
      case file if file.exists() && file.isFile=>
        println("extracting references...")
        val refs: BibReferences = extractor.extractBibEntries(file)
        println("extraction complete")
        if(!(update.exists() && update.isFile) && tsv != new JFile("")){
          printBiblioReferences(refs)
        } else {
          if(!tsv.isDirectory) writeTSV(refs, tsv)
          val writeToOpt = if(this.updateTo==new JFile("")) None else Some(updateTo.toScala)
          if(update.isFile && update.exists()) rewriteFile(refs, update.toScala, writeToOpt)
        }

      case non => throw new Exception(s"File ${non.getPath} does not exist!")
    }
  }
}


