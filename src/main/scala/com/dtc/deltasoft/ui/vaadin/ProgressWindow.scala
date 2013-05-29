package com.dtc.deltasoft.ui.vaadin

import com.dtc.deltasoft.Logging

import vaadin.scala._

/**
 * Specialisation of [[https://github.com/henrikerola/scaladin Scaladin]] Window class.
 * 
 */
class ProgressWindow extends vaadin.scala.Window with Logging {
  trace("Primary Constructor Commence")

  styleName = "dsta-progress-window"
  info("Using style " + styleName)

  caption = "Please wait ..."
  resizable = false
  draggable = false
  closable = false
  modal = true

  width = 300 px;
  height = 80 px;
  center()

  val progressIndicator = new ProgressIndicator()
  progressIndicator.indeterminate = false

  val verticalLayout = new VerticalLayout()
  content = verticalLayout
  verticalLayout.sizeFull()
  verticalLayout.margin = true
  verticalLayout.spacing = true
  verticalLayout.components += progressIndicator
  verticalLayout.setAlignment(progressIndicator, Alignment.MiddleCenter);

  trace("Primary Constructor Commence")
}