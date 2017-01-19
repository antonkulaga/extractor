import fastparse.all._
import org.comp.bio.aging.extractor.ReferenceParser

class SimpleTestParser(subst: Vector[String]) extends ReferenceParser[String](subst)(identity){

}
