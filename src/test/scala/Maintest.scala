import org.scalabridge.MyProcess
import org.scalatest.FlatSpec

class SetSpec extends FlatSpec {
  "Main method" should "run process" in {
    def maintest(args: Array[String]): Unit = {
      val newProcess = new MyProcess
      newProcess.execute(args)
    }
  }
}

