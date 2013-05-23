object Sudoku {
  val puzzle =
    """5 3 0  0 7 0  0 0 0
       6 0 0  1 9 5  0 0 0
       0 9 8  0 0 0  0 6 0
     
       8 0 0  0 6 0  0 0 3
       4 0 0  8 0 3  0 0 1
       7 0 0  0 2 0  0 0 6
     
       0 6 0  0 0 0  2 8 0
       0 0 0  4 1 9  0 0 5
       0 0 0  0 8 0  0 7 9"""
      .replaceAll("""\s+""", "").map(_.asDigit).toArray
                                                  //> puzzle  : Array[Int] = Array(5, 3, 0, 0, 7, 0, 0, 0, 0, 6, 0, 0, 1, 9, 5, 0,
                                                  //|  0, 0, 0, 9, 8, 0, 0, 0, 0, 6, 0, 8, 0, 0, 0, 6, 0, 0, 0, 3, 4, 0, 0, 8, 0, 
                                                  //| 3, 0, 0, 1, 7, 0, 0, 0, 2, 0, 0, 0, 6, 0, 6, 0, 0, 0, 0, 2, 8, 0, 0, 0, 0, 4
                                                  //| , 1, 9, 0, 0, 5, 0, 0, 0, 0, 8, 0, 0, 7, 9)

  def solve(p: Array[Int], start: Int = 0): Boolean = start match {
    case -1 => false
    case 81 => true
    case i =>
      if (p(i) > 0) solve(p, i + 1)
      else {
        val cand = (0 until 81).filter(n => (n - i) % 9 * (n / 9 ^ i / 9) * (n / 27 ^ i / 27 | n % 9 / 3 ^ i % 9 / 3) == 0)
          .map(n => 1 << p(n)).foldLeft(0)(_ | _)
        (9 to 0 by -1).exists { n => p(i) = n; (cand >> n & 1) == 0 && solve(p, i + 1) }
      }
  }                                               //> solve: (p: Array[Int], start: Int)Boolean

  solve(puzzle)                                   //> res0: Boolean = true

  (0 until 81).grouped(9).map(_.map("%d" format puzzle(_)).mkString).mkString("\n", "\n", "\n")
                                                  //> res1: String = "
                                                  //| 534678912
                                                  //| 672195348
                                                  //| 198342567
                                                  //| 859761423
                                                  //| 426853791
                                                  //| 713924856
                                                  //| 961537284
                                                  //| 287419635
                                                  //| 345286179
                                                  //| "
}