package org.com.bio.aging.exractor

import java.io.{InputStream, File => JFile}

import org.comp.bio.aging.extractor.{Annotator, Extractor, Reference}
import org.scalatest.{Matchers, WordSpec}

class CommandSpec extends WordSpec with Matchers {

  object extractor extends Extractor
  object annotator extends Annotator

  "Extractor" should {

    "read references from the pdf" in {
      val pdf: InputStream = getClass().getClassLoader().getResourceAsStream("1-8-2017_Mammalian.pdf")
      val refs = extractor.extractBibEntries(pdf)
      refs.size shouldEqual 190
      val l = Reference(refs.last)
      l.doi shouldEqual Some("10.1002/bit.20499")
      //l.url shouldEqual Some("""<a href="http://sci-hub.cc/10.1002/bit.20499">10.1002/bit.20499</a>""")
      l.url shouldEqual Some("""http://sci-hub.cc/10.1002/bit.20499""")
    }

    "read switches" in {
      val pdf: InputStream = getClass().getClassLoader().getResourceAsStream("1-8-2017_Mammalian.pdf")
      val refs = extractor.extractBibEntries(pdf)
      val switch = getClass().getClassLoader().getResourceAsStream("Switches - Sheet1.tsv")
      
      annotator
    }

    //"write references to TSV" in { }

  }
}
