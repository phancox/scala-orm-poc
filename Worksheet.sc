object Worksheet {

  val url = "jdbc:log4jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
                                                  //> url  : String = jdbc:log4jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
  //  val url = "jdbc:log4jdbc:postgresql://phancox1/DeltaSoft"

  val pattern = """.*:(.*):""".r                  //> pattern  : scala.util.matching.Regex = .*:(.*):
  val pattern(num) = url                          //> scala.MatchError: jdbc:log4jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1 (of class ja
                                                  //| va.lang.String)
                                                  //| 	at Worksheet$$anonfun$main$1.apply$mcV$sp(Worksheet.scala:7)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at Worksheet$.main(Worksheet.scala:1)
                                                  //| 	at Worksheet.main(Worksheet.scala)
}