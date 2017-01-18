package org.comp.bio.aging.extractor

import java.io.{File => JFile}

import org.backuity.clist._

object Main {

  def main(args: Array[String]): Unit = {
    Cli.parse(args).withCommands(Extract, Annotate).foreach(_.run())
  }
}