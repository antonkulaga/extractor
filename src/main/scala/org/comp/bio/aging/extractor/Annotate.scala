package org.comp.bio.aging.extractor

import java.io.File

import org.backuity.clist._

/**
  * Created by antonkulaga on 1/18/17.
  */
class Annotate extends Command(description = "annotates file with references from TSV") with Common {

  object annotator extends Annotator

  var source = arg[File](description = "file with text data")

  var tsv = arg[File](description = "TSV with references")

  var output = arg[File](description = "output file")

  def run() = {

  }
}

object Annotate extends Annotate