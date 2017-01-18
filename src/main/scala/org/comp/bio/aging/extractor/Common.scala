package org.comp.bio.aging.extractor

import org.backuity.clist._

/**
  * Created by antonkulaga on 1/18/17.
  */
trait Common { this: Command =>
  def run(): Unit
}
