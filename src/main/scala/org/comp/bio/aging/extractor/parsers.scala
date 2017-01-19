package org.comp.bio.aging.extractor

import fastparse.all._
import fastparse.core.Parser
import scala.collection.immutable._

class BiblioReferenceParser(refs: IndexedSeq[BiblioReference]) extends ReferenceParser[BiblioReference](refs)(r=>r.url) {
}

class ReferenceParser[T](refs: IndexedSeq[T])(implicit conv: T => String) extends BasicParser {
  lazy val normal: P[String] = P( CharsWhile(_ != '[').! )

  val oneRef: P[String] = P(integer).map{
    case index if index - 1 < refs.length => conv(refs(index - 1))
    case tooLarge =>
      throw new Exception(s"too large index ${tooLarge} while refs length is ${refs.length}")
      tooLarge.toString
  }

  val simpleRef = P(oneRef ~
    ("," ~ optSpaces ~ oneRef).rep).map{
    case (first, seq) if seq.isEmpty => first
    case (first, seq) => seq.foldLeft(first){ case (acc, el) => acc + ", " + el}
  }

  val refSpan = P(integer ~ optSpaces ~ "-" ~ optSpaces ~ integer).map{
    case (from, to) if from > to =>
      throw new Exception(s"${from} cannot be > ${to}")
    case (from, tooLarge) if tooLarge > refs.length =>
      throw new Exception(s"too large index ${tooLarge} while refs length is ${refs.length}")
    case (start, end)  =>
      val seq = for( index <- start to end) yield conv(refs(index - 1))
      seq.foldLeft(""){
        case ("", el) => el
        case (acc, el) => acc + ", " + el
      }
  }

  val ref = P(refSpan | simpleRef)

  val reference: P[String] = P("[" ~ optSpaces ~ ref ~ optSpaces ~ "]")

  val text: P[String] = P((normal ~ reference.?).rep).map {
    values =>
      values.foldLeft("") {
        case (acc, (txt, Some(r))) => acc + txt + "[" + r + "]"
        case (acc, (txt, None)) => acc + txt
      }
  }
}

trait BasicParser {
  protected val optSpaces = P(CharIn(" \t").rep(min = 0))
  protected val spaces = P(CharIn(" \t").rep(min = 1))
  protected val digit = P(CharIn('0'to'9'))
  protected val letter = P(CharIn('A' to 'Z') | CharIn('a' to 'z'))
  protected val d = P(optSpaces ~ CharIn(";\n") ~ optSpaces)

  protected val integer: P[Int] = P(
    "-".!.? ~ digit.rep(min = 1).!).map{
    case (None, str) => str.toInt
    case (Some(_), str) => - str.toInt
  }


  protected val normalNumber = P(
    integer ~ ("."~ integer).?
  ).map{
    case (i, None) => i.toDouble
    case (i, Some(o)) => (i + "." + o).toDouble
  }

  protected val eNumber = P( normalNumber ~ CharIn("Ee") ~ integer ).map{
    case (a, b) => a * Math.pow(10, b)
  }

  val number: P[Double] = P( eNumber | normalNumber )
}