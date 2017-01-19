package org.comp.bio.aging.extractor

import org.backuity.clist._

object Main {

  def main(args: Array[String]): Unit = {
    //runs the console application
    Cli.parse(args).withCommand(new Extract){ e => e.run() }
  }
}