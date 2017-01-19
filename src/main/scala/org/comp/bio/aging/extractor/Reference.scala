package org.comp.bio.aging.extractor

import pl.edu.icm.cermine.bibref.model.BibEntry

import scala.collection.JavaConverters._

object Reference {
  def apply(entry: BibEntry): Reference = {
    val keys = entry.getFieldKeys.asScala
    val fields: Map[String, List[String]] = keys.map(key => key-> entry.getAllFields(key).asScala.map(_.getText).toList).toMap
    new Reference(fields)
  }
}

class Reference(fields: Map[String, List[String]], position: Int = 0)
{
  protected def getHead(name: String) = get(name).flatMap(list=>list.headOption)
  protected def get(name: String) = fields.get(name)

  lazy val doi: Option[String] = for(value <- getHead("doi"))
    yield {
      if (value.endsWith(")") && (value.count(_ == ')') > value.count(_ == ')') ) )
        value.substring(0, value.length - 1)
      else value.substring(0, value.length - 1)
    }

  lazy val sciHubDoi: Option[String] = for{
      value <- doi
    } yield s"http://sci-hub.cc/${value}"

  lazy val url = for{
    link <- sciHubDoi
    d <- doi
  } yield link //s"""<a href="${link}">${d}</a>"""

  lazy val title: Option[String] = getHead("title")
  lazy val authors: Option[List[String]] = get("author")
  lazy val journal: Option[String] = getHead("journal")
  lazy val volume: Option[String] = getHead("volume")
  lazy val year: Option[String] = getHead("year")


  def toBiblioReference(num: Int): BiblioReference = {
    val na = "N/A"
    val auth = authors.fold("N/A")(list => list.foldLeft(""){
      case ("", e) => e
      case (acc, e)=> acc +", "+ e
    })
    BiblioReference(num, url.getOrElse(num.toString), title.getOrElse(na), journal.getOrElse(na), auth, year.getOrElse(na) )
  }
  //def hasPosition(str: String) =
}

case class BiblioReference(num: Int, url: String, title: String, authors: String, journal: String, year: String)
{
}