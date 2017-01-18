package org.comp.bio.aging.extractor

import java.io.File

import org.backuity.clist._

object Extract extends Extract
class Extract extends Command(description = "extracts references from pdf") with Common{

  object extractor extends Extractor

  var pdf: File = arg[File](description = "pdf to extract references from")

  var output: String = arg[String](description = "TSV to write extracted references to")

  def run() = {
    pdf match {
      case file if file.exists() && this.output != ""=>
        extractor.writeBiblioReferences2File(file, this.output)

      case file if file.exists()=>
        extractor.printBiblioReferences(file)

      case non => throw new Exception(s"File ${non.getPath} does not exist")
    }
  }
}


