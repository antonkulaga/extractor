import java.io.{InputStream, File => JFile}

import fastparse.all._
import org.comp.bio.aging.extractor._
import org.scalatest.{Matchers, WordSpec}
import pl.edu.icm.cermine.bibref.model.BibEntry
import better.files._
import java.io.{FileInputStream, InputStream, File => JFile}

import scala.io.Source



class CommandSpec extends WordSpec with Matchers {


  "Extractor" should {

    "substitute [] with other things" in {
      val txt = "hello [1] world [2,3,4] it is time to test it! [4-7]"
      val parser = new SimpleTestParser(Vector("one", "two", "three", "four", "five", "six", "seven"))
      val Parsed.Success(simple, _) = parser.text.parse(txt)
      simple shouldEqual "hello [one] world [two, three, four] it is time to test it! [four, five, six, seven]"
    }

    "read and substitute references from the pdf" in {
      val pdf: InputStream = getClass().getClassLoader().getResourceAsStream("1-8-2017_Mammalian.pdf")
      val extractor =  new Extractor
      val refs = extractor.extractBibEntries(pdf)
      refs.biblioReferences.size shouldEqual 190
      val l = refs.references.last
      l.doi shouldEqual Some("10.1002/bit.20499")
      //l.url shouldEqual Some("""<a href="http://sci-hub.cc/10.1002/bit.20499">10.1002/bit.20499</a>""")
      l.url shouldEqual Some("""http://sci-hub.cc/10.1002/bit.20499""")
      val txt = "hello [1] world [2,3,4] it is time to test it! [4,5]"
      val parser = new BiblioReferenceParser(refs.biblioReferences)
      val Parsed.Success(result, _) = parser.text.parse(txt)
      println(s"RESULT\n$result")
      //http://sci-hub.cc/10.1016/j.chembiol.2009.02.005, http://sci-hub.cc/10.1016/j.copbio.2009.07.009
      val part = """[http://sci-hub.cc/10.1016/j.chembiol.2009.02.005, http://sci-hub.cc/10.1016/j.copbio.2009.07.009]"""
      result.contains(part) shouldEqual true
    }

    /*
   "read switches" in {
       val pdf: InputStream = getClass().getClassLoader().getResourceAsStream("1-8-2017_Mammalian.pdf")
       val extractor =  new Extractor
       val refs = extractor.extractBibEntries(pdf)
       val switch: Source = resource"Switches - Sheet1.tsv"
       val text = switch.getLines().reduce(_ + "\n" + _)
       val parser = new BiblioReferenceParser(refs.biblioReferences)
       val Parsed.Success(result, _) = parser.text.parse(text)
       val part = "[http://sci-hub.cc/10.1021/cb800025k, http://sci-hub.cc/10.1038/nchembio.597]" //[82,83]
       result.contains(part) shouldEqual(true)
     }
    */
  }
}
