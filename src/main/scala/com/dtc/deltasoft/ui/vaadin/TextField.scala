package com.dtc.deltasoft.ui.vaadin

import com.dtc.deltasoft.Logging

import vaadin.scala._

/**
 * Specialisation of [[https://github.com/henrikerola/scaladin Scaladin]] TextField class.
 */
class TextField extends vaadin.scala.TextField with Logging {
  trace("Primary Constructor Commence")

  /**
   * Returns the field's value as a [[java.lang.String]].  Overrides default behaviour so that
   * instead of returning an [[scala.Option]], an empty string ("") is returned instead.
   * 
   * @return
   * The field's value as a [[java.lang.String]].
   * 
   */
  def asString = { value.getOrElse("").toString }

  trace("Primary Constructor Complete")
}