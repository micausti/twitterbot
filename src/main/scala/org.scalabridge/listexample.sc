//sealed trait IntList

//case class MyList(head: Int, tail: IntList) extends IntList

//case object End extends IntList

//def sum(list:IntList): Int =
//list match{
//  case End => 0
//  case MyList(hd, tl) => hd + sum(tl)

//}


//sum(End)


def expandList(n:Int)
= (1 to n).toList map{case x =>n}

//def expandList(n:Int) =
  //List.fill(n)(n)

val l: List[Int] = List(1, 2, 3)

//val l = List(1, 2, 3)

println(expandList(3))

println(l map expandList)

println(l flatMap expandList)
