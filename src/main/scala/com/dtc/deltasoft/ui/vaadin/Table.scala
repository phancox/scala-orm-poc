package com.dtc.deltasoft.ui.vaadin

import vaadin.scala._
import grizzled.slf4j.Logging

/**
 * Specialisation of [[https://github.com/henrikerola/scaladin Scaladin]] Table class.
 */
class Table extends vaadin.scala.Table with Logging {
  trace("Primary Constructor Commence")

  def lock() = {
    p.getUI().getSession().lock()
  }

  def unlock() = {
    p.getUI().getSession().unlock()
  }

  def addItem(cells: Array[Object], itemId: Object) = {
    p.addItem(cells, itemId)
  }

  override def removeAllItems() = {
    p.removeAllItems()
  }

  trace("Primary Constructor Complete")
}