package com.dtc.deltasoft

object Logging {

}

/**
 * Wrapper for SLF4J logging infrastructure.
 *
 */
trait Logging extends grizzled.slf4j.Logging {

  /**
   * Helper class to implicitly extend [[scala.Throwable]].
   */
  implicit class ThrowableHelper(val e: Throwable) { //extends AnyVal {
    def withStackTrace: String = {
      e.toString + "\n" + e.getStackTraceString
    }
  }

}
