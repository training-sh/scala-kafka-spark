package workshop.s108_function_curries

object Curries extends App {

    //Curries, a function returns another function as a result of the execution

    def sum(f: Int => Int): (Int, Int) => Int = {
      def sumF(a: Int, b: Int): Int = {
        if (a >= b)
            return a
        return f(a) + sumF(a + 1, b);
      }

      sumF;
    }



    val sumId =  sum(x => x)
    val sumSq =   sum(x => x * x)
    val sumCube =  sum(x => x * x * x)

    println(sumId(0, 3))
    println(sumSq(0, 2))
    println(sumCube(0, 2))


}
