package com.dtc.deltasoft.ui.vaadin

import com.dtc.deltasoft.Logging

import vaadin.scala._

/**
 * Specialisation of [[https://github.com/henrikerola/scaladin Scaladin]] Panel class.
 */
class Panel extends vaadin.scala.Panel with Logging {
  trace("Primary Constructor Commence")

  protected val layout = new VerticalLayout {
    content = this
  }

  protected val lblSpacer = new Label

  trace("Primary Constructor Complete")
}