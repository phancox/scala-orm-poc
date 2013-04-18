package com.dtc.deltasoft.ui.vaadin

import grizzled.slf4j.Logging
import vaadin.scala._

/**
 * Specialisation of [[https://github.com/henrikerola/scaladin Scaladin]] Panel class.
 */
class Panel extends vaadin.scala.Panel with Logging {
  trace("Panel Primary Constructor Commence")

  protected val layout = new VerticalLayout {
    content = this
  }

  protected val lblSpacer = new Label
}